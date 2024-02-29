package com.bitsar.cookshareauthservice.service;

import com.amazonaws.services.cognitoidp.model.*;
import com.bitsar.cookshareauthservice.constants.AuthConstants;
import com.bitsar.cookshareauthservice.dao.UserDao;
import com.bitsar.cookshareauthservice.dao.UserSessionDao;
import com.bitsar.cookshareauthservice.dto.request.ConfirmRegisterRequestDto;
import com.bitsar.cookshareauthservice.dto.request.RegisterRequestDto;
import com.bitsar.cookshareauthservice.dto.response.*;
import com.bitsar.cookshareauthservice.entity.User;
import com.bitsar.cookshareauthservice.entity.UserSession;
import com.bitsar.cookshareauthservice.enums.UserStatus;
import com.bitsar.cookshareauthservice.exception.*;
import com.bitsar.cookshareauthservice.helper.CognitoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static com.bitsar.cookshareauthservice.constants.AuthConstants.INDIAN_COUNTRY_CODE;
import static com.bitsar.cookshareauthservice.constants.AuthConstants.TEN_DIGIT_REGEX;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CognitoHelper cognitoHelper;
    private final UserDao userDao;
    private final UserSessionDao userSessionDao;
    @Value("${idle.timeout.day}")
    private int idleTimeoutDay;
    @Value("${idle.timeout.hour}")
    private int idleTimeoutHour;
    @Value("${idle.timeout.minute}")
    private int idleTimeoutMinute;
    @Value("${idle.timeout.second}")
    private int idleTimeoutSecond;

    /**
     * Registers a user with the provided phone number.
     *
     * @param registerRequestDto The request object containing the user's phone number.
     * @return The response object indicating the success of the registration.
     * @throws InvalidPhoneNumberFormatException if the phone number does not match the expected format.
     * @throws PhoneNumberAlreadyExistsException if the phone number is already registered.
     */
    public RegisterResponseDto signUp(RegisterRequestDto registerRequestDto) {
        // Validate phone number format
        String phoneNumber = registerRequestDto.getPhoneNumber();
        if (!phoneNumber.matches(TEN_DIGIT_REGEX))
            throw new InvalidPhoneNumberFormatException("Phone number should be 10 digits");

        // Check if user already exists
        User user = userDao.findByPhoneNumberInSignUp(phoneNumber);
        if (Objects.nonNull(user) && !user.getStatus().equals(UserStatus.NEW))
            throw new PhoneNumberAlreadyExistsException("Phone number already exists");

        String sessionId;
        // Sign up user if not already registered
        if (Objects.isNull(user)) {
            SignUpResult signUpResult = cognitoHelper.cognitoSignUp(INDIAN_COUNTRY_CODE, phoneNumber);
            sessionId = signUpResult.getUserSub();
            userDao.dbSignUp(registerRequestDto, signUpResult.getUserSub());
        }
        // Resend confirmation code if user already exists
        else {
            cognitoHelper.cognitoResendConfirmCode(user.getPhoneNumber());
            sessionId = user.getCognitoUserName();
        }

        // Return response indicating successful registration
        return RegisterResponseDto.builder()
                .status(AuthConstants.SUCCESS_STATUS)
                .sessionId(sessionId)
                .build();
    }

    /**
     * Confirms user sign up with the provided confirmation code
     *
     * @param confirmRegisterRequestDto the request containing the session id and confirmation code
     * @return the response containing the HTTP status code of the confirmation attempt
     * @throws InvalidRegistrationSessionException if the registration session is invalid
     * @throws IncorrectOTPException               if the OTP is incorrect
     */
    public ConfirmRegisterResponseDto confirmSignUp(ConfirmRegisterRequestDto confirmRegisterRequestDto) {
        // Retrieve the cognito username from the request
        String cognitoUserName = confirmRegisterRequestDto.getSessionId();

        // Find the user by cognito username
        User user = userDao.findByCognitoUserName(cognitoUserName);

        // Throw exception if user is not found
        if (Objects.isNull(user)) throw new InvalidRegistrationSessionException("Invalid registration session");

        ConfirmSignUpResult confirmSignUpResult;
        try {
            // Confirm sign up with the provided confirmation code and user phone number
            confirmSignUpResult = cognitoHelper.cognitoConfirmSignUp(confirmRegisterRequestDto.getConfirmationCode(), user.getPhoneNumber());
        } catch (CodeMismatchException e) {
            // Resend confirmation code and throw exception for incorrect OTP
            cognitoHelper.cognitoResendConfirmCode(user.getPhoneNumber());
            throw new IncorrectOTPException("Wrong OTP passed");
        }

        // If confirmation is successful, update user status and save to database
        if (confirmSignUpResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            user.setStatus(UserStatus.VERIFIED);
            userDao.save(user);
        }

        // Log the confirmation sign up response
        log.info("Confirm sign up response: {}", confirmSignUpResult);

        // Return the confirmation response with HTTP status code
        return ConfirmRegisterResponseDto.builder()
                .status(confirmSignUpResult.getSdkHttpMetadata().getHttpStatusCode())
                .build();
    }

    /**
     * Signs in a user with the given phone number
     *
     * @param phoneNumber the phone number to sign in with
     * @return a response indicating the status of the login and the session ID
     * @throws InvalidPhoneNumberFormatException if the phone number does not match the expected format
     * @throws NotAuthorizedToLoginException     if the user is not authorized to login
     */
    public LoginResponseDto signIn(String phoneNumber) {
        // Validate phone number format
        if (!phoneNumber.matches(TEN_DIGIT_REGEX))
            throw new InvalidPhoneNumberFormatException("Phone number should be 10 digits");

        // Find user by phone number in sign-up
        User user = userDao.findByPhoneNumberInSignUp(phoneNumber);
        // Check if user is null or in new status
        if (Objects.isNull(user) || user.getStatus().equals(UserStatus.NEW))
            throw new NotAuthorizedToLoginException("Phone number is not authorized to login");

        // Initiate sign-in with Cognito
        InitiateAuthResult initiateAuthResult = cognitoHelper.cognitoSignIn(INDIAN_COUNTRY_CODE + phoneNumber);

        // Log the login response
        log.info("Login response: {}", initiateAuthResult);

        // Save user session and return response indicating successful login
        UserSession userSession = userSessionDao.findAndUpdateAndSaveSession(UserSession.builder().session(initiateAuthResult.getSession()).phoneNumber(INDIAN_COUNTRY_CODE + phoneNumber).build());
        return LoginResponseDto.builder()
                .status(AuthConstants.SUCCESS_STATUS)
                .sessionId(userSession.getId())
                .build();
    }

    /**
     * Confirms user sign-in with session ID and OTP
     *
     * @param sessionId the session ID
     * @param otp the one-time password
     * @return the confirmation response DTO
     * @throws InvalidSessionException if the session is invalid
     * @throws IncorrectOTPException if the OTP is incorrect
     */
    public ConfirmLoginResponseDto confirmSignIn(String sessionId, String otp) {
        // Find user session by session ID
        UserSession userSession = userSessionDao.findBySessionId(sessionId);
        if (Objects.isNull(userSession)) throw new InvalidSessionException("Invalid session");

        RespondToAuthChallengeResult respondToAuthChallengeResult;
        try {
            // Respond to MFA challenge using phone number, OTP, and session data
            respondToAuthChallengeResult = cognitoHelper.respondToMFAChallenge(userSession.getPhoneNumber(), otp, userSession.getSession());
        }
        catch (CodeMismatchException ex) {
            // If OTP is incorrect, sign in again and throw an IncorrectOTPException
            LoginResponseDto loginResponseDto = signIn(userSession.getPhoneNumber().replace(INDIAN_COUNTRY_CODE, ""));
            throw new IncorrectOTPException("Wrong OTP passed, new session created : " + loginResponseDto.getSessionId());
        }

        // Update user session with access token, ID token, and refresh token
        userSession.setAccessToken(respondToAuthChallengeResult.getAuthenticationResult().getAccessToken());
        userSession.setIdToken(respondToAuthChallengeResult.getAuthenticationResult().getIdToken());
        userSession.setRefreshToken(respondToAuthChallengeResult.getAuthenticationResult().getRefreshToken());
        userSession.setTimeStamp(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        userSessionDao.updateSession(userSession);

        User user = userDao.findByPhoneNumberInSignUp(userSession.getPhoneNumber().replace(INDIAN_COUNTRY_CODE, ""));

        // Return confirmation response DTO with status and access token
        return ConfirmLoginResponseDto.builder()
                .status(AuthConstants.SUCCESS_STATUS)
                .accessToken(respondToAuthChallengeResult.getAuthenticationResult().getAccessToken())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    /**
     * Refreshes the user's access token and updates the user's session.
     * Throws an exception if the session has expired.
     *
     * @param cognitoUserName the Cognito username of the user
     * @return the confirmation login response DTO with the updated access token and user information
     * @throws IdleTimeExpiredException if the session has expired
     */
    public ConfirmLoginResponseDto refreshToken(String cognitoUserName) {
        // Retrieve user and user session from the database
        User user = userDao.findByCognitoUserName(cognitoUserName);
        UserSession userSession = userSessionDao.findByPhoneNumber(user.getPhoneNumber());

        // Check if the session has expired
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (userSession.getTimeStamp().plusDays(idleTimeoutDay).plusHours(idleTimeoutHour)
                .plusMinutes(idleTimeoutMinute).plusSeconds(idleTimeoutSecond).isBefore(currentLocalDateTime)) {
            throw new IdleTimeExpiredException("Session Expired! Please login again");
        }

        // Refresh the access token using the refresh token
        String refreshToken = userSession.getRefreshToken();
        InitiateAuthResult initiateAuthResult = cognitoHelper.cognitoRefreshToken(refreshToken, cognitoUserName);

        // Log the refresh token response
        log.info("Refresh token response: {}", initiateAuthResult);

        // Update user session with the new access token, ID token, and refresh token
        userSession.setAccessToken(initiateAuthResult.getAuthenticationResult().getAccessToken());
        userSession.setIdToken(initiateAuthResult.getAuthenticationResult().getIdToken());
        userSession.setTimeStamp(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        userSessionDao.updateSession(userSession);

        // Return the confirmation login response DTO with the updated access token and user information
        return ConfirmLoginResponseDto.builder()
                .status(AuthConstants.SUCCESS_STATUS)
                .accessToken(initiateAuthResult.getAuthenticationResult().getAccessToken())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    /**
     * Logs out the user by deleting the user session and performing a global sign out from Cognito.
     *
     * @param accessToken the access token of the user
     * @param cognitoUserName the Cognito username of the user
     * @return ConfirmLogoutResponseDto with the status of the logout operation
     */
    public ConfirmLogoutResponseDto logout(String accessToken, String cognitoUserName) {
        // Delete the user session by phone number
        userSessionDao.deleteByPhoneNumber(userDao.findByCognitoUserName(cognitoUserName).getPhoneNumber());

        // Perform global sign out from Cognito
        cognitoHelper.cognitoGlobalSignOut(accessToken);

        // Return the response with a success status
        return ConfirmLogoutResponseDto.builder()
                .status(AuthConstants.SUCCESS_STATUS)
                .build();
    }
}

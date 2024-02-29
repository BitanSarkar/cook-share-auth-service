package com.bitsar.cookshareauthservice.helper;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.util.StringUtils;
import com.bitsar.cookshareauthservice.constants.AuthConstants;
import com.bitsar.cookshareauthservice.exception.SecretHashGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.bitsar.cookshareauthservice.constants.AuthConstants.GLOBAL_PASSWORD;
import static com.bitsar.cookshareauthservice.constants.AuthConstants.HMAC_SHA256_ALGORITHM;

@Slf4j
@Component
@RequiredArgsConstructor
public class CognitoHelper {

    private final AWSCognitoIdentityProvider cognitoClient;
    @Value("${cognito.clientId}")
    private String clientId;
    @Value("${cognito.client-secret}")
    private String clientSecret;

    /**
     * Signs up a user in Cognito using the specified country code and phone number.
     *
     * @param countryCode the country code of the user's phone number
     * @param phoneNumber the user's phone number
     * @return the result of the sign up process
     */
    public SignUpResult cognitoSignUp(String countryCode, String phoneNumber) {
        // Combine the country code and phone number to form the username
        String username = countryCode + phoneNumber;

        // Create a sign up request with the necessary parameters
        SignUpRequest signUpRequest = new SignUpRequest()
                .withClientId(clientId)
                .withSecretHash(calculateSecretHash(username))
                .withUsername(username)
                .withPassword(GLOBAL_PASSWORD)
                .withUserAttributes(
                        new AttributeType()
                                .withName("phone_number")
                                .withValue(username)
                );

        // Sign up the user using the Cognito client
        return cognitoClient.signUp(signUpRequest);
    }

    /**
     * Confirms the sign-up using the provided confirmation code and phone number
     *
     * @param confirmationCode The confirmation code
     * @param phoneNumber      The phone number
     * @return The result of the sign-up confirmation
     */
    public ConfirmSignUpResult cognitoConfirmSignUp(String confirmationCode, String phoneNumber) {
        // Create a confirm sign-up request
        ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
                .withClientId(clientId)
                .withSecretHash(calculateSecretHash(phoneNumber))
                .withUsername(phoneNumber)
                .withConfirmationCode(confirmationCode);
        // Confirm sign-up using Cognito client
        return cognitoClient.confirmSignUp(confirmSignUpRequest);
    }

    /**
     * Resends the confirmation code for the given phone number using Amazon Cognito.
     *
     * @param phoneNumber The phone number for which to resend the confirmation code
     */
    public void cognitoResendConfirmCode(String phoneNumber) {
        // Create a request to resend the confirmation code
        ResendConfirmationCodeRequest request = new ResendConfirmationCodeRequest()
                .withClientId(clientId) // Set the client ID
                .withSecretHash(calculateSecretHash(phoneNumber)) // Calculate and set the secret hash
                .withUsername(phoneNumber); // Set the username as the phone number
        cognitoClient.resendConfirmationCode(request); // Send the request to the Amazon Cognito client
    }

    /**
     * Authenticates the user using their phone number and global password in Cognito.
     *
     * @param phoneNumber the user's phone number
     * @return the result of the authentication
     */
    public InitiateAuthResult cognitoSignIn(String phoneNumber) {
        // Set up authentication parameters
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", phoneNumber);
        authParams.put("PASSWORD", GLOBAL_PASSWORD); // Replace GLOBAL_PASSWORD with the actual global password
        authParams.put(AuthConstants.SECRET_HASH_ATTRIBUTE, calculateSecretHash(phoneNumber));

        // Set up the authentication request
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(clientId) // Replace clientId with the actual client ID
                .withAuthParameters(authParams);

        // Initiate the authentication and return the result
        return cognitoClient.initiateAuth(authRequest);
    }

    /**
     * Responds to a Multi-Factor Authentication (MFA) challenge using the provided phone number and confirmation code.
     *
     * @param phoneNumber the phone number associated with the user
     * @param confirmationCode the confirmation code received via SMS
     * @param session the session string
     * @return the result of responding to the MFA challenge
     */
    public RespondToAuthChallengeResult respondToMFAChallenge(String phoneNumber, String confirmationCode, String session) {
        // Create a map to hold the challenge responses
        Map<String, String> challengeResponses = new HashMap<>();
        challengeResponses.put("USERNAME", phoneNumber);
        challengeResponses.put("SMS_MFA_CODE", confirmationCode);
        challengeResponses.put(AuthConstants.SECRET_HASH_ATTRIBUTE, calculateSecretHash(phoneNumber));

        // Create a request to respond to the MFA challenge
        RespondToAuthChallengeRequest challengeRequest = new RespondToAuthChallengeRequest()
                .withChallengeName(ChallengeNameType.SMS_MFA)
                .withClientId(clientId)
                .withChallengeResponses(challengeResponses)
                .withSession(session);

        // Send the challenge response request to Cognito and return the result
        return cognitoClient.respondToAuthChallenge(challengeRequest);
    }


    public InitiateAuthResult cognitoRefreshToken(String refreshToken, String cognitoUserName) {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("REFRESH_TOKEN", refreshToken);
        authParams.put(AuthConstants.SECRET_HASH_ATTRIBUTE, calculateSecretHash(cognitoUserName));
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .withClientId(clientId)
                .withAuthParameters(authParams);
        return cognitoClient.initiateAuth(authRequest);
    }


    /**
     * Calculates the secret hash for the given username.
     *
     * @param userName the username for which the secret hash is to be calculated
     * @return the calculated secret hash
     * @throws SecretHashGenerationException if an error occurs during the calculation
     */
    public String calculateSecretHash(String userName) {
        // Create a secret key for the HMAC-SHA256 algorithm using the client secret
        SecretKeySpec signingKey = new SecretKeySpec(
                clientSecret.getBytes(StringUtils.UTF8),
                HMAC_SHA256_ALGORITHM);

        try {
            // Initialize the MAC algorithm with the secret key
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);

            // Update the MAC with the user name bytes
            mac.update(userName.getBytes(StringUtils.UTF8));

            // Calculate the HMAC-SHA256 and encode as Base64
            byte[] rawHmac = mac.doFinal(clientId.getBytes(StringUtils.UTF8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            // Throw an exception if an error occurs during the calculation
            throw new SecretHashGenerationException("Error while calculating secret hash", e);
        }
    }

    public void cognitoGlobalSignOut(String accessToken) {
        GlobalSignOutRequest signOutRequest = new GlobalSignOutRequest()
                .withAccessToken(accessToken);
        cognitoClient.globalSignOut(signOutRequest);
    }
}

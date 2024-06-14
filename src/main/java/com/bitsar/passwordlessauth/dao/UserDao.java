package com.bitsar.passwordlessauth.dao;

import com.bitsar.passwordlessauth.dto.request.RegisterRequestDto;
import com.bitsar.passwordlessauth.entity.User;
import com.bitsar.passwordlessauth.enums.UserStatus;
import com.bitsar.passwordlessauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.bitsar.passwordlessauth.constants.AuthConstants.INDIAN_COUNTRY_CODE;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final UserRepository userRepository;

    /**
     * Creates a new user in the database.
     *
     * @param registerRequestDto The registration request data transfer object
     * @param cognitoUserName    The Cognito username
     * @return The newly created user object
     */
    public User dbSignUp(RegisterRequestDto registerRequestDto, String cognitoUserName) {
        // Create a new user object
        User user = User.builder()
                .cognitoUserName(cognitoUserName) // Set the Cognito username
                .firstName(registerRequestDto.getFirstName()) // Set the first name from the registration request
                .lastName(registerRequestDto.getLastName()) // Set the last name from the registration request
                .phoneNumber(INDIAN_COUNTRY_CODE + registerRequestDto.getPhoneNumber()) // Set the phone number with Indian country code
                .status(UserStatus.NEW) // Set the user status to NEW
                .build(); // Build the user object

        // Save the user object in the database
        return userRepository.save(user);
    }

    /**
     * Check if a user with the given phone number exists in the sign-up process.
     *
     * @param phoneNumber The phone number to check
     * @return True if a user with the given phone number exists and is not in a NEW status, false otherwise
     */
    public User findByPhoneNumberInSignUp(String phoneNumber) {
        return userRepository.findByPhoneNumber(INDIAN_COUNTRY_CODE + phoneNumber);
    }

    public User findByCognitoUserName(String cognitoUserName) {
        return userRepository.findByCognitoUserName(cognitoUserName);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}

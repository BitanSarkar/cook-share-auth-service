package com.bitsar.passwordlessauth.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutResult;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.bitsar.passwordlessauth.exception.SecretHashGenerationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CognitoHelper.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CognitoHelperDiffblueTest {
    @MockBean
    private AWSCognitoIdentityProvider aWSCognitoIdentityProvider;

    @Autowired
    private CognitoHelper cognitoHelper;

    /**
     * Method under test: {@link CognitoHelper#cognitoSignUp(String, String)}
     */
    @Test
    void testCognitoSignUp() {
        // Arrange
        SignUpResult signUpResult = new SignUpResult();
        when(aWSCognitoIdentityProvider.signUp(Mockito.<SignUpRequest>any())).thenReturn(signUpResult);

        // Act
        SignUpResult actualCognitoSignUpResult = cognitoHelper.cognitoSignUp("GB", "6625550144");

        // Assert
        verify(aWSCognitoIdentityProvider).signUp(Mockito.<SignUpRequest>any());
        assertSame(signUpResult, actualCognitoSignUpResult);
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoSignUp(String, String)}
     */
    @Test
    void testCognitoSignUp2() {
        // Arrange
        when(aWSCognitoIdentityProvider.signUp(Mockito.<SignUpRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("HmacSHA256")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class, () -> cognitoHelper.cognitoSignUp("GB", "6625550144"));
        verify(aWSCognitoIdentityProvider).signUp(Mockito.<SignUpRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoConfirmSignUp(String, String)}
     */
    @Test
    void testCognitoConfirmSignUp() {
        // Arrange
        ConfirmSignUpResult confirmSignUpResult = new ConfirmSignUpResult();
        when(aWSCognitoIdentityProvider.confirmSignUp(Mockito.<ConfirmSignUpRequest>any())).thenReturn(confirmSignUpResult);

        // Act
        ConfirmSignUpResult actualCognitoConfirmSignUpResult = cognitoHelper.cognitoConfirmSignUp("Confirmation Code",
                "6625550144");

        // Assert
        verify(aWSCognitoIdentityProvider).confirmSignUp(Mockito.<ConfirmSignUpRequest>any());
        assertSame(confirmSignUpResult, actualCognitoConfirmSignUpResult);
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoConfirmSignUp(String, String)}
     */
    @Test
    void testCognitoConfirmSignUp2() {
        // Arrange
        when(aWSCognitoIdentityProvider.confirmSignUp(Mockito.<ConfirmSignUpRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("HmacSHA256")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class,
                () -> cognitoHelper.cognitoConfirmSignUp("Confirmation Code", "6625550144"));
        verify(aWSCognitoIdentityProvider).confirmSignUp(Mockito.<ConfirmSignUpRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoResendConfirmCode(String)}
     */
    @Test
    void testCognitoResendConfirmCode() {
        // Arrange
        when(aWSCognitoIdentityProvider.resendConfirmationCode(Mockito.<ResendConfirmationCodeRequest>any()))
                .thenReturn(new ResendConfirmationCodeResult());

        // Act
        cognitoHelper.cognitoResendConfirmCode("6625550144");

        // Assert
        verify(aWSCognitoIdentityProvider).resendConfirmationCode(Mockito.<ResendConfirmationCodeRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoResendConfirmCode(String)}
     */
    @Test
    void testCognitoResendConfirmCode2() {
        // Arrange
        when(aWSCognitoIdentityProvider.resendConfirmationCode(Mockito.<ResendConfirmationCodeRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("HmacSHA256")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class, () -> cognitoHelper.cognitoResendConfirmCode("6625550144"));
        verify(aWSCognitoIdentityProvider).resendConfirmationCode(Mockito.<ResendConfirmationCodeRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoSignIn(String)}
     */
    @Test
    void testCognitoSignIn() {
        // Arrange
        InitiateAuthResult initiateAuthResult = new InitiateAuthResult();
        when(aWSCognitoIdentityProvider.initiateAuth(Mockito.<InitiateAuthRequest>any())).thenReturn(initiateAuthResult);

        // Act
        InitiateAuthResult actualCognitoSignInResult = cognitoHelper.cognitoSignIn("6625550144");

        // Assert
        verify(aWSCognitoIdentityProvider).initiateAuth(Mockito.<InitiateAuthRequest>any());
        assertSame(initiateAuthResult, actualCognitoSignInResult);
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoSignIn(String)}
     */
    @Test
    void testCognitoSignIn2() {
        // Arrange
        when(aWSCognitoIdentityProvider.initiateAuth(Mockito.<InitiateAuthRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("USERNAME")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class, () -> cognitoHelper.cognitoSignIn("6625550144"));
        verify(aWSCognitoIdentityProvider).initiateAuth(Mockito.<InitiateAuthRequest>any());
    }

    /**
     * Method under test:
     * {@link CognitoHelper#respondToMFAChallenge(String, String, String)}
     */
    @Test
    void testRespondToMFAChallenge() {
        // Arrange
        RespondToAuthChallengeResult respondToAuthChallengeResult = new RespondToAuthChallengeResult();
        when(aWSCognitoIdentityProvider.respondToAuthChallenge(Mockito.<RespondToAuthChallengeRequest>any()))
                .thenReturn(respondToAuthChallengeResult);

        // Act
        RespondToAuthChallengeResult actualRespondToMFAChallengeResult = cognitoHelper.respondToMFAChallenge("6625550144",
                "Confirmation Code", "Session");

        // Assert
        verify(aWSCognitoIdentityProvider).respondToAuthChallenge(Mockito.<RespondToAuthChallengeRequest>any());
        assertSame(respondToAuthChallengeResult, actualRespondToMFAChallengeResult);
    }

    /**
     * Method under test:
     * {@link CognitoHelper#respondToMFAChallenge(String, String, String)}
     */
    @Test
    void testRespondToMFAChallenge2() {
        // Arrange
        when(aWSCognitoIdentityProvider.respondToAuthChallenge(Mockito.<RespondToAuthChallengeRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("USERNAME")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class,
                () -> cognitoHelper.respondToMFAChallenge("6625550144", "Confirmation Code", "Session"));
        verify(aWSCognitoIdentityProvider).respondToAuthChallenge(Mockito.<RespondToAuthChallengeRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoRefreshToken(String, String)}
     */
    @Test
    void testCognitoRefreshToken() {
        // Arrange
        InitiateAuthResult initiateAuthResult = new InitiateAuthResult();
        when(aWSCognitoIdentityProvider.initiateAuth(Mockito.<InitiateAuthRequest>any())).thenReturn(initiateAuthResult);

        // Act
        InitiateAuthResult actualCognitoRefreshTokenResult = cognitoHelper.cognitoRefreshToken("ABC123", "janedoe");

        // Assert
        verify(aWSCognitoIdentityProvider).initiateAuth(Mockito.<InitiateAuthRequest>any());
        assertSame(initiateAuthResult, actualCognitoRefreshTokenResult);
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoRefreshToken(String, String)}
     */
    @Test
    void testCognitoRefreshToken2() {
        // Arrange
        when(aWSCognitoIdentityProvider.initiateAuth(Mockito.<InitiateAuthRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("REFRESH_TOKEN")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class, () -> cognitoHelper.cognitoRefreshToken("ABC123", "janedoe"));
        verify(aWSCognitoIdentityProvider).initiateAuth(Mockito.<InitiateAuthRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#calculateSecretHash(String)}
     */
    @Test
    void testCalculateSecretHash() {
        // Arrange, Act and Assert
        assertEquals("ZFTgGm7gQf6p79S00UetubRFpSB/giLyoXBxpdy7dZ4=", cognitoHelper.calculateSecretHash("janedoe"));
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoGlobalSignOut(String)}
     */
    @Test
    void testCognitoGlobalSignOut() {
        // Arrange
        when(aWSCognitoIdentityProvider.globalSignOut(Mockito.<GlobalSignOutRequest>any()))
                .thenReturn(new GlobalSignOutResult());

        // Act
        cognitoHelper.cognitoGlobalSignOut("ABC123");

        // Assert
        verify(aWSCognitoIdentityProvider).globalSignOut(Mockito.<GlobalSignOutRequest>any());
    }

    /**
     * Method under test: {@link CognitoHelper#cognitoGlobalSignOut(String)}
     */
    @Test
    void testCognitoGlobalSignOut2() {
        // Arrange
        when(aWSCognitoIdentityProvider.globalSignOut(Mockito.<GlobalSignOutRequest>any()))
                .thenThrow(new SecretHashGenerationException("An error occurred", new Exception("foo")));

        // Act and Assert
        assertThrows(SecretHashGenerationException.class, () -> cognitoHelper.cognitoGlobalSignOut("ABC123"));
        verify(aWSCognitoIdentityProvider).globalSignOut(Mockito.<GlobalSignOutRequest>any());
    }
}

package com.bitsar.passwordlessauth.controller;

import static org.mockito.Mockito.when;

import com.bitsar.passwordlessauth.dto.request.ConfirmRegisterRequestDto;
import com.bitsar.passwordlessauth.dto.request.RegisterRequestDto;
import com.bitsar.passwordlessauth.dto.response.ConfirmLoginResponseDto;
import com.bitsar.passwordlessauth.dto.response.ConfirmRegisterResponseDto;
import com.bitsar.passwordlessauth.dto.response.LoginResponseDto;
import com.bitsar.passwordlessauth.dto.response.RegisterResponseDto;
import com.bitsar.passwordlessauth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthService authService;

    /**
     * Method under test: {@link AuthController#confirmLogin(String, String)}
     */
    @Test
    void testConfirmLogin() throws Exception {
        // Arrange
        ConfirmLoginResponseDto buildResult = ConfirmLoginResponseDto.builder()
                .accessToken("ABC123")
                .status("Status")
                .build();
        when(authService.confirmSignIn(Mockito.<String>any(), Mockito.<String>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/auth/confirm-login")
                .param("otp", "foo")
                .param("sessionId", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":{\"code\":1000,\"status\":\"SUCCESS\",\"description\":\"\"},\"data\":{\"status\":\"Status\",\"accessToken\":"
                                        + "\"ABC123\"}}"));
    }

    /**
     * Method under test:
     * {@link AuthController#confirmRegister(ConfirmRegisterRequestDto)}
     */
    @Test
    void testConfirmRegister() throws Exception {
        // Arrange
        ConfirmRegisterResponseDto buildResult = ConfirmRegisterResponseDto.builder().status(1).build();
        when(authService.confirmSignUp(Mockito.<ConfirmRegisterRequestDto>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/confirm-register")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        ConfirmRegisterRequestDto buildResult2 = ConfirmRegisterRequestDto.builder()
                .confirmationCode("Confirmation Code")
                .sessionId("42")
                .build();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(buildResult2));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":{\"code\":1000,\"status\":\"SUCCESS\",\"description\":\"\"},\"data\":{\"status\":1}}"));
    }

    /**
     * Method under test: {@link AuthController#login(String)}
     */
    @Test
    void testLogin() throws Exception {
        // Arrange
        LoginResponseDto buildResult = LoginResponseDto.builder().sessionId("42").status("Status").build();
        when(authService.signIn(Mockito.<String>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/auth/login")
                .param("phoneNumber", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":{\"code\":1000,\"status\":\"SUCCESS\",\"description\":\"\"},\"data\":{\"status\":\"Status\",\"sessionId"
                                        + "\":\"42\"}}"));
    }

    /**
     * Method under test: {@link AuthController#register(RegisterRequestDto)}
     */
    @Test
    void testRegister() throws Exception {
        // Arrange
        RegisterResponseDto buildResult = RegisterResponseDto.builder().sessionId("42").status("Status").build();
        when(authService.signUp(Mockito.<RegisterRequestDto>any())).thenReturn(buildResult);
        RegisterRequestDto buildResult2 = RegisterRequestDto.builder().phoneNumber("6625550144")
                .firstName("Jane")
                .lastName("Doe")
                .phoneNumber("6625550144")
                .build();
        String content = (new ObjectMapper()).writeValueAsString(buildResult2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":{\"code\":1000,\"status\":\"SUCCESS\",\"description\":\"\"},\"data\":{\"status\":\"Status\",\"sessionId"
                                        + "\":\"42\"}}"));
    }
}

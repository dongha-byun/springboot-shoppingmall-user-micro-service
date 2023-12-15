package shoppingmall.userservice.login.presentation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.userservice.login.application.LoginService;
import shoppingmall.userservice.login.exception.NotExistsEmailException;
import shoppingmall.userservice.login.exception.TryLoginLockedUserException;
import shoppingmall.userservice.login.exception.WrongPasswordException;
import shoppingmall.userservice.login.presentation.request.LoginRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.shopping.mall", uriPort = 443)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LoginService loginService;

    @Test
    @DisplayName("로그인에 성공한다.")
    void login() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("newUser@test.com", "plainPassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        when(loginService.login(any(), any(), any())).thenReturn(
                "Access-Token-By-Auth-Service"
        );

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(document("login",
                        requestFields(
                                fieldWithPath("email").description("이메일 주소"),
                                fieldWithPath("password").description("비밀번호(평문)")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("Access Token")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("이메일이 존재하지 않는 경우, 로그인에 실패한다.")
    void login_fail_with_not_exists_email() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("notExistsEmail@test.com", "plainPassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        when(loginService.login(any(), any(), any())).thenThrow(
                new NotExistsEmailException()
        );

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(601)))
                .andExpect(jsonPath("$.message", is("존재하지 않는 이메일 입니다.")));
    }

    @Test
    @DisplayName("패스워드가 틀린 경우, 로그인에 실패한다.")
    void login_fail_with_wrong_password_exception() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("user1@test.com", "wrongPassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        when(loginService.login(any(), any(), any())).thenThrow(
                new WrongPasswordException("비밀번호가 틀렸습니다. 5회 이상 실패하시는 경우, 로그인하실 수 없습니다. (현재 1 / 5)")
        );

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(602)))
                .andExpect(jsonPath("$.message", is("비밀번호가 틀렸습니다. 5회 이상 실패하시는 경우, 로그인하실 수 없습니다. (현재 1 / 5)")));
    }

    @Test
    @DisplayName("5회 이상 패스워드를 틀리면, 사용자 계정이 잠긴다.")
    void login_fail_with_try_login_locked_user_exception() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("user1@test.com", "wrongPassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        when(loginService.login(any(), any(), any())).thenThrow(
                new TryLoginLockedUserException("해당 계정은 로그인 실패 횟수 5회를 초과하여 로그인 할 수 없습니다. 관리자에게 문의해주세요.")
        );

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(603)))
                .andExpect(jsonPath("$.message", is("해당 계정은 로그인 실패 횟수 5회를 초과하여 로그인 할 수 없습니다. 관리자에게 문의해주세요.")));
    }
}
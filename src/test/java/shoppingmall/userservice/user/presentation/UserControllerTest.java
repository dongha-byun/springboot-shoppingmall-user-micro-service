package shoppingmall.userservice.user.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.userservice.user.application.UserService;
import shoppingmall.userservice.user.application.dto.UserDto;
import shoppingmall.userservice.user.presentation.request.SignUpRequest;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("사용자가 회원가입에 성공한다.")
    void sign_up() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "신규 가입자", "new@test.com", "new1!", "new1!", "010-1234-1234"
        );
        String content = objectMapper.writeValueAsString(signUpRequest);

        when(userService.signUp(any())).thenReturn(
               UserDto.builder()
                       .id(1L)
                       .name("신규 가입자")
                       .email("new@test.com")
                       .signUpDate(LocalDateTime.of(2023, 8, 25, 12, 1, 2))
                       .build()
        );

        // when & then
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is("신규 가입자")));
    }

    @Test
    @WithMockUser
    @DisplayName("이미 가입된 이메일 정보로는 가입할 수 없다.")
    void sign_up_fail_with_duplicate_email() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "신규 가입자", "new@test.com", "new1!", "new1!", "010-1234-1234"
        );
        String content = objectMapper.writeValueAsString(signUpRequest);

        when(userService.signUp(any())).thenThrow(
                new IllegalArgumentException("이미 가입된 정보가 있습니다.")
        );

        // when & then
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("이미 가입된 정보가 있습니다.")));
    }
}
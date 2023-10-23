package shoppingmall.userservice.user.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.userservice.user.application.UserService;
import shoppingmall.userservice.user.application.dto.FindEmailResultDto;
import shoppingmall.userservice.user.application.dto.FindPwResponseDto;
import shoppingmall.userservice.user.application.dto.LoginUserDto;
import shoppingmall.userservice.user.application.dto.UserDto;
import shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import shoppingmall.userservice.user.domain.UserGrade;
import shoppingmall.userservice.user.presentation.request.FindEmailRequest;
import shoppingmall.userservice.user.presentation.request.FindPwRequest;
import shoppingmall.userservice.user.presentation.request.SignUpRequest;
import shoppingmall.userservice.user.presentation.request.UserEditRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.shopping.mall", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
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
                        .telNo("010-1234-1234")
                        .signUpDate(LocalDateTime.of(2023, 8, 25, 12, 1, 2))
                        .build()
        );

        // when & then
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is("신규 가입자")))
                .andDo(document("signUp",
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("가입 이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("confirmPassword").description("비밀번호 확인"),
                                fieldWithPath("telNo").description("연락처")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 고유 ID"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("가입 이메일"),
                                fieldWithPath("telNo").description("가입 연락처"),
                                fieldWithPath("signUpDate").description("가입일자")
                        )
                ));
    }

    @Test
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
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("이미 가입된 정보가 있습니다.")))
                .andDo(document("signUp_fail_duplicate_email"))
        ;
    }

    @Test
    @DisplayName("가입한 이메일 정보를 조회한다.")
    void find_email() throws Exception {
        // given
        FindEmailRequest findEmailRequest = new FindEmailRequest(
                "신규가입자", "010-2222-3333"
        );
        String content = objectMapper.writeValueAsString(findEmailRequest);

        when(userService.findEmail(any())).thenReturn(
                new FindEmailResultDto("ne*@test.com")
        );

        // when & then
        mockMvc.perform(post("/find-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email", is("ne*@test.com")))
                .andDo(document("find_email",
                        requestFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("telNo").description("연락처")
                        ),
                        responseFields(
                                fieldWithPath("email").description("가입 시, 사용한 이메일(마스킹 처리돼서 조회됨)")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호를 잃어버렸을 때, 재발급을 위해 회원정보를 확인한다.")
    void find_pw() throws Exception {
        // given
        FindPwRequest findPwRequest = new FindPwRequest(
                "신규사용자", "010-1234-2345", "newUser@test.com"
        );
        String content = objectMapper.writeValueAsString(findPwRequest);

        when(userService.findPw(any())).thenReturn(
                new FindPwResponseDto(
                        10L, "신규사용자",
                        "010-1234-2345", "newUser@test.com"
                )
        );

        // when & then
        mockMvc.perform(post("/find-pw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(10)))
                .andDo(document("find_pw",
                        requestFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("telNo").description("연락처"),
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("사용자 고유 ID"),
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("telNo").description("연락처"),
                                fieldWithPath("email").description("이메일")
                        )
                ));
    }

    @Test
    @DisplayName("사용자가 자신의 회원정보를 조회한다.")
    void find_user() throws Exception {
        // given
        when(userService.findUser(any())).thenReturn(
                new UserDto(
                        100L, "사용자100",
                        "user100@test.com", "010-1234-2345",
                        LocalDateTime.of(2022, 12, 22, 11, 34, 19)
                )
        );

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/{userId}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(100)))
                .andExpect(jsonPath("signUpDate", is("2022-12-22")))
                .andDo(document("find_user",
                        pathParameters(
                                parameterWithName("userId").description("사용자 고유 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 고유 ID"),
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("email").description("사용자 Email"),
                                fieldWithPath("telNo").description("사용자 연락처"),
                                fieldWithPath("signUpDate").description("회원가입 일자")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("로그인한 사용자가 자신의 회원정보를 수정한다.")
    void update_user() throws Exception {
        // given
        UserEditRequest userEditRequest = new UserEditRequest("testPassword", "010-2345-3456");
        String content = objectMapper.writeValueAsString(userEditRequest);
        when(userService.editUser(any(), any())).thenReturn(
                new UserDto(
                        1000L, "사용자100",
                        "user100@test.com", "010-2345-3456",
                        LocalDateTime.of(2022, 12, 22, 11, 34, 19)
                )
        );

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/users/{id}", 1000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1000)))
                .andExpect(jsonPath("signUpDate", is("2022-12-22")))
                .andDo(document("update_user",
                        requestFields(
                                fieldWithPath("password").description("비밀번호(평문)"),
                                fieldWithPath("telNo").description("연락처")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 고유 ID"),
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("email").description("가입 이메일"),
                                fieldWithPath("telNo").description("연락처"),
                                fieldWithPath("signUpDate").description("2022-12-22")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 사용자가 자신의 등급정보를 조회한다.")
    void find_user_grade_info() throws Exception {
        // given
        when(userService.getUserGradeInfo(any())).thenReturn(
                new UserGradeInfoDto(
                        1000L, "사용자1000",
                        LocalDateTime.of(2022, 12, 22, 0, 0, 0),
                        UserGrade.REGULAR, UserGrade.VIP,
                        50, 10000
                )
        );

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/{id}/grade-info", 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(1000)))
                .andExpect(jsonPath("userName", is("사용자1000")))
                .andExpect(jsonPath("signUpDate", is("2022-12-22")))
                .andExpect(jsonPath("currentUserGrade", is("단골회원")))
                .andExpect(jsonPath("nextUserGrade", is("VIP")))
                .andDo(document("find_user_grade_info",
                        pathParameters(
                                parameterWithName("id").description("사용자 고유 ID")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("사용자 고유 ID"),
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("signUpDate").description("가입일자"),
                                fieldWithPath("currentUserGrade").description("현재 회원등급"),
                                fieldWithPath("gradeDiscountRate").description("등급 할인율"),
                                fieldWithPath("nextUserGrade").description("다음 회원등급"),
                                fieldWithPath("remainedOrderCountForNextGrade").description("다음 회원등급 승급까지 남은 주문 수"),
                                fieldWithPath("remainedAmountsForNextGrade").description("다음 회원등급 승급까지 남은 주문 금액")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("가입한 email 로 사용자 정보를 조회한다.")
    void find_user_by_email() throws Exception {
        // given
        when(userService.findUserForLogin(any())).thenReturn(
                new LoginUserDto(
                        100L, "newUser@test.com", 0, "encryptPasswordOfSpringSecurity"
                )
        );

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users?email={email}", "newUser@test.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(100)))
                .andExpect(jsonPath("email", is("newUser@test.com")))
                .andExpect(jsonPath("loginFailCount", is(0)))
                .andExpect(jsonPath("password", is("encryptPasswordOfSpringSecurity")))
                .andDo(document("find_user_by_email",
                        queryParameters(
                                parameterWithName("email").description("가입 이메일")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("사용자 고유 ID"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("loginFailCount").description("로그인 실패 횟수"),
                                fieldWithPath("password").description("암호화된 비밀번호")
                        )
                ));
    }
}

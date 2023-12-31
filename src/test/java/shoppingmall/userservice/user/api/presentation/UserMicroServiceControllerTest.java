package shoppingmall.userservice.user.api.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.userservice.user.api.dao.UserQueryDAO;
import shoppingmall.userservice.user.api.dao.dto.UserInfoDto;
import shoppingmall.userservice.user.api.presentation.request.RequestUserInfo;
import shoppingmall.userservice.user.domain.UserGrade;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class UserMicroServiceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserQueryDAO userQueryDAO;

    @Test
    @DisplayName("쿠폰을 발급받은 사용자들의 정보를 id로 조회한다.")
    void find_users_has_coupon() throws Exception {
        // given
        RequestUserInfo requestUserInfo = new RequestUserInfo(
                Arrays.asList(10L, 20L, 30L)
        );
        String requestBody = objectMapper.writeValueAsString(requestUserInfo);

        when(userQueryDAO.getUsers(any())).thenReturn(
                Arrays.asList(
                        new UserInfoDto(10L, "사용자 10", UserGrade.NORMAL),
                        new UserInfoDto(20L, "사용자 20", UserGrade.VIP),
                        new UserInfoDto(30L, "사용자 30", UserGrade.NORMAL)
                )
        );

        // when & then
        mockMvc.perform(post("/users/has-coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("특정 회원등급 이상의 사용자를 조회한다.")
    void get_user_ids_above_grade() throws Exception {
        // given
        when(userQueryDAO.getUserIdsAboveGrade(any())).thenReturn(
                Arrays.asList(1L, 2L, 3L)
        );

        // when & then
        mockMvc.perform(get("/users/above-grade?targetGrade=REGULAR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get_user_above_grade",
                                queryParameters(
                                        parameterWithName("targetGrade").description("회원 등급")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("특정 회원의 등급 할인율을 조회한다.")
    void get_user_discount_rate() throws Exception {
        // given
        when(userQueryDAO.getUserGradeOf(any())).thenReturn(
                UserGrade.NORMAL
        );

        // when
        mockMvc.perform(get("/users/{userId}/discount-rate", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }
}
package shoppingmall.userservice.authorization.presentation;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.userservice.authorization.presentation.request.AuthRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.shopping.mall", uriPort = 443)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("사용자 정보를 통해 Access Token 을 발급받는다.")
    void auth() throws Exception {
        // given
        LocalDateTime currentLocalDateTime = LocalDateTime.of(2023, 7, 12, 3, 11, 1);
        Instant instant = currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date currentDate = Date.from(instant);
        AuthRequest authRequest = new AuthRequest(
                100L, "127.0.0.1", currentDate
        );
        String requestBody = objectMapper.writeValueAsString(authRequest);

        // when & then
        mockMvc.perform(post("/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(document("auth",
                        requestFields(
                                fieldWithPath("userId").description("사용자 고유 ID"),
                                fieldWithPath("accessIp").description("접속 IP"),
                                fieldWithPath("currentDate").description("토근 생성 일자")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("Access Token")
                        )
                ));
    }
}
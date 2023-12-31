package shoppingmall.userservice.authentication.email.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.authentication.email.application.dto.EmailAuthenticationInfo;
import shoppingmall.userservice.authentication.email.domain.AuthenticationCodeGenerator;
import shoppingmall.userservice.authentication.email.domain.Email;
import shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCode;
import shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCodeStore;
import shoppingmall.userservice.authentication.email.domain.EmailAuthenticationProcessor;

@Transactional
@RequiredArgsConstructor
@Service
public class EmailAuthenticationService {
    private final EmailAuthenticationCodeStore store;
    private final EmailAuthenticationProcessor emailAuthorizationProcessor;
    private final AuthenticationCodeGenerator codeGenerator;

    public EmailAuthenticationInfo createCode(Email email, LocalDateTime requestTime) {
        // 1. create code
        String code = codeGenerator.generate();

        // 2. save code
        // 2-1. 기존에 저장된 코드가 있나?
        // => 이러면 이미 생성된 코드가 있단 뜻 이므로, 재발송이라고 간주
        String responseMessage = "인증번호가 발송되었습니다.";
        if(store.getCode(email) != null) {
            responseMessage = "인증번호가 재발송되었습니다.";
        }
        EmailAuthenticationCode authCode = new EmailAuthenticationCode(code, requestTime);
        store.save(email, authCode);

        // 3. send email
        // 비동기 처리 필요
        emailAuthorizationProcessor.sendAuthorizationMail(email.getValue(), authCode.getValue());

        return EmailAuthenticationInfo.of(email, authCode, responseMessage);
    }

    public EmailAuthenticationInfo checkCode(Email email, EmailAuthenticationCode code, LocalDateTime checkRequestTime) {
        EmailAuthenticationCode findCode = store.getCode(email);
        if(!findCode.isValidAt(checkRequestTime)) {
            return createCode(email, checkRequestTime);
        }

        if(!findCode.equals(code)) {
            throw new IllegalArgumentException("인증번호가 맞지 않습니다.");
        }
        store.remove(email);
        return EmailAuthenticationInfo.of(email, code, "인증이 완료되었습니다.");
    }


}

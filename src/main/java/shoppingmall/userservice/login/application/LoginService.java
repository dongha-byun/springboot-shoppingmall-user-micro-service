package shoppingmall.userservice.login.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.login.client.AuthServiceClient;
import shoppingmall.userservice.login.client.response.ResponseAuthInfo;
import shoppingmall.userservice.login.exception.NotExistsEmailException;
import shoppingmall.userservice.login.exception.TryLoginLockedUserException;
import shoppingmall.userservice.login.exception.WrongPasswordException;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AuthServiceClient authServiceClient;

    public String login(String email, String password, String accessIp) throws WrongPasswordException {
        User user = userRepository.findUserByLoginInfoEmail(email)
                .orElseThrow(
                        () -> new NotExistsEmailException("이메일이 존재하지 않습니다.")
                );
        validateLogin(password, user);

        ResponseAuthInfo authInfo = authServiceClient.getAuthInfo(user.getId(), accessIp, LocalDateTime.now());

        return authInfo.getAccessToken();
    }

    private void validateLogin(String password, User user) throws WrongPasswordException {
        if(user.isLocked()) {
            throw new TryLoginLockedUserException("해당 계정은 로그인 실패 횟수 5회를 초과하여 로그인 할 수 없습니다. 관리자에게 문의해주세요.");
        }

        if(!user.isEqualPassword(password)) {
            int loginFailCount = user.increaseLoginFailCount();
            throw new WrongPasswordException("비밀번호가 틀렸습니다. 5회 이상 실패하시는 경우, 로그인하실 수 없습니다. (현재 " + loginFailCount + " / 5)");
        }
    }
}

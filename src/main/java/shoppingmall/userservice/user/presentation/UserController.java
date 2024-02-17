package shoppingmall.userservice.user.presentation;

import java.net.MalformedURLException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.userservice.user.application.UserService;
import shoppingmall.userservice.user.application.dto.FindEmailRequestDto;
import shoppingmall.userservice.user.application.dto.FindEmailResultDto;
import shoppingmall.userservice.user.application.dto.FindPwRequestDto;
import shoppingmall.userservice.user.application.dto.FindPwResponseDto;
import shoppingmall.userservice.user.application.dto.LoginUserDto;
import shoppingmall.userservice.user.application.dto.SignUpRequestDto;
import shoppingmall.userservice.user.application.dto.UserDto;
import shoppingmall.userservice.user.application.dto.UserEditDto;
import shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import shoppingmall.userservice.user.presentation.argumentresolver.GatewayAuthentication;
import shoppingmall.userservice.user.presentation.argumentresolver.GatewayAuthInfo;
import shoppingmall.userservice.user.presentation.request.FindEmailRequest;
import shoppingmall.userservice.user.presentation.request.FindPwRequest;
import shoppingmall.userservice.user.presentation.request.SignUpRequest;
import shoppingmall.userservice.user.presentation.request.UserEditRequest;
import shoppingmall.userservice.user.presentation.response.FindEmailResultResponse;
import shoppingmall.userservice.user.presentation.response.FindPwResponse;
import shoppingmall.userservice.user.presentation.response.LoginUserResponse;
import shoppingmall.userservice.user.presentation.response.UserGradeInfoResponse;
import shoppingmall.userservice.user.presentation.response.UserResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        SignUpRequestDto signUpRequestDto = signUpRequest.toDto();
        UserDto userDto = userService.signUp(signUpRequestDto);
        UserResponse response = UserResponse.of(userDto);

        return ResponseEntity.created(URI.create("/user/"+response.getId())).body(response);
    }

    @PostMapping("/find-email")
    public ResponseEntity<FindEmailResultResponse> findEmail(@RequestBody FindEmailRequest findEmailRequest){
        FindEmailRequestDto findEmailRequestDto = findEmailRequest.toDto();
        FindEmailResultDto resultDto = userService.findEmail(findEmailRequestDto);
        FindEmailResultResponse response = FindEmailResultResponse.of(resultDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/find-pw")
    public ResponseEntity<FindPwResponse> findPw(@RequestBody FindPwRequest findPwRequest){
        FindPwRequestDto findPwRequestDto = findPwRequest.toDto();
        FindPwResponseDto findPwResponseDto = userService.findPw(findPwRequestDto);
        FindPwResponse response = FindPwResponse.of(findPwResponseDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> findUser(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo){
        UserDto dto = userService.findUser(gatewayAuthInfo.userId());
        UserResponse response = UserResponse.of(dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long userId,
                                                   @RequestBody UserEditRequest userRequest){
        UserEditDto userEditDto = userRequest.toDto();
        UserDto userDto = userService.editUser(userId, userEditDto);
        UserResponse response = UserResponse.of(userDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/grade-info")
    public ResponseEntity<UserGradeInfoResponse> findUserGradeInfo(@GatewayAuthentication GatewayAuthInfo authInfo) {
        UserGradeInfoDto userGradeInfo = userService.getUserGradeInfo(authInfo.userId());
        UserGradeInfoResponse response = UserGradeInfoResponse.to(userGradeInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/login")
    public ResponseEntity<LoginUserResponse> findUserForLogin(@RequestParam("email") String email) {
        LoginUserDto loginUserDto = userService.findUserForLogin(email);
        LoginUserResponse response = LoginUserResponse.of(loginUserDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/images/logo/grade/{fileName}")
    public Resource gradeLogo(@PathVariable("fileName") String fileName) throws MalformedURLException {
        String path = "/Users/byundongha/byun/spring/file_dir/shopping_upload/image/grade/";
        return new UrlResource("file:" + path + fileName);
    }
}

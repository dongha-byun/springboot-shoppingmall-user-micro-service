package shoppingmall.userservice.user.presentation;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.userservice.user.application.UserService;
import shoppingmall.userservice.user.application.dto.FindEmailRequestDto;
import shoppingmall.userservice.user.application.dto.FindEmailResultDto;
import shoppingmall.userservice.user.application.dto.FindPwRequestDto;
import shoppingmall.userservice.user.application.dto.FindPwResponseDto;
import shoppingmall.userservice.user.application.dto.SignUpRequestDto;
import shoppingmall.userservice.user.application.dto.UserDto;
import shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import shoppingmall.userservice.user.presentation.request.FindEmailRequest;
import shoppingmall.userservice.user.presentation.request.FindPwRequest;
import shoppingmall.userservice.user.presentation.request.SignUpRequest;
import shoppingmall.userservice.user.presentation.request.UserEditRequest;
import shoppingmall.userservice.user.presentation.response.FindEmailResultResponse;
import shoppingmall.userservice.user.presentation.response.FindPwResponse;
import shoppingmall.userservice.user.presentation.response.UserGradeInfoResponse;
import shoppingmall.userservice.user.presentation.response.UserResponse;

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

    @GetMapping("/user")
    public ResponseEntity<UserResponse> findUser(Authentication authentication){
        String userId = authentication.getName();
        UserDto dto = userService.findUser(Long.parseLong(userId));
        UserResponse response = UserResponse.of(dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUser(Authentication authentication,
                                                   @RequestBody UserEditRequest userRequest){
        String userId = authentication.getName();
        UserDto userDto = userService.editUser(Long.parseLong(userId), userRequest);
        UserResponse response = UserResponse.of(userDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/grade-info")
    public ResponseEntity<UserGradeInfoResponse> findUserGradeInfo(Authentication authentication) {
        String userId = authentication.getName();
        UserGradeInfoDto userGradeInfo = userService.getUserGradeInfo(Long.parseLong(userId));
        UserGradeInfoResponse response = UserGradeInfoResponse.to(userGradeInfo);
        return ResponseEntity.ok(response);
    }
}

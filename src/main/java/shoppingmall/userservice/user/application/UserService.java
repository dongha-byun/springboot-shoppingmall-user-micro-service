package shoppingmall.userservice.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.user.application.dto.FindEmailRequestDto;
import shoppingmall.userservice.user.application.dto.FindEmailResultDto;
import shoppingmall.userservice.user.application.dto.FindPwRequestDto;
import shoppingmall.userservice.user.application.dto.FindPwResponseDto;
import shoppingmall.userservice.user.application.dto.SignUpRequestDto;
import shoppingmall.userservice.user.application.dto.UserDto;
import shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserFinder;
import shoppingmall.userservice.user.domain.UserRepository;
import shoppingmall.userservice.user.presentation.request.UserEditRequest;
import shoppingmall.userservice.user.utils.MaskingUtil;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserFinder userFinder;
    private final SignUpValidator signUpValidator;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserDto signUp(SignUpRequestDto signUpRequestDto){
        signUpValidator.validateSignUp(signUpRequestDto);
        String plainPassword = signUpRequestDto.getPassword();
        signUpRequestDto.setPassword(passwordEncoder.encode(plainPassword));

        User user = signUpRequestDto.toEntity();
        userRepository.save(user);

        return UserDto.of(user);
    }

    public UserDto findUser(Long id){
        User user = userFinder.findUserById(id);
        return UserDto.of(user);
    }

    public FindEmailResultDto findEmail(FindEmailRequestDto findEmailRequestDto) {
        User user = userFinder.findEmailOf(
                findEmailRequestDto.getName(),
                findEmailRequestDto.getTelNo()
        );
        String maskingEmail = MaskingUtil.maskingEmail(user.getEmail());

        return FindEmailResultDto.of(maskingEmail);
    }

    public FindPwResponseDto findPw(FindPwRequestDto findPwRequestDto) {
        User user = userFinder.findUserOf(
                findPwRequestDto.getName(),
                findPwRequestDto.getTelNo(),
                findPwRequestDto.getEmail()
        );
        return FindPwResponseDto.of(user);
    }

    @Transactional
    public UserDto editUser(Long userId, UserEditRequest userEditRequest) {
        User user = userFinder.findUserById(userId);
        user.updateUser(userEditRequest.getTelNo(), userEditRequest.getPassword());

        return UserDto.of(user);
    }

    public UserGradeInfoDto getUserGradeInfo(Long userId) {
        User user = userFinder.findUserById(userId);
        return UserGradeInfoDto.of(user);
    }
}

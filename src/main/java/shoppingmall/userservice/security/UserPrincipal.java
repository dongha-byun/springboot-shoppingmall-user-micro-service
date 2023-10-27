package shoppingmall.userservice.security;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

    private Long userId;
    public UserPrincipal(shoppingmall.userservice.user.domain.User user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}

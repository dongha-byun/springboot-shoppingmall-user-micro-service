package shoppingmall.userservice.authorization.domain;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static shoppingmall.userservice.authorization.domain.AuthorizationConstants.BEARER_TYPE;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class AuthorizationExtractor {

    public static String parsingToken(HttpServletRequest request){
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()){
            String value = headers.nextElement();
            if(value.startsWith(BEARER_TYPE)){
                return value.replace(BEARER_TYPE, "").trim();
            }
        }
        return null;
    }
}

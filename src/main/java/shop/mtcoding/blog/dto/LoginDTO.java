package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/* 로그인 API
 * 1. URL : http://localhost:8080/login
 * 2. method : POST (로그인은 select지만 post로 한다. 주소에 적히면 안되니까)
 * 3. 요청body : username=값(String)&password=값(String)
 * 4. MIME타입 : x-www-form-urlencoded
 * 5. 응답 : view를 응답함. index 페이지
 */

@Getter
@Setter
@ToString
public class LoginDTO {
    private String username;
    private String password;

}

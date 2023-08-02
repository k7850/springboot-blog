package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UserUpdateDTO {
    private String username;
    private String password;
    private String newPassword;
    private String email;
}

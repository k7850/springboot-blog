package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/* 글쓰기 API
 * 1. URL : http://localhost:8080/save
 * 2. method : POST
 * 3. 요청body : title=값(String)&content=값(String)
 * 4. MIME타입 : x-www-form-urlencoded
 * 5. 응답 : view를 응답함. index 페이지
 */


@Getter @Setter @ToString
public class WriteDTO {
    private String title;
    private String content;
}

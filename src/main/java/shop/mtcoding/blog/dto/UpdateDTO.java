package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/* 글수정 API
 * 1. URL : http://localhost:8080/board/{id}/update
 * 2. method : POST
 * 3. 요청body : title=값(String)&content=값(String)
 * 4. MIME타입 : x-www-form-urlencoded
 * 5. 응답 : view를 응답함. detail의 해당id 페이지
 * 
 * WriteDTO와 필드가 같아도 새로 만드는게 좋다. 나중에 추가될수도 있으니까
 */


@Getter @Setter @ToString
public class UpdateDTO {
    private String title;
    private String content;
}
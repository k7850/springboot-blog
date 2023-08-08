package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReplyWriteDTO {
    private Integer boardId;
    private String comment;
}

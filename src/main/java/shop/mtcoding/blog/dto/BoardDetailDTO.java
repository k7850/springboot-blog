package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter @ToString
public class BoardDetailDTO {
    private Integer boardId;
    private String boardContent;
    private String boardTitle;
    private Integer boardUserId;
    private Integer replyId;
    private String replyComment;
    private Integer replyUserId;
    private String replyUserUsername;
    private boolean replyOwner; // DB에서 만들어 오세요

    public BoardDetailDTO(Integer boardId, String boardContent, String boardTitle, Integer boardUserId, Integer replyId,
            String replyComment, Integer replyUserId, String replyUserUsername, boolean replyOwner) {
        this.boardId = boardId;
        this.boardContent = boardContent;
        this.boardTitle = boardTitle;
        this.boardUserId = boardUserId;
        this.replyId = replyId;
        this.replyComment = replyComment;
        this.replyUserId = replyUserId;
        this.replyUserUsername = replyUserUsername;
        this.replyOwner = replyOwner;
    }


}

// select
// b.id board_id,
// b.content board_content,
// b.title board_title,
// b.user_id board_user_id,
// r.id reply_id,
// r.comment reply_comment,
// r.user_id reply_user_id,
// ru.username reply_user_username,
// case when r.user_id = 2 then true else false end reply_owner
// from board_tb b left outer join reply_tb r
// on b.id = r.board_id
// left outer join user_tb ru
// on r.user_id = ru.id
// where b.id = 1;


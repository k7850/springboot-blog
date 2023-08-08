package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// User(1) - Reply(N)
// Board(1) - Reply(N)

@Getter @Setter @ToString
@Entity
@Table(name = "Reply_tb")
public class Reply {

    @Id  // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AI
    private Integer id;

    @Column(nullable = false, length = 100)
    private String comment;
    
    @JoinColumn(name="user_id")
    @ManyToOne
    private User user; // FK / 기본컬럼이름 : @JoinColumn(name="user_id")
    
    @ManyToOne
    private Board board; // FK board_id
}

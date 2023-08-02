package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString

@Entity // 이 클래스가 엔티티임을 나타냄. 데이터베이스의 테이블과 자동으로 매핑되는 객체임을 의미
        // application.yml의 ddl-auto가 create면 만들어줌
@Table(name = "user_tb") // 클래스명이랑 DB랑 테이블명이 다르면 직접 매핑
public class User {

    @Id  // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AI
    private Integer id; // int가 아니라 Integer 객체로 받아서 null값 넣어짐

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, length = 20)
    private String email;
    

}

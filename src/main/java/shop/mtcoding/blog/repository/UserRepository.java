package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;

// BoardController, UserController, UserRepository : 내가 어노테이션 적어서 IoC에 올린거
// EntityManager, HttpSession : 자동으로 만들어진거

// 클래스를 IoC에 bean으로 등록(컴퍼넌트스캔)하고 + 데이터베이스와의 상호작용을 처리하는 repository임을 표시
// repository는 DAO랑 비슷한 개념
@Repository
public class UserRepository {
    @Autowired
    private EntityManager em;

    public User findByUsername(String username) {
        try {
            Query query = em.createNativeQuery("select * from user_tb where username=:username",
                    User.class);
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsernameAndPassword(LoginDTO loginDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=:username and password=:password",
                User.class);
        // User에 매핑해서 반환

        query.setParameter("username", loginDTO.getUsername());
        query.setParameter("password", loginDTO.getPassword());

        return (User) query.getSingleResult(); // getSingleResult() : 엔티티 1개만 받아올 때

        // List<User> userList = query.getResultList(); // getResultList() : 엔티티 여러개를
        // List로 받아올 때
        // return userList.get(0);
    }

    @Transactional // db작업 전부 정상적으로 종료되면 커밋, 예외가 발생하면 롤백 / DB 변경(C,U,D) 있는데 @Transactional 빼먹으면 오류뜸
    public void save(JoinDTO joinDTO) {
        System.out.println("테스트:" + 1);
        Query query = em.createNativeQuery(
                "insert into user_tb(username, password, email) values(:username, :password, :email)");
        System.out.println("테스트:" + 2);
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        System.out.println("테스트:" + 3);
        query.executeUpdate(); // 쿼리를 전송 (DBMS에게)
        System.out.println("테스트:" + 4);
    }

    public User findById(int id) {
        Query query = em.createNativeQuery("select * from user_tb where id=:id", User.class);
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    @Transactional
    public void updateUser(UserUpdateDTO DTO, Integer id) {
        Query query = em.createNativeQuery("update user_tb set password=:password, email=:email where id=:id");
        query.setParameter("id", id);
        query.setParameter("password", DTO.getNewPassword());
        query.setParameter("email", DTO.getEmail());
        query.executeUpdate();
    }

}

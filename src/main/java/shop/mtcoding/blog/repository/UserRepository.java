package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;

// BoardController, UserController, UserRepository : 내가 어노테이션 적어서 IoC에 올린거
// EntityManager, HttpSession : 자동으로 만들어진거

// 클래스를 IoC에 bean으로 등록(컴퍼넌트스캔)하고 + 데이터베이스와의 상호작용을 처리하는 repository임을 표시
// repository는 DAO랑 비슷한 개념
@Repository
public class UserRepository {

    @Autowired
    private EntityManager em;

    @Transactional // db작업 전부 정상적으로 종료되면 커밋, 예외가 발생하면 롤백 / DB 변경(C,U,D) 있는데 @Transactional 빼먹으면 오류뜸
    public void save(JoinDTO joinDTO) {
        Query query =  em.createNativeQuery("insert into user_tb(username, password, email) values(:username, :password, :email)");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate();
    }

}

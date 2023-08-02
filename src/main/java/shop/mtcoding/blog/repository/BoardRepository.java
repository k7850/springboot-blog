package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.CountDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;

@Repository
public class BoardRepository {
    @Autowired
    private EntityManager em;

    @Transactional
    public void save(WriteDTO writeDTO, Integer userId){
        Query query = em.createNativeQuery("insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())");
        
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);

        query.executeUpdate();
        
    }


    // select id, title from board_tb
    // resultClass 안붙히고 직접 파싱하려면
    // 배열 Object[] 로 리턴됨
    // Object[0] = 1
    // Object[1] = 제목1
    public Integer count(){
        Query query = em.createNativeQuery("select count(*) from board_tb");
        // 원래는 Object배열로 리턴받는다, Object배열은 칼럼의 연속이다
        // 그룹함수를 써서 하나의 칼럼을 조회하면, Object로 리턴된다
        BigInteger count = (BigInteger) query.getSingleResult();
        // System.out.println("카운트 :"+count);
        return count.intValue();
    }

    public int count2() {
        // Entity(Board, User) 타입이 아니어도, 기본 자료형도 안되더라
        Query query = em.createNativeQuery("select * from board_tb", Board.class);
        List<Board> boardList = query.getResultList();
        return boardList.size();
    }

    public List<Board> findAll(int page){
        final int SIZE = 3;
        // page는 시작 인덱스 번호(1아니고 0부터 시작), size는 페이징할 개수(페이지 넘겨도 보이는 글 개수는 같으니까 상수가 좋다)
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page, :size", Board.class);
        query.setParameter("page", page*SIZE);
        query.setParameter("size", SIZE);

        return query.getResultList();
        
    }

    public Board findById(int id){
        Query query = em.createNativeQuery("select * from board_tb where id=:id", Board.class);
        query.setParameter("id", id);
        return (Board)query.getSingleResult();
    }

    // public Board findById(int id){
    //     Query query = em.createNativeQuery("select * from board_tb where id=:id");
    //     query.setParameter("id", id);
        
    //     Object[] result = (Object[]) query.getSingleResult();
    //     Board board = new Board();

    //     board.setId((Integer)result[0]);
    //     board.setContent((String)result[1]);
    //     board.setCreatedAt((Timestamp)result[2]);
    //     board.setTitle((String)result[3]);
    //     User user = new User();
    //     user.setId((Integer)result[4]);
    //     board.setUser(user);
        
    //     System.out.println("테스트: "+ board.toString());

    //     return board;
    //     // return (Board)query.getSingleResult();
    // }

    

    
    @Transactional
    public void deleteById(int id){
        Query query = em.createNativeQuery("delete from board_tb where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    @Transactional
    public void updateBoard(Board board){
        Query query = em.createNativeQuery("update board_tb set title=:title, content=:content where id=:id");
        query.setParameter("id", board.getId());
        query.setParameter("title", board.getTitle());
        query.setParameter("content", board.getContent());
        query.executeUpdate();
    }

}

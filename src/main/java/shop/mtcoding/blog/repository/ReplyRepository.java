package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;

@Repository
public class ReplyRepository {
    
    @Autowired
    EntityManager em;


    
    public List<Reply> findByboardId(int boardId){
        Query query = em.createNativeQuery("select * from reply_tb where board_id=:boardId order by id desc", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();
    }
    
    @Transactional
    public void deleteById(int id){
        Query query = em.createNativeQuery("delete from reply_tb where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public Reply findById(int id){
        Query query = em.createNativeQuery("select * from reply_tb where id=:id", Reply.class);
        query.setParameter("id", id);
        return (Reply)query.getSingleResult();
    }

    @Transactional
    public void save(ReplyWriteDTO DTO, Integer userId){
        Query query = em.createNativeQuery("insert into reply_tb(comment, board_id, user_id) values(:comment, :boardId, :userId)");
        
        query.setParameter("comment", DTO.getComment());
        query.setParameter("boardId", DTO.getBoardId());
        query.setParameter("userId", userId);

        query.executeUpdate();
        
    }



}

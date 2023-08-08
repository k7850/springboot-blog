package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;
    
    @Autowired
    private ReplyRepository replyRepository;



    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO DTO){

        // 유효성 검사
        if (DTO.getComment() == null || DTO.getComment().isEmpty()) {
            return "redirect:/40x";
        }
        if (DTO.getBoardId() == null || DTO.getBoardId()<=0) {
            return "redirect:/40x";
        }

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // System.out.println("디버그 : DTO : "+DTO);
        // System.out.println("디버그 : sessionUser : "+sessionUser);
        // System.out.println("디버그 : sessionUser.getId() : "+sessionUser.getId());

        // 댓글 쓰기
        replyRepository.save(DTO, sessionUser.getId());
        
        return "redirect:/board/"+DTO.getBoardId();
    }




    @PostMapping("/reply/{id}/delete")
    public String deleteReply(@PathVariable Integer id) { // 1. PathVariable 값 받기

        // 2. 인증검사
        // session에 접근해서 sessionUser 키값 가져오기
        // null이면 로그인페이지로 리다이랙트
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401 에러
        }

        // 3. 권한검사
        Reply reply = replyRepository.findById(id);
        if (sessionUser.getId() != reply.getUser().getId()) {
            return "redirect:/40x"; // 403 에러 권한없음
        }

        // 4. 모델에 접근해서 삭제
        replyRepository.deleteById(id);

        return "redirect:/board/"+reply.getBoard().getId();
    }



}

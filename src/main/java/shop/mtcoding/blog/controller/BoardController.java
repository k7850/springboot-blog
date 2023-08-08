package shop.mtcoding.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        // @RequestParam(defaultValue = "0") : 디폴트값 0으로 (/?page=0)

        // 1. 유효성 검사 x (get요청이라 http body 데이터가 없으니까)
        // 2. 인증 검사 x (로그인 안해도 볼 수 있게 할꺼니까)

        List<Board> boardList = boardRepository.findAll(page);
        int totalCount = boardRepository.count();

        boolean last = false;
        if (totalCount <= (page + 1) * 6) {
            last = true;
        }

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        // request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

        return "index";
    }

    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {

        // validation check (유효성 검사)
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null) {
            return "redirect:/40x";
        }

        // 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        boardRepository.save(writeDTO, sessionUser.getId());
        return "redirect:/";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) { // MVC중 C
        User sessionUser = (User) session.getAttribute("sessionUser");

        List<BoardDetailDTO> dtos = null;
        if (sessionUser == null) {
            dtos = boardRepository.findByIdJoinReply(id, null);
        } else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
        }

        boolean pageOwner = false;
        if (sessionUser != null) {
            pageOwner = sessionUser.getId() == dtos.get(0).getBoardUserId();
        }

        request.setAttribute("dtos", dtos);
        request.setAttribute("pageOwner", pageOwner);

        if(sessionUser != null){
            request.setAttribute("sessionUserId", sessionUser.getId());
        } else{
            request.setAttribute("sessionUserId", -1);
        }

        request.setAttribute("nameUser", boardRepository.findById(id).getUser().getUsername());
        return "board/detail"; // MVC중 V
    }




    @PostMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable Integer id) { // 1. PathVariable 값 받기


        // 2. 인증검사
        // session에 접근해서 sessionUser 키값 가져오기
        // null이면 로그인페이지로 리다이랙트
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401 에러
        }

        // 3. 권한검사
        Board board = boardRepository.findById(id);
        if (sessionUser.getId() != board.getUser().getId()) {
            return "redirect:/40x"; // 403 에러 권한없음
        }

        // 4. 모델에 접근해서 삭제
        boardRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {

        Board board = boardRepository.findById(id);

        // 1. 인증검사 (로그인 상태인가)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401 에러
        }

        // 2. 권한검사 (로그인과 수정하려는게 일치한가)
        if (sessionUser.getId() != board.getUser().getId()) {
            return "redirect:/40x"; // 403 에러 권한없음
        }

        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/update")
    public String boardUpdate(@PathVariable Integer id, UpdateDTO updateDTO) {

        // db수정이 있으면 유효성 검사 해야함
        if (updateDTO.getTitle() == null || updateDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (updateDTO.getContent() == null) {
            return "redirect:/40x";
        }

        // 1. 인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401 에러
        }

        // 2. 권한검사
        if (sessionUser.getId() != boardRepository.findById(id).getUser().getId()) {
            return "redirect:/40x"; // 403 에러 권한없음
        }

        // 핵심로직
        boardRepository.updateBoard(updateDTO, id);
        return "redirect:/board/" + id;
    }




}

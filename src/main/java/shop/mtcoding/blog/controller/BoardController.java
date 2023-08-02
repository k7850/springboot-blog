package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;


    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        // @RequestParam(defaultValue = "0") : 디폴트값 0으로 (/?page=0)

        // 1. 유효성 검사 x (get요청이라 http body 데이터가 없으니까)
        // 2. 인증 검사 x (로그인 안해도 볼 수 있게 할꺼니까)

        List<Board> boardList = boardRepository.findAll(page);
        int totalCount = boardRepository.count();

        boolean last = false;
        if(totalCount<=(page+1)*3){
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

        Board board = boardRepository.findById(id); // MVC중 M (MVC에서 모델은 Board, User등등 만든 모델이 아니라 비지니스로직)

        boolean pageOwner = false;
        if(sessionUser != null){
            pageOwner = sessionUser.getId() == board.getUser().getId();
        }

        request.setAttribute("board", board);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail"; // MVC중 V
    }

    @PostMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable Integer id) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardRepository.findById(id);
        if (sessionUser == null || sessionUser.getId() != board.getUser().getId()) {
            return "redirect:/loginForm";
        }

        boardRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/board/{id}/update")
    public String boardUpdateById(@PathVariable Integer id, HttpServletRequest request) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardRepository.findById(id);
        if (sessionUser == null || sessionUser.getId() != board.getUser().getId()) {
            return "redirect:/loginForm";
        }

        request.setAttribute("board", board);
        return "board/boardUpdateForm";
    }

    @PostMapping("/board/{id}/update")
    public String boardUpdate(@PathVariable Integer id, Board board) {

        board.setId(id);

        board.setUser(boardRepository.findById(board.getId()).getUser());
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null || sessionUser.getId() != board.getUser().getId()) {
            return "redirect:/loginForm";
        }

        boardRepository.updateBoard(board);
        return "redirect:/board/"+id;
    }

}

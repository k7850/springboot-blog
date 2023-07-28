package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.repository.UserRepository;

@Controller // application.yml 에 적은 경로의 파일을 줌
//@RestController // return 값 자체를 줌 , @Controller 클래스일때 메서드에 @ResponseBody 붙인 거랑 같음
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

/////////////////////////////////////////////////////////////////////////////////
    // 실무에서
    @PostMapping("/join") // @PostMapping = 클라이언트가 서버로 데이터를 전송할 때
    public String join(JoinDTO joinDTO) {

        // validation check (유효성 검사)
        if(joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()){
            return "redirect:/40ex";
        }
        if(joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()){
            return "redirect:/40ex";
        }
        if(joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()){
            return "redirect:/40ex";
        }

        userRepository.save(joinDTO); // 핵심 기능
        return "redirect:/loginForm";
    }
    // 
    // 정상
    // @PostMapping("/join")
    // public String join(String username, String password, String email) {
    //     // username=ssar&password=1234&email=ssar@nate.com
    //     System.out.println("username : " + username);
    //     System.out.println("password : " + password);
    //     System.out.println("email : " + email);
    //     return "redirect:/loginForm";
    // }
    //
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) throws IOException{
    //     BufferedReader br = request.getReader();
    //     String body = br.readLine(); // 버퍼가 소비됨
    //     String username = request.getParameter("username"); // 버퍼에 값이 없어서 못꺼냄
    //     System.out.println(body);
    //     System.out.println(username);
    //     return "redirect:/loginForm";
    // }
    //
    // // DispatcherServlet 역할 : 컨트롤러 메서드 찾기, 바디데이터 파싱
    // // DS가 파싱은 안하고 컨트롤러 메서드만 찾은 상황
    // @PostMapping("/join")
    // public String join(HttpServletRequest request){
    //     String username = request.getParameter("username");
    //     String password = request.getParameter("password");
    //     String email = request.getParameter("email");
    //     System.out.println(username+" //// "+password+" //// "+email);
    //     return "redirect:/loginForm";
    // }
/////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/joinForm") // @GetMapping = 클라이언트가 서버에게 데이터를 요청할때
    // 클라이언트가 주소창에 입력한 경로(엔드포인트)가 /joinForm 이면 이 메서드가 실행됨(invoke로 실행되서 메서드 이름이랑 상관없이)
    public String joinForm(){
        return "/user/joinForm"; // @Controller라서 뷰리졸버로 경로의 파일을 실행 , application.yml에 적어둔대로 자동으로 파싱
                                 // 만약 @ResponseBody 를 메서드에 붙였다면 메세지 컨버터로 리턴값 자체를 반환
    }
    
    @GetMapping("/loginForm")
    public String loginForm(){
        return "/user/loginForm";
    }
    
    @GetMapping("/user/updateForm")
    public String userUpdateForm(){
        return "/user/updateForm";
    }

    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }
}

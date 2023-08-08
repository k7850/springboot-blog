package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

@Controller // application.yml 에 적은 경로의 파일을 줌
//@RestController // return 값 자체를 줌 , @Controller 클래스일때 메서드에 @ResponseBody 붙인 거랑 같음
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session; // request는 가방, session은 서랍

    


    //localhost:8080/check?username=ssar
    @GetMapping("/check")
    // ResponseEntity쓰면 리스펀스바디 안써도 되고 내가 직접 안넣어줘도 됨
    public ResponseEntity<String> check(String username){
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new ResponseEntity<String>("유저네임이 중복 되었습니다", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("유저네임을 사용할 수 있습니다", HttpStatus.OK);
    }




    @ResponseBody
    @GetMapping("/test/login")
    public String testLogin() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "로그인이 되지 않았습니다";
        } else {
            return "로그인 됨 : " + sessionUser.getUsername();
        }
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO){

        // validation check (유효성 검사)
        if(loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()){
            return "redirect:/40x";
        }
        if(loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()){
            return "redirect:/40x";
        }

        // 핵심 기능
        try {
            User user = userRepository.findByUsernameAndPassword(loginDTO);
            session.setAttribute("sessionUser", user);

            // System.out.println("테스트성공"+user.getId()+"/"+user.getUsername()+"/"+user.getPassword()+"/"+user.getEmail());
            return "redirect:/";
        } catch (Exception e) {
            // System.out.println("테스트 실패");
            return "redirect:/exLogin";
        }
    }





/////////////////////////////////////////////////////////////////////////////////
    // 실무에서
    @PostMapping("/join") // @PostMapping = 클라이언트가 서버로 데이터를 전송할 때
    public String join(JoinDTO joinDTO) {

        // validation check (유효성 검사)
        if(joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()){
            return "redirect:/40x";
        }
        if(joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()){
            return "redirect:/40x";
        }
        if(joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()){
            return "redirect:/40x";
        }

        // DB에 해당 username이 있는지 체크
        User user = userRepository.findByUsername(joinDTO.getUsername());
        if(user!=null){
            return "redirect:/50x";
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
    

    @GetMapping("/logout")
    public String logout(){
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/user/{id}/updateForm")
    public String userUpdateForm(@PathVariable Integer id, HttpServletRequest request){

        User user = userRepository.findById(id);

        // 1. 인증검사 (로그인 상태인가)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401 에러
        }

        // 2. 권한검사 (로그인과 수정하려는게 일치한가)
        if (sessionUser.getId() != user.getId()) {
            return "redirect:/40x"; // 403 에러 권한없음
        }
        
        request.setAttribute("user", user);

        return "/user/updateForm";
    }

    
    
    
    @PostMapping("/user/{id}/update")
    public String userUpdate(@PathVariable Integer id, UserUpdateDTO DTO){
     
        User user = userRepository.findById(id);

        System.out.println("테스트:DTO:"+DTO);
        System.out.println("테스트:user:"+user);

        // db수정이 있으면 유효성 검사 해야함
        if (DTO.getPassword() == null || DTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (DTO.getNewPassword() == null || DTO.getNewPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (DTO.getEmail() == null || DTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }
        
        // 1. 인증검사 (로그인 상태인가)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401 에러
        }

        // 2. 권한검사 (로그인과 수정하려는게 일치한가)
        if (sessionUser.getId() != user.getId()) {
            return "redirect:/40x"; // 403 에러 권한없음
        }



        if(!(DTO.getPassword().equals(user.getPassword()))){
            return "redirect:/user/"+id+"/updateForm";
        }

        userRepository.updateUser(DTO, id);

        session.invalidate();
        return "redirect:/loginForm";
    }







    
}

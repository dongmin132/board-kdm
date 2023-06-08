package idusw.springboot.boardkdm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomeController {
//// Field Injection(필드 주입) : Spring Framework 에게 MemoService형 객체를 주입(해줄 것을 알림)
//    @Autowired
//    MemoService memoService;  // MemoService 인터페이스의 구현체를 필드 주입
//

    //localhost: port로 요청을 하면 getAdmin() 메서드를 호출하여 처리하고, /admin/index view에게 전달
    @GetMapping
    public String getAdmin() {
        return "main/index";
    }
}

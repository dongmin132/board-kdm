package idusw.springboot.boardkdm.controller;

import idusw.springboot.boardkdm.domain.Member;
import idusw.springboot.boardkdm.domain.PageRequestDTO;
import idusw.springboot.boardkdm.domain.PageResultDTO;
import idusw.springboot.boardkdm.entity.MemberEntity;
import idusw.springboot.boardkdm.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/members")
public class MemberController {
    //생성자 주입(constructor DI)
    MemberService memberService;
    private HttpSession session;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login-form")
    public String getLoginForm(Model model) {
        //memberService.toString
        model.addAttribute("member", Member.builder().build());
        return "/members/login";
    }

    @PostMapping("/login")
    public String loginMember(@ModelAttribute("member") Member m, Model model, HttpServletRequest request) {    //model 객체는 사용하지 않음
        //@ModelAttribute: 요청으로 전달된 객체(폼에서 입력한 정보를 갖는), email, pw
        Member result = null;

        if ((result = memberService.login(m)) != null) {      //서비스에 로그인 기능을 구현해야 함
            session = request.getSession();
            session.setAttribute("mb", result);
            return "redirect:/";
        } else {
            return "/errors/404";
        }
    }

    @GetMapping("/reg-form")
    public String getRegisterForm(Model model) {
        // Member 형의 객체를 생성하고,
        model.addAttribute("member", Member.builder().build());
        return "/members/register";     //reg-form.html, view resolving
    }


    @PostMapping("/register")
    public String registerMember(@ModelAttribute("member") Member m, Model model) {
        if (memberService.create(m) > 0)
            return "redirect:/";     //홈으로 재지정함 /admin/index.html로 가야됨. : 컨트롤러에게 재지정
        else
            return "errors/404";
    }

    //@RequestMapping(value="/forgot", method=RequestMethod.GET)
    @GetMapping("/forgot")
    public String getForgotForm() {

        return "/members/forgot-password";       //forgot-password.html
    }

    @GetMapping("/{seq}")
    public String getMember(@PathVariable("seq") Long seq, Model model) {
        Member result = new Member(); // 반환
        Member m = new Member(); // 매개변수로 전달
        m.setSeq(seq);
        result = memberService.read(m);
        // MemberService가 MemberRepository에게 전달
        // MemberRepository는 JpaRepository 인터페이스의 구현체를 활용할 수 있음
        model.addAttribute("member", result);
        return "/members/detail";
    }


//    @GetMapping(value = {"", "/"})
//    public String getmemberList(Model model) {
//        //1. 받을게 있을 경우 매개변수를 받아서 (reisterMember()가 매개 변수를 받는 경우)
//        //2. 서비스에게 요청을 전달 - readList()가 처리 후 반환
//        //3. 결과를 view에 전달
//        List<Member> memberList = new ArrayList<>();    //결과를 받을 객체
//        if ((memberList = memberService.readList()) != null) {
//            model.addAttribute("list", memberList);
//            return "/members/list";
//
//        } else {
//            model.addAttribute("error message", "목록 조회에 실패, 권환 확인");
//            return "/errors/404";
//        }
//
//    }

    /*
    @GetMapping(value={"","/","/{pn}/{size}"})  //"" 공백은 없어도 될듯?
    public String listMemberByPageNumber(@PathVariable("pn") int pn,@PathVariable("size") int size, Model model) {
    *///  다른 방법
    @GetMapping(value = {"", "/"})  //?page=@perPage=
    public String listMemberPagination(@RequestParam(value="page",required = false, defaultValue="1") int page,     //현재페이지      //안쓰면 없는 것으로 처리
                                       @RequestParam(value="perPage",required = false, defaultValue="10") int perPage, //레코드수  //안쓰면 없는 것으로 처리
                                       @RequestParam(value="perPagination", required = false, defaultValue="5") int perPagination,
                                       @RequestParam(value="type", required = false, defaultValue="e") String type,
                                       @RequestParam(value="keyword", required = false, defaultValue="@") String keyword,//현재페이지
                                       Model model)    {
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(page)
            .perPage(perPage)
            .perPagination(perPagination)
            .type(type)
            .keyword(keyword)
            .build();
        PageResultDTO<Member, MemberEntity> resultDTO = memberService.getList(pageRequestDTO);
        //List<Member> result = resultDTO.getDtoList();
        if(resultDTO != null) {
            model.addAttribute("result", resultDTO);
            return "/members/list";
        }
        else
            return "/errors/404";
    }

    @GetMapping("/logout")
    public String logoutMember() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/init")
    public String initializeMember() {      //테스트 회원 정보 생성
        //Integer 데이터 흐름, Lambda 식 - 함수형 언어의 특징을 활요
        IntStream.rangeClosed(1, 33).forEach(i -> {
            Member m = Member.builder()
                    .seq(Long.valueOf(i))
                    .email("email" + i + "@induk.ac.kr")
                    .pw("pw" + i)
                    .name("name" + i)
                    .build();
            memberService.create(m);
        });
        return "redirect:/";
    }

    @PutMapping("/{seq}")
    public String updateMember(@ModelAttribute("member") Member member, Model model) {
        if (memberService.update(member) > 0) {
            session.setAttribute("mb", member);
            return "redirect:/";
        } else
            return "/erros/404";
    }

    @DeleteMapping("/{seq}")
    public String deleteMember(@ModelAttribute("member") Member member) {
        //삭제 처리 -> service -> repository -> service -> controller
        if (memberService.delete(member) > 0) {
            session.invalidate();
            return "redirect:/";
        }
        else return "/errors/404";
    }
}

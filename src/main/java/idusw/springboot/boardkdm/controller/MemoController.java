package idusw.springboot.boardkdm.controller;

import idusw.springboot.boardkdm.domain.Memo;
import idusw.springboot.boardkdm.service.MemoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/memo")
@Controller
// @RequestMapping("/api")
public class MemoController {
    // 생성자 주입 (Constructor DI)
    MemoService memoService;
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @GetMapping("/init")
    public String initialize(Model model) {
        List<Memo> result = new ArrayList<>();
        result = memoService.initialize();
        model.addAttribute("attr", result);
        return "/memo/list";
    }

    @GetMapping("/")        //    /memo/
    public String getList(Model model) {
        List<Memo> result = new ArrayList<>();
        result = memoService.readList(); // 여기를 수정함
        model.addAttribute("attr", result);
        return "/memo/list";
    }

    @GetMapping("/{mno}")
    public String getList(@PathVariable("mno") Long mno, Model model) {
        Memo result = new Memo();
        Memo m = new Memo();
        m.setMno(mno);
        result = memoService.read(m); // 여기를 수정함
        model.addAttribute("attr", result);
        return "/memo/one";
    }
    @GetMapping("/tables")     //localhost/tables.html을 통해서 tables에 접근
    public String getTables() {     //메서드이름은 get이나 post를 붙이는것이 좋음
        return "/memo/tables";
    }
}

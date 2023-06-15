package idusw.springboot.boardkdm.controller;

import idusw.springboot.boardkdm.domain.Board;
import idusw.springboot.boardkdm.domain.Member;
import idusw.springboot.boardkdm.domain.PageRequestDTO;
import idusw.springboot.boardkdm.domain.PageResultDTO;
import idusw.springboot.boardkdm.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/boards")
public class BoardController {
    HttpSession session = null;

    private final BoardService boardService; // BoardController에서 사용할 BoardService 객체를 참조하는 변수

    public BoardController(BoardService boardService) {
        // Spring Framework 가 BoardService 객체를 주입, boardService(주입될 객체의 참조변수)
        this.boardService = boardService;
    }

    @GetMapping("/reg-form")
    public String getRegForm(PageRequestDTO pageRequestDTO, Model model, HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
        if (member != null) {
            model.addAttribute("board", Board.builder().build());
            return "/boards/reg-form";      //view
        } else
            return "redirect:/members/login-form"; // 로그인이 안된 상태인 경우
    }

    @PostMapping("")
    public String postBoard(@ModelAttribute("board") Board dto, Model model, HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
        if (member != null) {
            // form에서 hidden 전송하는 방식으로 변경
            dto.setWriterSeq(member.getSeq());
            dto.setWriterEmail(member.getEmail());
            dto.setWriterName(member.getName());

            Long bno = Long.valueOf(boardService.registerBoard(dto));

            return "redirect:/boards"; // 등록 후 목록 보기, get method
        }else
            return "redirect:/members/login-form";  //로그인이 안된 상태인 경우
    }

    @GetMapping(value = {"", "/"})
    public String getBoards(@RequestParam(value="page",required = false, defaultValue="1") int page,     //현재페이지      //안쓰면 없는 것으로 처리
                            @RequestParam(value="perPage",required = false, defaultValue="8") int perPage, //레코드수  //안쓰면 없는 것으로 처리
                            @RequestParam(value="perPagination", required = false, defaultValue="5") int perPagination,
                            @RequestParam(value="type", required = false, defaultValue="e") String type,
                            @RequestParam(value="keyword", required = false, defaultValue="@") String keyword,//현재페이지
                            Model model) { // 중간 본 수정

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .perPage(perPage)
                .perPagination(perPagination)
                .type(type)
                .keyword(keyword)
                .build();

        PageResultDTO<Board,Object[]> pageResultDTO = boardService.findBoardAll(pageRequestDTO);
        model.addAttribute("list",pageResultDTO);
        // model.addAttribute("list", boardService.findBoardAll(pageRequestDTO));

        return "/boards/list";
    }

    @GetMapping("/{bno}")
    public String getBoardByBno(@PathVariable("bno") Long bno, Model model) {
        // Long bno 값을 사용하는 방식을 Board 객체에 bno를 설정하여 사용하는 방식으로 변경
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        //boardService.updateBoard(board);
        model.addAttribute("board", board);
        return "boards/detail";
    }

    @GetMapping("/{bno}/up-form")
    public String getUpForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        model.addAttribute("board", board);
        return "/boards/up-form";
    }

    @PutMapping("/{bno}")
    public String putBoard(@ModelAttribute("board") Board board, Model model) {
        boardService.updateBoard(board);
        model.addAttribute(boardService.findBoardById(board));
        return "redirect:/boards/" + board.getBno();
    }

    @GetMapping("/{bno}/del-form")
    public String getDelForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        model.addAttribute("board", board);
        return "/boards/del-form";
    }

    @DeleteMapping("/{bno}")
    public String deleteBoard(@ModelAttribute("board") Board board, Model model) {
        boardService.deleteBoard(board);
        model.addAttribute(board);
        return "redirect:/boards";
    }


    @PostMapping("/")
    public String createMember(@ModelAttribute("board") Board board, Model model) { // 등록 처리 -> service -> repository -> service -> controller
        if (boardService.registerBoard(board) > 0) // 정상적으로 레코드의 변화가 발생하는 경우 영향받는 레코드 수를 반환
            return "redirect:/boards";
        else
            return "/errors/404"; // 게시물 등록 예외 처리
    }



    @PostMapping("/{bno}/like")
//    public ResponseEntity<String> postLike(@PathVariable("bno") Long bno, HttpServletRequest request) {
    public String postLike(@PathVariable("bno") Long bno, HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
        try {
            boardService.postLike(bno, member.getEmail());
            return "redirect:/boards/{bno}";
        } catch (IllegalArgumentException e) {
            return "redirect:/boards";
        }
    }
}
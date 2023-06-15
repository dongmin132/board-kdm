package idusw.springboot.boardkdm.controller;

import idusw.springboot.boardkdm.domain.Board;
import idusw.springboot.boardkdm.entity.MemberEntity;
import idusw.springboot.boardkdm.repository.BoardRepository;
import idusw.springboot.boardkdm.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardControllerTest {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;
    @Test
    void registerBoard() {
        Board board = Board.builder()
                .bno(1L)
                .title("TITLE1")
                .content("CONTENT1")
                .writerSeq(1L)
                .writerEmail("a@a")
                .writerName("aaaaaaa")
                .likeCount(0)
                .build();

        if(boardService.registerBoard(board)>0)
            System.out.printf("등록성공");
        else
            System.out.println("등록실패");
    }
}

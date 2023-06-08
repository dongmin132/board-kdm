package idusw.springboot.boardkdm.controller;

import idusw.springboot.boardkdm.domain.Member;
import idusw.springboot.boardkdm.domain.PageRequestDTO;
import idusw.springboot.boardkdm.domain.PageResultDTO;
import idusw.springboot.boardkdm.entity.MemberEntity;
import idusw.springboot.boardkdm.repository.MemberRepository;
import idusw.springboot.boardkdm.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberControllerTests {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void regTest() {
        List<Member> result = memberService.readList();
        for(Member m : result)
            System.out.println(m.getSeq()+ ":" + m.getEmail() + ":" + m.getName());
    }

    @Test
    @Transactional
        // could not initialize proxy - no Session : Lazy fetch 로 인한 오류
    void readMember() { // seq를 사용해야 함
        Member member = new Member();
        member.setSeq(51L);
        Member result = null;
        if((result = memberService.read(member)) != null )
            System.out.println("조회 성공! " + result.getEmail() + ":::" + result.getName());
        else
            System.out.println("조회 실패!");
    }


    @Test
    void initializeMember() {
        // Integer 데이터 흐름, Lambda 식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 105).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .seq(Long.valueOf(i))
                    .email("email" + i + "@induk.ac.kr")
                    .pw("pw" + i)
                    .name("name" + i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    public void testPageList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(3).perPage(5).build();
        //new
        PageResultDTO<Member, MemberEntity> resultDTO = memberService.getList(pageRequestDTO);
        for(Member member : resultDTO.getDtoList())
            System.out.println(member);
        System.out.println("Prev : " + resultDTO.isPrev()); //PerPagination(화면에 보이는 페이지) = 4인경우, 1-4, 5-8... 일 때 1~4 앞에 prev가 없어두 되게 하는 것
        System.out.println("Next : " + resultDTO.isNext());
        System.out.println("Total Page : " + resultDTO.getTotalPage());
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}

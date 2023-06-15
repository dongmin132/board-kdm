package idusw.springboot.boardkdm.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import idusw.springboot.boardkdm.domain.Member;
import idusw.springboot.boardkdm.domain.Memo;
import idusw.springboot.boardkdm.domain.PageRequestDTO;
import idusw.springboot.boardkdm.domain.PageResultDTO;
import idusw.springboot.boardkdm.entity.MemberEntity;
import idusw.springboot.boardkdm.entity.MemoEntity;
import idusw.springboot.boardkdm.entity.QMemberEntity;
import idusw.springboot.boardkdm.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    //생성자 주입
    MemberRepository memberRepository;
    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }





    private void validateDuplicateMember(MemberEntity member) {       //이미 가입된 회원의 경우 예외 발생
        MemberEntity findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
    }


    private void validateDuplicatePhone(Member member) {       //이미 가입된 번호인 경우 예외 발생
        MemberEntity findPhone = memberRepository.findByMobile(member.getMobile()).orElse(null);
        if (findPhone != null) {
            throw new IllegalStateException("이미 가입된 번호입니다");
        }
    }

    @Override
    public int create(Member m) {
//        validateDuplicateMember(member);
        validateDuplicatePhone(m);
        //DTO -> Entity : Repository에서 처리하기 위해
        MemberEntity entity = MemberEntity.builder()
                .seq(m.getSeq())
                .email(m.getEmail())
                .name(m.getName())
                .pw(m.getPw())
                .mobile(m.getMobile())
                .zipcode(m.getZipcode())
                .build();

        if(memberRepository.save(entity)!= null)//저장 성공
            return 1;
        else
            return 0;
    }

    @Override
    public Member read(Member m) {
        MemberEntity e = memberRepository.getById(m.getSeq());
        Member member = Member.builder()
                .seq(e.getSeq())
                .email(e.getEmail())
                .name(e.getName())
                .build();
        return member;
    }

    @Override
    public List<Member> readList() {
        //1. 매개 변수를 처리하고(처리를 위해 객체 선언 및 초기화), 리파지터리 객체에게 전달
        //2.  리파지터리 객체의 해당 메소드가 처리한 후 결과를 반환 유형으로 변환

        //repository : entity를 대상으로 함, service : dto or domain 객체를 대상으로 함.

        // 테이블로 부터 모두 읽어와 객체의 리스트로 반환
        // entity : Service <-> Repository
        List<Member> result = new ArrayList<>(); // Memo 객체를 원소로 갖는 리스트형 객체를 생성, 배정
        List<MemberEntity> entities = memberRepository.findAll(); // select * from a_memo;
        for(MemberEntity e : entities) {
            //이렇게 builder()를 사용해두 되고, setter를 사용해서 만들어도 됨.
            //Function Language 특징을 활용한 처리 (메서드 안에서 메서드를 정의한 형태) lamda식과 유사하게 사용하는 방식
            //Lombok library가 필요함
            Member m = Member.builder()
                    .seq(e.getSeq())
                    .email(e.getEmail())
                    .name(e.getName())
                    .pw(e.getPw())
                    .mobile(e.getMobile())
                    .zipcode(e.getZipcode())
                    .modDate(e.getModDate())
                    .regDate(e.getRegDate())
                    .build();

            result.add(m);
        }
        return result;
    }

    @Override
    public int update(Member m) {
        MemberEntity entity = MemberEntity.builder()
                .seq(m.getSeq())
                .email(m.getEmail())
                .name(m.getName())
                .pw(m.getPw())
                .build();
        if(memberRepository.save(entity)!= null)//저장 성공
            return 1;
        else
            return 0;
    }

    @Override
    public int delete(Member m) {
        MemberEntity entity = MemberEntity.builder()
                .seq(m.getSeq())
                .build();
        memberRepository.deleteById(entity.getSeq());
        return 1;
    }

    @Override
    public Member login(Member m) {
        Member result = null;
        MemberEntity entity = memberRepository.getByEmailPw(m.getEmail(), m.getPw());
        if (entity != null) {
            result = new Member();
            result.setSeq(entity.getSeq());
            result.setEmail(entity.getEmail());
            result.setName(entity.getName());
            result.setPw(entity.getPw());
        }

        return result;
    }

    @Override
    public PageResultDTO<Member, MemberEntity> getList(PageRequestDTO requestDTO) {


        Sort sort = Sort.by("seq").ascending();

        Pageable pageable = requestDTO.getPageable(sort);
//        Page<MemberEntity> result = memberRepository.findAll(pageable);

        BooleanBuilder booleanBuilder = findByCondition(requestDTO);
        Page<MemberEntity> result = memberRepository.findAll(booleanBuilder,pageable);


        Function<MemberEntity,Member> fn = (entity->entityToDto(entity));

        PageResultDTO pageResultDTO = new PageResultDTO<>(result, fn,requestDTO.getPerPagination());
        pageResultDTO.setPerPagination(requestDTO.getPerPagination());

        return pageResultDTO;
    }


    private BooleanBuilder findByCondition(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

        BooleanExpression expression = qMemberEntity.seq.gt(0L); // where seq > 0 and title == "title"
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        String keyword = pageRequestDTO.getKeyword();

        System.out.println(type + " 99999999999999999999 " + keyword);

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("e")) { // email로 검색
            conditionBuilder.or(qMemberEntity.email.contains(keyword));
        }
        if(type.contains("n")) { // name으로 검색
            conditionBuilder.or(qMemberEntity.name.contains(keyword));
        }

        if(type.contains("p")) { // phone로 검색
            conditionBuilder.or(qMemberEntity.mobile.contains(keyword));
        }
        /*
        if(type.contains("a")) { // address로 검색
            conditionBuilder.or(qMemberEntity.address.contains(keyword));
        } // 조건을 전부 줄 수도 있으니 if else문 아님
        if(type.contains("l")) {
            conditionBuilder.or(qMemberEntity.level.contains(keyword));
        }
        */

        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}

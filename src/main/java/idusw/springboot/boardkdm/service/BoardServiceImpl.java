package idusw.springboot.boardkdm.service;

import idusw.springboot.boardkdm.domain.Board;
import idusw.springboot.boardkdm.domain.PageRequestDTO;
import idusw.springboot.boardkdm.domain.PageResultDTO;
import idusw.springboot.boardkdm.entity.BoardEntity;
import idusw.springboot.boardkdm.entity.LikeBoardEntity;
import idusw.springboot.boardkdm.entity.MemberEntity;
import idusw.springboot.boardkdm.repository.BoardRepository;
import idusw.springboot.boardkdm.repository.LikeBoardRepository;
import idusw.springboot.boardkdm.repository.MemberRepository;
import idusw.springboot.boardkdm.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor        //클래스의 final 필드에 대한 생성자를 자동으로 생성
@Service
public class BoardServiceImpl implements BoardService {

//    public BoardServiceImpl(BoardRepository boardRepository) {    //@RequiredArgsConstructor 어노테이션으로 생성자 주입이 됨.(필드 주입)
//        this.boardRepository = boardRepository;
//    }
    private final BoardRepository boardRepository;  //final 필드는 클래스에서 초기화를 하던지 객체 생성 시 생성자를 이용해 꼭 초기화 해야됨.
    private final ReplyRepository replyRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final MemberRepository memberRepository;

    @Override
    public int registerBoard(Board dto) {

        //DTO -> Entity : Repository에서 처리하기 위해
        BoardEntity entity = dtoToEntity(dto);
        if(boardRepository.save(entity)!= null)//저장 성공
            return 1;
        else
            return 0;
    }

    @Override
    public Board findBoardById(Board board) {
        Object[] entities= (Object[]) boardRepository.getBoardByBno(board.getBno());
        return entityToDto((BoardEntity) entities[0],(MemberEntity) entities[1],(Long) entities[2]);
    }

    @Override
    public PageResultDTO<Board, Object[]> findBoardAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable= pageRequestDTO.getPageable(Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );
        Function<Object[], Board> fn = (entity -> entityToDto((BoardEntity) entity[0],
                (MemberEntity) entity[1],(Long) entity[2]));
        return new PageResultDTO<>(result,fn,5);
    }

    @Transactional
    @Override
    public int updateBoard(Board board) {
        BoardEntity existingBoard = boardRepository.findById(board.getBno())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));
        existingBoard.updateForm(board);
        if(boardRepository.save(existingBoard)!= null)//저장 성공
            return 1;
        else
            return 0;
    }

    @Transactional
    @Override
    public int deleteBoard(Board board) {
        replyRepository.deleteByBno(board.getBno());    //댓글 삭제
        boardRepository.deleteById(board.getBno());     //게시물 삭제
        return 0;
    }



    public void postLike(Long bno, String username) {
        BoardEntity post = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + bno));

        MemberEntity user = memberRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // 중복 좋아요 체크
        boolean alreadyLiked = likeBoardRepository.existsByMemberAndBoard(user, post);
        if (alreadyLiked) {
            throw new IllegalArgumentException("User has already liked this post.");
        }

        // 좋아요 수 증가
        post.setLikeCount(post.getLikeCount() + 1);

        boardRepository.save(post);

        // 좋아요 기록 저장
        LikeBoardEntity likeBoard = new LikeBoardEntity();
        likeBoard.setMember(user);
        likeBoard.setBoard(post);
        likeBoardRepository.save(likeBoard);
    }










}

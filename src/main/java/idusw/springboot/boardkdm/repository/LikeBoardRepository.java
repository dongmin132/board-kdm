package idusw.springboot.boardkdm.repository;

import idusw.springboot.boardkdm.entity.BoardEntity;
import idusw.springboot.boardkdm.entity.LikeBoardEntity;
import idusw.springboot.boardkdm.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeBoardRepository extends JpaRepository<LikeBoardEntity,Long> {
    boolean existsByMemberAndBoard(MemberEntity member, BoardEntity board);
}

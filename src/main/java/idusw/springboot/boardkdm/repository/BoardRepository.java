package idusw.springboot.boardkdm.repository;

import idusw.springboot.boardkdm.entity.BoardEntity;
import idusw.springboot.boardkdm.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Long>, SearchBoardRepository {

}

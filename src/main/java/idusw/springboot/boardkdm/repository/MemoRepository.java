package idusw.springboot.boardkdm.repository;

import idusw.springboot.boardkdm.entity.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<MemoEntity, Long> {

}

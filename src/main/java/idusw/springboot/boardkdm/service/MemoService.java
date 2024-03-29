package idusw.springboot.boardkdm.service;

import idusw.springboot.boardkdm.domain.Memo;

import java.util.List;

public interface MemoService {
    int create(Memo m);
    Memo read(Memo m);  // mno 값을 넘김
    List<Memo> readList();
    int update(Memo m);
    int delete(Memo m);

    List<Memo> initialize();
}

package idusw.springboot.boardkdm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data   //Getter, Setter, Tostring 등을 모아 놓은 것
//페이지의 결과를 불러 오는 것
public class PageRequestDTO {
    private int page;   //요청하는 페이지
    private int perPage;   //페이지당 게시물 수
    private int perPagination;      //한 화면에 나오는 페이지 수 갯수

    private String type;             //검색 유형
    private String keyword;         //검색어
    public PageRequestDTO() {
        this.page = 1;
        this.perPage = 10;
    }
    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page-1,perPage,sort);
    }
}

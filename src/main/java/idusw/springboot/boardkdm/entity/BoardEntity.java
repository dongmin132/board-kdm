package idusw.springboot.boardkdm.entity;
import idusw.springboot.boardkdm.domain.Board;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.sql.results.graph.Fetch;

@Entity
@Table(name = "a201912018_board")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude="writer")     //ToString 만들때 writer을 빼줌.

//  JPA Auditing을 활용하여서 생성한 사람, 생성일자, 수정한 사람, 수정일자 등을 선택하여서 감사
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "a201912018_board_seq_gen")
    @SequenceGenerator(sequenceName = "a201912018_board_seq", name = "a201912018_board_seq_gen", allocationSize = 1, initialValue = 1)
    private Long bno; // 유일키

    @Column(length = 50, nullable = false)
    private String title; // 제목
    @Column(length=1000,nullable = false)
    private String content; // 내용
//    private Long views; // 조회수
//    private String block; // 차단여부

    @ManyToOne(fetch=FetchType.LAZY)
    private MemberEntity writer;  //연관 관계 지정 : 게시물 다수 - 작성자 1명

    @Column
    private Integer likeCount; // 좋아요 수를 나타내는 필드




    public void updateForm(Board board) {
        this.content = board.getContent();
        this.title = board.getTitle();
    }

}


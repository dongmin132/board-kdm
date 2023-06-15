package idusw.springboot.boardkdm.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "a201912018_likeBoard")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LikeBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "a201912018_likeBoard_seq_gen")
    @SequenceGenerator(sequenceName = "a201912018_likeBoard_seq", name = "a201912018_likeBoard_seq_gen", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne
    private MemberEntity member;

    @ManyToOne
    private BoardEntity board;
}

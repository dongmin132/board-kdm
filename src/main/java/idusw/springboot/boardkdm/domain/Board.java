package idusw.springboot.boardkdm.domain;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@ToString
@EqualsAndHashCode
public class Board {
    //board
    private Long bno;
    private String title;
    private String content;
    private Long replyCount;
    //join
    private Long writerSeq;
    private String writerEmail;
    private String writerName;

    private Integer likeCount=0;
    //auditing
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}

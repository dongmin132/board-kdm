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

    //join
    private Long writerSeq;
    private String writerEmail;
    private String writerName;

    //auditing
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}

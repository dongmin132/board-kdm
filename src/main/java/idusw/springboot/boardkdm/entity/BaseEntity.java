package idusw.springboot.boardkdm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//JPA Auditing 을 위한 공통 추상 클래스
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {      //abstract: 추상 클래스 (추상 메서드: 선언부만 있고 바디는 없는 것)
                                        //이 클래스로 객체 생성하지말아라, 상속에만 써라

    @CreatedDate    //JPA auditing 어노테이션
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;


    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
}

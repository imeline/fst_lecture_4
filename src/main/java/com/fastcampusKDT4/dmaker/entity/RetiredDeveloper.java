package com.fastcampusKDT4.dmaker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
// 생성일이랑 수정일 데이터 잘 들어가기 위해 필요
public class RetiredDeveloper {
    // 삭제 시 실제 삭제하는 것이 아니라 디벨로퍼 쪽에서 개인정보가 될 수 있는 이름,나이 등을 지우고
    // 상태값을 delete로 변경 후, 여기에 정보들을 분리 보관하는 식으로 구현

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    private String memberId;
    private String name;
    // 삭제된 사람의 id,name 만 보관

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;
}

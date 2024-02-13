package com.fastcampusKDT4.dmaker.entity;

import com.fastcampusKDT4.dmaker.type.DeveloperLevel;
import com.fastcampusKDT4.dmaker.type.DeveloperSkillType;
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
public class Developer {

    @Id // 여기서 부터 3줄 다 @Entity 사용시 달아줘야해서 작성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Enumerated(EnumType.STRING)
    private DeveloperLevel developerLevel;

    @Enumerated(EnumType.STRING)
    private DeveloperSkillType developerSkillType;

    private Integer experienceYear;
    private String memberId;
    private String name;
    private Integer age;

    @CreatedDate // jpa에서 자동으로 생성일 값 세팅
    private LocalDateTime createAt;

    @LastModifiedDate // 마찬가지로 자동생성
    private LocalDateTime updateAt;
}

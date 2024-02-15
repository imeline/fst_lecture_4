package com.fastcampusKDT4.dmaker.dto;

import com.fastcampusKDT4.dmaker.entity.Developer;
import com.fastcampusKDT4.dmaker.type.DeveloperLevel;
import com.fastcampusKDT4.dmaker.type.DeveloperSkillType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private String memberId;
    ;

    public static DeveloperDto fromEntity(Developer developer) { // entity로 부터 dto를 만들어서 정보들을 담아주는 메소드
        return DeveloperDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .build();
    }
}

package com.fastcampusKDT4.dmaker.dto;

import com.fastcampusKDT4.dmaker.code.StatusCode;
import com.fastcampusKDT4.dmaker.entity.Developer;
import com.fastcampusKDT4.dmaker.type.DeveloperLevel;
import com.fastcampusKDT4.dmaker.type.DeveloperSkillType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDetailDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYear;
    private String memberId;
    private StatusCode statusCode;
    private String name;
    private Integer age;

    public static DeveloperDetailDto fromEntity(Developer developer) { // entity로 부터 dto를 만들어서 정보들을 담아주는 메소드
        return DeveloperDetailDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYear(developer.getExperienceYear())
                .memberId(developer.getMemberId())
                .statusCode(developer.getStatusCode())
                .name(developer.getName())
                .age(developer.getAge())
                .build();
    }
}

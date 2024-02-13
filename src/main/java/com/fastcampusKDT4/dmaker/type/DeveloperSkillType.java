package com.fastcampusKDT4.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum DeveloperSkillType {
    BACK_END("백엔드 개발자"),
    FRONT_END("프론트엔드 개발자"),
    FULL_END("풀스택 개발자");

    private final String description;
}

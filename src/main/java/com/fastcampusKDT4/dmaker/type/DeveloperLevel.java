package com.fastcampusKDT4.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
    NEW("신입 개발자"), // 생성자
    JUNIOR("주니어 개발자"),
    JUNGNIOR("중니어 개발자"),
    SENIOR("시니어 개발자");

    private  final String description;
}

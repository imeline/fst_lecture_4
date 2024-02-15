package com.fastcampusKDT4.dmaker.dto;

import com.fastcampusKDT4.dmaker.type.DeveloperLevel;
import com.fastcampusKDT4.dmaker.type.DeveloperSkillType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class EditDeveloper {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYears;

        // id, name, age는 같은 사람에서 변하지 않으니 필드 안 만듦
    }
}

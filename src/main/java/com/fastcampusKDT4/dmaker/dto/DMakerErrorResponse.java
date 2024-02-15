package com.fastcampusKDT4.dmaker.dto;

import com.fastcampusKDT4.dmaker.exception.DMakerErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DMakerErrorResponse {
    // 실무에서 api 별로 성공 때 주는 응답들은 다 다른 방식으로 하고
    // 실패는 이런 공통 실패 dto 를 하나 만들어 놓고, 이 구조에 맞춰서 내려줌
    private DMakerErrorCode errorCode;
    private String errorMessage;
}

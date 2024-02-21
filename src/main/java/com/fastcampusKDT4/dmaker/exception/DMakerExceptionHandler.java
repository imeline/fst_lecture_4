package com.fastcampusKDT4.dmaker.exception;

import com.fastcampusKDT4.dmaker.dto.DMakerErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.fastcampusKDT4.dmaker.exception.DMakerErrorCode.*;

@Slf4j
@RestControllerAdvice // 각 컨트롤러에 마다 조언을 해줌
public class DMakerExceptionHandler {
    // controller가 여러 개일 때, 컨트롤러마다 에러 처리 코드를 넣어줄 수 없어서 이 클래스를 만듦

    @ResponseStatus(value = HttpStatus.CONFLICT)
    // HTTP/1.1 200 에러는 이상하니까, 409 에러가 떨어지게 CONFLICT인 상수 값 사용
    // 이번처럼 status을 지정하기보단 세부적인 에러 코드와 에러 메세지에서 정확한 에러의 종류나 원인을 내려주는 게 더 추세
    @ExceptionHandler(DMakerException.class)
    // 이 controller 에서 발생하는 DMakerException은 여기서 처리
    // 복잡하게 각 메소드들이 에러처리를 갖게 하지 않고, 여기서 글로벌하게 처리하게 해줌
    public DMakerErrorResponse handldException(DMakerException e, HttpServletRequest request) { // 요청이 들어온 request를 받을 수 있음
        log.error("errorCode: {}, url: {}, message: {}", e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class, // create 메소드에다가 post 요청을 하면 이런 에러가 발생
            MethodArgumentNotValidException.class  // @NotNull 같은 controller 진입하기도 전의 에러가 발생했을 때
    }) // 전 메소드와 달리 DMakerException 이 아닌 일반적인 Exception 들
    public DMakerErrorResponse handleBadRequest(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage()); // 직접 커스텀한 메세지가 아닌 에러가 던저주는 메세지

        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)  // create 요청해야하는데 get으로 요청했거나, create 요청 방식이 틀렸거나
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    // 최후의 보루 - 모든 exception 다 받음
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage()); // 직접 커스텀한 메세지가 아닌 에러가 던저주는 메세지

        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)  // create 요청해야하는데 get으로 요청했거나, create 요청 방식이 틀렸거나
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}

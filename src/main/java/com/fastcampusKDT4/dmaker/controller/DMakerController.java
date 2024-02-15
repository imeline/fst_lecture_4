package com.fastcampusKDT4.dmaker.controller;

import com.fastcampusKDT4.dmaker.dto.*;
import com.fastcampusKDT4.dmaker.exception.DMakerException;
import com.fastcampusKDT4.dmaker.service.DMakerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
// log 사용 - log는 이벤트에 대한 정보를 시간에 따라 기록하는 것
@RestController
// @Responsebody 와 @Controller 를 합친거
// 여기서 사용자 요청을 받아서, 응답을 json 파일로 변환해 내려주겠다
// @Responsebody 가 자바객체를 다시 HTTP 응답 바디(json)로 변환
// @Requestbody 는 반대의 일을 함
@RequiredArgsConstructor // sevice를 가져오는 거를 자동으로 인젝션(삽입) 해준다.
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();

    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(@PathVariable String memberId) {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);

    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(@Valid @RequestBody CreateDeveloper.Request request)
    // http 스펙에서 맨위부터 요청라인, 헤더, 그 다음이 requestbody 부분인데, 그걸 @RequestBody 뒤 변수에 담음
    // @Valid 는 CreateDeveloper 의 @Notnull 같은 유효성이 동작하게 해줌
    {
        //GET /create-developer HTTP/1.1
        log.info("request : {}", request);
        //request 정보 찍어봄, CreateDeveloper에 toString 있으면 찍기 좋음

        return dMakerService.createDeveloper(request);
        // get으로 무엇가 만드는 건 어색한 동작이긴 함, 여기선 test용으로 간단히

        //return Collections.singletonList("Olaf");
        // 단일 객체를 가진 list는 arraylist보다 이게 맞음

    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(@PathVariable String memberId, @Valid @RequestBody EditDeveloper.Request request) {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(@PathVariable String memberId) {
        return dMakerService.deleteDeveloper(memberId);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    // HTTP/1.1 200 에러는 이상하니까, 409 에러가 떨어지게 CONFLICT인 상수 값 사용
    // 이번처럼 status을 지정하기보단 세부적인 에러 코드와 에러 메세지에서 정확한 에러의 종류나 원인을 내려주는 게 더 추세
    @ExceptionHandler(DMakerException.class)
    // 이 controller 에서 발생하는 DMakerException은 여기서 처리
    // 복잡하게 각 메소드들이 에러처리를 갖게 하지 않고, 여기서 글로벌하게 처리하게 해줌
    public DMakerErrorResponse handldException (DMakerException e, HttpServletRequest request) { // 요청이 들어온 request를 받을 수 있음
        log.error("errorCode: {}, url: {}, message: {}", e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }
}

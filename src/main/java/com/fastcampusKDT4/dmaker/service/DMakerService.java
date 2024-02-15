package com.fastcampusKDT4.dmaker.service;

import com.fastcampusKDT4.dmaker.code.StatusCode;
import com.fastcampusKDT4.dmaker.dto.CreateDeveloper;
import com.fastcampusKDT4.dmaker.dto.DeveloperDetailDto;
import com.fastcampusKDT4.dmaker.dto.DeveloperDto;
import com.fastcampusKDT4.dmaker.dto.EditDeveloper;
import com.fastcampusKDT4.dmaker.entity.Developer;
import com.fastcampusKDT4.dmaker.entity.RetiredDeveloper;
import com.fastcampusKDT4.dmaker.exception.DMakerException;
import com.fastcampusKDT4.dmaker.repository.DeveloperRepository;
import com.fastcampusKDT4.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampusKDT4.dmaker.type.DeveloperLevel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.fastcampusKDT4.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
// 옛날엔 @Autowired 랑 @Inject을 썼으나, 의존성이 높아져 단독으로 테스트하기 어려웠음
public class DMakerService {
    private final DeveloperRepository developerRepository;
    // @RequiredArgsConstructor 덕에 developerRepository가 자동으로 인젝션 됨
    // final 붙이고 @RequiredArgsConstructor 더하면 자동으로 생성자 만들어줌
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional //ACID 특성
    // 이 어노테이션을 단 로직은 자동으로 한 트랜잭션으로 되서, 중간에 오류 시 이 단위로 롤백함

    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) // 여기 @valid 필요 없으면 안 넣어도 됨
    {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYear(request.getExperienceYears())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer); //db에 저장
        return CreateDeveloper.Response.fromEntity(developer);
        // CreateDeveloper의 리퀘스트를 받아서, 리스폰스를 손쉽게 생성해서 응답해주는 createDevelope 메소드 생성
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        // memberId 가 중복되지 않는지 확인
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
//        Optional<Developer> developer = developerRepository.findByMemberId(request.getMemberId());
//        if(developer.isPresent())
//            throw new DMakerException(DUPLICATED_MEMBER_ID);

    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED) // 고용상태인 사람만 확인
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
        // DeveloperDto를 fromEntity로 매핑해서 list 타입으로 변경
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId) // id로 찾은 developer(entity)를
                .map(DeveloperDetailDto::fromEntity)        // DeveloperDetailDto에 있는 fromEntity 메소드로 DetailDto로 바꿈
                .orElseThrow(() -> new DMakerException((NO_DEVELOPER)));
        // map 코드까지 하면 DeveloperDetailDto 이 아니라 optional 타입이므로
        // get() 을 붙여줘야하는데, 더 나아가서 orElseThrow 를 써서
        // developerEntity를 못 가져왔을 때, 오류를 던저주도록 함
        // 제대로 developerEntity를 가져왔으면 get()을 수행해줌
    }

    @Transactional // 이거 없으면 값들이 entity에만 반영되고, dto에 반영 안 됨
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        ); // validateEditDeveloperRequest 클래스에 넣었다가, 여기에서 필요해서 여기로 옮겨옴

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYear(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request, String memberId) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) { // senior을 10년 이하로 보겠다는 정책
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGNIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional // 단 한번의 db 조작이 있어도 넣어주는 편
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOYED -> RETIRED 로 상태 변경
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER)); // 삭제할 developer 정보를 가져오면서, 간단한 valid 체크
        developer.setStatusCode(StatusCode.RETIRED);

        // 2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder() // RetiredDeveloper 엔티티 생성
                .memberId(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);

        return DeveloperDetailDto.fromEntity(developer);
        // 은퇴한 사람의 정보를 dto로 만들어서 넘겨줌
    }
}

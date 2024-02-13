package com.fastcampusKDT4.dmaker.service;

import com.fastcampusKDT4.dmaker.dto.CreateDeveloper;
import com.fastcampusKDT4.dmaker.entity.Developer;
import com.fastcampusKDT4.dmaker.exception.DMakerErrorCode;
import com.fastcampusKDT4.dmaker.exception.DMakerException;
import com.fastcampusKDT4.dmaker.repository.DeveloperRepository;
import com.fastcampusKDT4.dmaker.type.DeveloperLevel;
import com.fastcampusKDT4.dmaker.type.DeveloperSkillType;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.fastcampusKDT4.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.fastcampusKDT4.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;

@Service
@RequiredArgsConstructor
// 옛날엔 @Autowired 랑 @Inject을 썼으나, 의존성이 높아져 단독으로 테스트하기 어려웠음
public class DMakerService {
    private final DeveloperRepository developerRepository;
    // @RequiredArgsConstructor 덕에 developerRepository가 자동으로 인젝션 됨
    // final 붙이고 @RequiredArgsConstructor 더하면 자동으로 생성자 만들어줌

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
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer); //db에 저장
         return CreateDeveloper.Response.fromEntity(developer);
         // CreateDeveloper의 리퀘스트를 받아서, 리스폰스를 손쉽게 생성해서 응답해주는 createDevelope 메소드 생성
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        DeveloperLevel developerLevel = request.getDeveloperLevel(); //
        Integer experienceYears = request.getExperienceYears();
        if(developerLevel == DeveloperLevel.SENIOR
            && experienceYears < 10) { // senior을 10년 이하로 보겠다는 정책
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNGNIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        // memberId 가 중복되지 않는지 확인
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
//        Optional<Developer> developer = developerRepository.findByMemberId(request.getMemberId());
//        if(developer.isPresent())
//            throw new DMakerException(DUPLICATED_MEMBER_ID);

    }

}

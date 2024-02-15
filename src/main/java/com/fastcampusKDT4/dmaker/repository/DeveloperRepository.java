package com.fastcampusKDT4.dmaker.repository;

import com.fastcampusKDT4.dmaker.code.StatusCode;
import com.fastcampusKDT4.dmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    // Develpor 라는 entity를 관리해주는 레포
    Optional<Developer> findByMemberId(String memberId);

    List<Developer> findDeveloperByStatusCodeEquals(StatusCode statusCode);
}

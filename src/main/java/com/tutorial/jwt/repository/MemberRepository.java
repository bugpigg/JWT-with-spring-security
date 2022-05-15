package com.tutorial.jwt.repository;

import com.tutorial.jwt.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserName(String userName);
    Optional<Member> findByUserName(String userName);
}

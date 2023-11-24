package com.kh.jpatotalapp.repository;


import com.kh.jpatotalapp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository // 데이터 베이스와 상호작용하는 역할, 데이터 베이스에 접근해서 데이터를 저장하거나 가져오는 역할을 한다.
public interface MemberRepository extends JpaRepository<Member, Long> {
// Optional은 값이 있을 수도 있고 없을 수도 있을 값을 감싸는 컨테이너 타입, 함수에서 보면 네모인 필터 형태
    Optional<Member> findByEmail(String email); // findByEmail 쿼리메소드를 사용해 일치하는 조건(이메일 값)을 기준으로 Optional<Member> 형태로 반환, 값이 존재 한다면 Optional<Member>객체 안에 정보가 들어가 있을거고 없다면 빈Optional 객체로 반환
   // 회원 가입 여부 확인하기 위해 필요한 조건
    boolean existsByEmail(String email); // JPA에서 제공하는 existsByEmail (쿼리를 생성해주는)메소드 : 존재하는 이메일이 있는지 String email타입과 일치하는 값을 true,false로 값을 받는거
    Optional<Member> findByEmailAndPassword(String email, String password); // 이메일과 비밀번호룰 찾아서 Optional<Member>형태로 반환되는 것 둘다 문자 형태
}

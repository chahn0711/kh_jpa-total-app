package com.kh.jpatotalapp.service;

import com.kh.jpatotalapp.dto.MemberDto;
import com.kh.jpatotalapp.entity.Member;
import com.kh.jpatotalapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j // 롬복 라이브러리에서 제공하는 어노테이션, 간단한 로그메시지 확인 가능, 사용법은 log.info("로그메시지")
@Service // 스프링 프레임워크에서 비즈니스 로직을 담당하는 서비스 클래스에 부여하는 어노테이션, 해당 클래스가 비즈니스 로직을 처리하는 컴포넌트를 표시하는 역할
@RequiredArgsConstructor // 롬복 라이브러리에서 제공하는 어노테이션, 클래스에 final로 선언된 필드에 대한 생성자를 생성해주는 역할, 객체를 생성할 때 초기화 작업을 필요로 하는데 @RequiredArgsConstructor 어노테이션을 생성하면 해당 클래스 내의 final 필드를 가진 생성자를 자동으로 생성해주기 때문에 생성자 호출하는 과정을 생략해 코드를 간단하게 작성 할 수 있다.  + class 안에 @NonNull 어노테이션이 붙으면 final말고 다른 것도 가능??
public class MemberService { // Dto는 데이터를 전송하는 객체다. 반드시 필요한건 아니나 데이터 보안이나 최적화를 위해 Dto를 활용해서 하는거다. Service에서는 1차 필터링한 Repository에서 데이터를 가져와 받은 데이터를 추가로 가공해서 필터링하는 역할, Entity를 Dto로 변환시키는 메소드
    private final MemberRepository memberRepository; // 문장의 큰 의미는 final이 붙어있어 MemberService 클래스내에서 선언된 memberRepository 필드를 통해서만 호출이 가능하다. 이걸 의존성 주입이라하며 MemberService 내에서 직접 MemberRepository를 만드는게 아니고 외부에서 MemberRepository를 불러와사용하는 것
    // 멤버 서비스를 관리하기 위한 기능

    // 회원 가입 여부 확인
    public boolean isMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원 상세 조회 : 일치하는 이메일 없는지 없다면 Dto변환 메소드에 넣어서 정보 반환
    // 주어진 이메일에 해당하는 회원을 찾아 회원의 세부정보를 MemberDto형태로 변환한 후 반환 없으면 예외처리
    public MemberDto getMemberDetail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(// optional이 비어있으면 예외처리
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );
        return convertEntityToDto(member);
    }

    // 회원 가입(회원가입 여부 확인해서 이메일, 이름, 패스워드, 이미지,날짜 받는거 필요)
    public boolean saveMember(MemberDto memberDto) { // saveMember 메서드가 호출될 때 MemberDto 클래스에 정의된 변수를 가져오는거
        Member member = new Member();
        member.setEmail(memberDto.getEmail()); // 예를 들어 MemberDto에 사용자로부터 입력 받았다는 것을 전제로 MemberDto에 받은 값을 각각 불러 새로 만든 객체(member)에 담고 메서드가 저장에 성공하면 true이고 실패하면 false
        member.setName(memberDto.getName());
        member.setPassword(memberDto.getPwd());
        member.setImage(memberDto.getImage());
        member.setRegDate(memberDto.getRegDate());
        memberRepository.save(member);
        return true;
    }
    // 회원 수정
    public boolean modifyMember(MemberDto memberDto) {
        try {
            Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            member.setName(memberDto.getName());
            member.setImage(memberDto.getImage());
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 로그인
    public boolean login(String email, String pwd) {
        log.info("email: {}, pwd: {}", email, pwd);
        Optional<Member> member = memberRepository.findByEmailAndPassword(email, pwd);
        log.info("member: {}", member);
        return member.isPresent();
    }
    // 회원 삭제
    public boolean deleteMember(String email) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            memberRepository.delete(member);
            return true; // 회원이 존재하면 true 반환
        } catch (RuntimeException e) {
            return false; // 회원이 존재하지 않으면 false 반환
        }
    }

    // 회원 전체 조회
    public List<MemberDto> getMemberList() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for(Member member : members) {
            memberDtos.add(convertEntityToDto(member));
        }
        return memberDtos;
    }
    // 총 페이지 수
    public int getMemberPage(Pageable pageable) {
        return memberRepository.findAll(pageable).getTotalPages();
    }

    // 회원 조회 : 페이지 네이션
    public List<MemberDto> getMemberList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Member> members = memberRepository.findAll(pageable).getContent();
        List<MemberDto> memberDtos = new ArrayList<>();
        for(Member member : members) {
            memberDtos.add(convertEntityToDto(member));
        }
        return memberDtos;
    }

    // 회원 엔티티를 회원 DTO로 변환 : 보안이나 최적화를 위해 Dto활용하기 위한 조건
    private MemberDto convertEntityToDto(Member member) { //
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(member.getEmail());
        memberDto.setName(member.getName());
        memberDto.setPwd(member.getPassword());
        memberDto.setImage(member.getImage());
        memberDto.setRegDate(member.getRegDate());
        return memberDto;
    }
}

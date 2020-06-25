package jpabook.jpashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@Service
@Transactional(readOnly = true) // JPA의 모든 데이터 변경이나 로직들은 트랜잭션 안에서 실행되어야 함
// readonly = true는 JPA가 조회하는 곳에서는 성능을 좀더 최적화
//@AllArgsConstructor // 모든 필드에 대한 생성자 만듬
@RequiredArgsConstructor // final 필드에 대한 생성자 만듬
public class MemberService {

	@Autowired
	private final MemberRepository memberRepository; // field 인젝션
	// 장점 : 한번 생성하면 값을 바꿀수 없음
	// 단점 : 못 바꿈

//	@Autowired
//	public void setMemberRepository(MemberRepository memberRepository) { // setter 인젝션
//		this.memberRepository = memberRepository;
//	}
//	// 장점 : 테스트 코드 작성시 Mock 주입 편리
//	// 단점 : 실제 개발시 누군가가 바꿀 위험이 있음

//	public MemberService(MemberRepository memberRepository) { // 생성자 인젝션
//		this.memberRepository = memberRepository;
//	}
	// 장점 : 테스트 코드 작성시 Mock 주입 편리, 한번 생성하면 값을 바꿀수 없음, @Autowired 생략 가능

	// 회원 가입
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId(); // Id값이 항상 있음을 보장-> @GeneratedValue
	}

	private void validateDuplicateMember(Member member) { // 중복회원 검증
		List<Member> findMembers = memberRepository.findByName(member.getName());

		//EXCEPTION
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	// 회원 전체 조회
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	// 회원 단건 조회
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findOne(id);
		member.setName(name);
	}
	// Member를 반환하면, 영속성이 끊긴 상태의 Member가 반환된다. -> command와 query를 철저히 분리해야한다.
}

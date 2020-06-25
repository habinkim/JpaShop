package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional // 스프링의 테스트 트랜잭션은 기본적으로 Commit이 아니라 Rollback
class MemberServiceTest {
	
	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;
	
	// INSERT SQL문이 왜 실행되지 않는가? -> DB 트랜잭션이 커밋될 때 플러쉬되면서 DB에 INSERT
	// -> 어차피 Rollback할거라 INSERT할 이유도 없음
	
	@Test
//	@Rollback(false)
	public void 회원가입() throws Exception {
		// given 
		Member member  = new Member();
		member.setName("kim");
		
		// when
		Long savedId = memberService.join(member);
		
		// then
		em.flush(); // flush: 영속성 컨텍스트에 있는 변경이나 등록 내용을 DB에 반영(롤백하기 전에 강제로 SQL문 실행)
		assertEquals(member, memberRepository.findOne(savedId));
	}
	
	@Test
	public void 중복_회원_예외() throws Exception {
//		Assertions.assertThrows(IllegalStateException.class, () -> {});
		// given
		Member member1 = new Member();
		member1.setName("kim");
		
		Member member2 = new Member();
		member2.setName("kim");
		
		// when
		memberService.join(member1);
		memberService.join(member2); // 예외가 발생해야 한다
		
		
		// then
		fail("예외가 발생해야 한다.");
	}
}

//package jpabook.jpashop;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import jpabook.jpashop.domain.Member;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//public class MemberRepositoryTest {
//	@Autowired MemberRepository memberRepository;
//	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void testMember() throws Exception {
//		//given
//		Member member = new Member();
//		member.setName("memberA");
//		
//		//when
//		Long savedId = memberRepository.save(member);
//		Member findMember = memberRepository.find(savedId);
//		
//		//then
//		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//		Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
//		Assertions.assertThat(findMember).isEqualTo(member);
//		System.out.println("findMember == member: " + (findMember == member));
//		// 영속성 컨텍스트에서 식별자가 같으면 같은 엔티티로 인식
//	}
//}


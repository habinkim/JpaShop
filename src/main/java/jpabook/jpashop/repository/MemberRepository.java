package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository // 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
@RequiredArgsConstructor 
public class MemberRepository {
	
//	@PersistenceContext // 엔티티 매니저 주입
// Spring Data JPA에서 entity manager @Autowired로도 생성자 인젝션 지원
	private final EntityManager em;
	
	public void save(Member member) { em.persist(member); } // 영속성 컨텍스트에 객체 주입
	
	public Member findOne(Long id) { return em.find(Member.class,  id); } // 단건 조회(type, pk)
	
	public List<Member> findAll() { // JPQL 사용 (대상은 테이블이 아니라 엔티티)
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}
	
	public List<Member> findByName(String name) { // JPQL 사용
		return em.createQuery("select m from Member m where m.name = :name",
				Member.class)
				.setParameter("name", name) // 파라미터 바인딩
				.getResultList();
				
	}
}

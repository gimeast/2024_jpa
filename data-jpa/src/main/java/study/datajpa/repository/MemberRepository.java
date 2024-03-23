package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findWowBy();

//    @Query(name = "Member.findByUsernameNamedQuery") //1차로 named query 를 찾고 없으면 2차로 method 명으로 query 호출 방식을 사용한다.
    List<Member> findByUsernameNamedQuery(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findByUser(@Param("username") String username, @Param("age") int age);
}

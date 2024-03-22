# Spring Data JPA

## JpaRepository
* 메소드 이름으로 쿼리 생성이 가능하다.
  * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
  * 2개 이상 조건이 넘어 가면 다른 방식을 이용하는게 좋다.

<span style="background-color:#FFE6E6; color:dimgray">
※ 필드명 변경시 인터페이스에 정의한 메서드 이름도 변경해주어야 오류가 발생하지 않는다.
</span>

## JPA NamedQuery
* 장점:애플리케이션 실행 시점에 오류를 발견할 수 있다.
* 단점:불편하다. 실무에서 거히 안쓰인다.

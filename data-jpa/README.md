# Spring Data JPA

## JpaRepository
### 메소드 이름으로 쿼리 생성(good)
* https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
* 2개 이상 조건이 넘어 가면 다른 방식을 이용하는게 좋다.

<span style="background-color:#FFE6E6; color:dimgray">
※ 필드명 변경시 인터페이스에 정의한 메서드 이름도 변경해주어야 오류가 발생하지 않는다.
</span>

### JPA NamedQuery(bad)
* 장점:애플리케이션 로딩 시점에 오류를 발견할 수 있다.
* 단점:불편하다. 실무에서 거히 안쓰인다.

### @Query, 리포지토리 메소드에 쿼리 정의(good)
* 메서드명을 간략하게 만들 수 있다.
* 실무에서 자주 쓰인다.
* 편리하다.
* 실행 시점에 애플리케이션 로딩 시점에 오류를 발견할 수 있다.
* new Operator를 사용하여 dto에 바로 값을 넣을 수 있다.

## JPA 페이징
### Page와 Slice
* page는 일반 페이징과 같다.
* page는 1부터가 아닌 0부터 시작이다.
* slice는 앱에서 주로 맨 밑으로 내렸을때 나오는 더보기 기능에 사용된다.
* slice는 limit이 10이면 +1 해서 맨 밑으로 왔을때 +1이 있으면 더보기를 할수있게 하는 로직이다.

### pageable에 대한 정보
```
last : 마지막 페이지인지
totalElements : DB의 전체 데이터 개수
totlaPages : 만들수 있는 page수
size : 페이지당 나타낼수 있는 데이터 개수
number : 현재 페이지 번호
sort : 정렬 정보
first : 첫번쨰 페이지 인지
numberOfElements : 실제 데이터 개수 
empty : 리스트가 비어있는지 여부
hasNext : 다음 페이지 존재 여부
```

<span style="background-color:#FFE6E6; color:dimgray">
total count를 구할때 where문에 조건이 없는 경우라면 countQuery를 이용해서 left join을 제거해야 한다.
</span>

### Entity Paging을 Dto Paging으로 변환
Page.map 기능을 활용해서 dto 생성자에 엔티티 값을 설정하여 Page<DTO>로 반환 한다.


## 벌크성 수정 쿼리
* executeUpdate()는 INSERT, UPDATE, DELETE와 같은 DML(Data Manipulation Language)에서 실행 결과로 영향을 받은 레코드 수를 반환한다.
* spring data jpa에서는 @Modifying을 붙여야 executeUpdate()가 실행된다.
* @Modifying을 변경이 일어나는 쿼리와 함께 사용해야 JPA에서 변경 감지와 관련된 처리를 생략하고 더 효율적인 실행이 가능하다.


## flush와 clear
* flush는 변경 내용을 DB에 반영하는 것이다.
* 🔥flush는 영속 컨텍스트를 비우지않는다.
  * 1차 캐시가 유지된다. 🔥 
  * 쓰기 지연 SQL 저장소의 쿼리문도 지워지지 않는다. 🔥

* clear 메서드는 영속성 컨텍스트의 모든 엔티티를 초기화하는 역할을 한다.
* 영속성 컨텍스트에 캐시된 모든 엔티티를 제거하고, 1차 캐시를 비우는 역할을 수행한다.

```
요약: flush는 앞에있는 쿼리를 반영하고 clear는 영속성 컨텍스트를 비운다.
```

## entity graph
엔티티 그래프를 이용하면 페치조인을 매우 간편하게 할 수 있다.
메소드 명으로 쿼리 생성시 페치 조인까지 하기 위한 방법이다.
attributePaths 또는 NamedEntityGraph를 이용하여 사용하면된다. 


## 사용자 정의 리포지토리 구현
* 순수 JPA 직접 사용
* 스프링 JDBC Template 사용
* MyBatis 사용
* 데이터베이스 커넥션 직접 사용
* Querydsl 사용

```
data jpa에 커스텀한 repository interface를 상속해서 사용할 수 있다.

※ repository custom할때 주의해야할 점은 class 이름을 
JpaRepository를 상속 받은 interface의 이름 + Impl을 붙여야 한다.
ex) MemberRepository, MemberRepositoryImpl 

```

```
항상 사용자 정의 리포지토리가 필요한 것은 아니다. 그냥 임의의 리포지토리를 만들어도 된다.
예를 들어 MemberQueryRepository를 인터페이스가 아닌 클래스로 만들고 스프링 빈으로 등록해서 그냥 직접 사용해도 된다.
물론 이 경우 스프링 데이터 JPA와는 아무런 관계 없이 별도로 동작 한다.

이렇게 repository를 분리해서 사용하는것이 조금더 구조적으로 핵심 비즈니스 로직과 아닌것을 분리하는것이 좋다.
```





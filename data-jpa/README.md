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
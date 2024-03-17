## 엔티티 설계시 주의점

### 엔티티에는 가급적 Setter를 사용하지 말자
setter가 모두 열려있다면 변경포인트가 너무 많아서 유지보수가 어렵다.

### 모든 연관관계는 지연로딩으로 설정!
- 즉시로딩은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때는 N+1 문제가 자주 발생한다.

### 컬렉션은 필드에서 초기화 하자.
컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.
- null 문제에서 안전하다.


```
비즈니스 로직을 대부분 엔티티에서 하는 것을 
*도메인 모델 패턴*이라고 한다.
반대로 엔티티에 비즈니스 로직이 거의 없고 
서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 
*트랜잭션 스크립트 패턴*이라 한다.
```

## 변경 감지와 병함(merge)
```
준영속 엔티티
- 영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.

Book book = new Book();
book.setId(bookForm.getId());
book.setName(bookForm.getName());
book.setPrice(bookForm.getPrice());
book.setStockQuantity(bookForm.getStockQuantity());
book.setAuthor(bookForm.getAuthor());
book.setIsbn(bookForm.getIsbn());
이렇게 식별자가 존재하도록 임의로 만들어낸 엔티티를 준영속 엔티티로 본다.
```

### 준영속 엔티티를 수정하는 2가지 방법
- 변경 감지 기능 사용
- 병합 사용




## 페치조인 주의점
컬렉션은 페치 조인하면 페이징이 불가능 하다.  
1대 N이기 때문에 데이터가 예측할 수 없이 증가하게 된다.  
하지만 이 상태에서 페이징을 하게 되면 N을 기준으로 페이징을 하게되는데
그 결과는 원하는 결과가 아니게 된다.  
distinct를 하여 중복 데이터를 줄이고 결과를 낼 수 도 있다.  
하지만 쿼리 결과를 확인하면 전체 조회를 하게되며
하이버네이트는 경고 로그를 남기고 모든 DB 데이터를 읽어서 메모리에서 페이징을 시도한다.  
out of memory가 발생할 수 있다.

### 해결방법
대부분의 페이킬 + 컬렉션 엔티티 조회 문제는 아래 방법으로 해결이 가능하다.

*ToOne 관계는 모두 fetch join을 한다. *ToOne관계는 row 수를 증가 시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.

해결방법1: `default로 걸어두는게 좋다.`
default_batch_fetch_size 또는 @BatchSize를 조절한다.
* default_batch_fetch_size : 글로벌 설정 100~1000 사이로 조절하는게 가장 좋다.(데이터베이스의 스펙에 의해 갈린다.)
* @BatchSize 개별 설정

batch fetch size를 설정하게 되면 in 쿼리로 변경되게 된다.  
쿼리 호출수가 1+N에서 1+1로 최적화 된다.  
`결론:ToOne관계는 fetch join으로 쿼리수를 줄이고 나머지는 batch fetch size로 최적화 하자`




















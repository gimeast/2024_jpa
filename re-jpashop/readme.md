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
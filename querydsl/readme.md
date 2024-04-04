# querydsl

* 설정
  * ``` build.gradle
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
    annotationProcessor "jakarta.annotation:jakarta.annotation-api"
    annotationProcessor "jakarta.persistence:jakarta.persistence-api"
    ```
* `./gradlew compileJava 로 Q파일 생성`

## 기본 Q-Type 활용
Q 클래스 인스턴스를 사용하는 2가지 방법
```java
QMember qMember = new QMember("m"); //별칭 직접 지정: 같은 테이블을 조인하는 경우가 아니면 굳이 사용x
QMember qMember = QMember.member; //기본 인스턴스 사용
```

```
페이지네이션 개발에 필요한 4가지 값

총 페이지 개수
화면에 보여질 페이지 그룹
화면에 보여질 페이지의 첫번째 페이지 번호
화면에 보여질 페이지의 마지막 페이지 번호
```

---

## 프로젝션
* select 하려는 컬럼이 하나라면 타입을 지정하여 사용할 수 있다.
* 결과가 둘 이상일때는 Tuple을 사용한다.
* Dto로 조회 
  1. new 오퍼레이션
  2. 프로퍼티 접근 - Setter
  3. 필드 직접 접근
  4. 생성자 사용
  5. @QueryProjection
     * 단점: Dto가 QueryDsl에 의존하게된다.
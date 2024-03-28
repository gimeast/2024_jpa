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


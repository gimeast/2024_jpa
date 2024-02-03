package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

    @Id
    private long id;
    @Column(name = "name", insertable = true, updatable = false, nullable = true, length = 10, columnDefinition = "varchar(100) default 'EMPTY'") //insertable: 등록 가능여부, updatable: 변경 가능여부, nullable: 필수여부
    private String username;
    private int age;
    @Enumerated(EnumType.STRING) //enum타입을 쓰려면 @Enumerated를 사용하면 된다.
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP) //날짜 타입을 사용하려면 @Temporal을 사용하면된다. TIMESTAMP:날짜+시간
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    //Date를 사용하는 경우 @Temporal을 지정해야하지만 LocalDate, LocalDateTime인 경우 자동 매핑 된다.
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;
    @Lob //큰 컨텐츠를 넣고싶으면 @Lob를 사용하면 된다. 문자 타입인 경우 clob로 나머지는 blob로 생성된다.
    private String description;
    @Transient //디비와 매핑시키지 않으려면 @Transient를 사용한다.
    private int temp;

    public Member() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}

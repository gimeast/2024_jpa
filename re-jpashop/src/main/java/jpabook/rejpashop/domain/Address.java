package jpabook.rejpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //값 타입은 변경이 불가능한 이뮤터블한 객체여야한다.

    //JPA는 리플렉션이나 프록시같은 기술을 써야하는데 기본생성자가 없으면 정상적인 작동이 되지 않으므로 기본생성자가 필요하다.
    // 하지만 호출하지 못하도록 protected로 스코프를 잡는것이 좋다.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) { //생성할때만 값이 생성되도록
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

package jpabook.rejpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jpabook.rejpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //jpa에서 protected는 쓰지 말라는것을 의미한다. protected를 이용하여 사용하지 못하도록 막는다.
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    //생성 메서드//
    /**
     * @Method         : createOrderItem
     * @Description    : 주문한 아이템 생성
     * @Author         : gimeast
     * @Date           : 2024. 02. 29.
     * @params         : item, orderPrice, count
     * @return         : OrderItem
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }


    //비즈니스 로직//
    /**
     * @Method         : cancel
     * @Description    : 주문취소로 인한 재고 증가 로직
     * @Author         : gimeast
     * @Date           : 2024. 02. 29.
     * @params         :
     * @return         :
     */
    public void cancel() {
        this.item.addStock(count);
    }

    /**
     * @Method         : getTotalPrice
     * @Description    : 전체 주문 가격 조회를 위한 계산 로직
     * @Author         : gimeast
     * @Date           : 2024. 02. 29.
     * @params         :
     * @return         : int
     */
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}

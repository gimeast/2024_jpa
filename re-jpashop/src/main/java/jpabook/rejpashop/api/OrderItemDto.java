package jpabook.rejpashop.api;

import jpabook.rejpashop.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
    private String itemName;
    private int price;
    private int count;

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        price = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
}

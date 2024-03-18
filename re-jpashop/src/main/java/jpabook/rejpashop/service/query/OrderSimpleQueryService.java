package jpabook.rejpashop.service.query;


import jakarta.persistence.EntityManager;
import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderItem;
import jpabook.rejpashop.repository.OrderRepository;
import jpabook.rejpashop.repository.OrderSearch;
import jpabook.rejpashop.repository.order.query.SimpleOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderSimpleQueryService {

    private final OrderRepository orderRepository;

    public List<Order> getOrdersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress(); //LAZY 강제 초기화

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem().getName());
        }

        return all;
    }

    public List<SimpleOrderDto> getSimpleOrderDtosV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    public List<SimpleOrderDto> getSimpleOrderDtosV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

}

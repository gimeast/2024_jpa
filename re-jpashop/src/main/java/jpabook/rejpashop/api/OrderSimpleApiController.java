package jpabook.rejpashop.api;

import jpabook.rejpashop.domain.Address;
import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderStatus;
import jpabook.rejpashop.repository.OrderRepository;
import jpabook.rejpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * @Method         : ordersV1
     * @Description    : 엔티티를 직접 반환 - 절대 하지말것
     * @Author         : gimeast
     * @Date           : 2024. 03. 13.
     * @params         : 
     * @return         : List<Order>
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress(); //LAZY 강제 초기화
        }
        return all;
    }

    /**
     * @Method         : ordersV2
     * @Description    : Dto로 결과값 반환 - N + 1 문제 발생
     * @Author         : gimeast
     * @Date           : 2024. 03. 13.
     * @params         :
     * @return         : List<SimpleOrderDto>
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * @Method         : ordersV3
     * @Description    : fetch join을 이용한 N+1문제 해결
     * @Author         : gimeast
     * @Date           : 2024. 03. 13.
     * @params         :
     * @return         : List<SimpleOrderDto>
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWidthMemberDelivery();
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    private class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}

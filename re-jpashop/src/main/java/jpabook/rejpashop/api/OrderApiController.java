package jpabook.rejpashop.api;

import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderItem;
import jpabook.rejpashop.repository.order.query.OrderFlatDto;
import jpabook.rejpashop.repository.order.query.OrderItemQueryDto;
import jpabook.rejpashop.repository.order.query.OrderQueryDto;
import jpabook.rejpashop.service.OrderService;
import jpabook.rejpashop.service.query.OrderQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        return orderService.getOrdersV1();
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderService.getOrderDtosV2();
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        return orderService.getOrderDtosV3();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit
        ) {
        return orderService.getOrderDtos(offset, limit);
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryService.getOrderQueryDtosV1();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryService.getOrderQueryDtosV2();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        return orderQueryService.getOrderQueryDtosV3();
    }



}

package jpabook.rejpashop.api;

import jpabook.rejpashop.repository.order.query.SimpleOrderDto;
import jpabook.rejpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.repository.OrderRepository;
import jpabook.rejpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import jpabook.rejpashop.service.query.OrderSimpleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderSimpleQueryService orderSimpleQueryService;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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
        return orderSimpleQueryService.getOrdersV1();
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
        return orderSimpleQueryService.getSimpleOrderDtosV2();
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
        return orderSimpleQueryService.getSimpleOrderDtosV3();
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }


}

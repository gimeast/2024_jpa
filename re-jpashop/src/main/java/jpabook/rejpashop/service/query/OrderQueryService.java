package jpabook.rejpashop.service.query;

import jpabook.rejpashop.repository.order.query.OrderFlatDto;
import jpabook.rejpashop.repository.order.query.OrderItemQueryDto;
import jpabook.rejpashop.repository.order.query.OrderQueryDto;
import jpabook.rejpashop.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;


    public List<OrderQueryDto> getOrderQueryDtosV1() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos();
        return orderQueryDtos;
    }

    public List<OrderQueryDto> getOrderQueryDtosV2() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findAllByDto_optimization();
        return orderQueryDtos;
    }

    public List<OrderQueryDto> getOrderQueryDtosV3() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        //쿼리는 한번에 끝낼 수 있다.
        //쿼리에서는 중복 처리가 되지 않고 애플리케이션 단에서 분해 조립을 하는 과정을 거치므로 v5보다 느릴수있다.
        //페이징이 불가능 하다.
        return flats.stream()
                .collect(
                        Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                                Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())
                        )
                )
                .entrySet().stream()
                .map(e ->
                        new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue())
                )
                .toList();
    }


}

package jpabook.rejpashop;

import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderStatus;
import jpabook.rejpashop.repository.OrderRepository;
import jpabook.rejpashop.repository.OrderSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("검색 조회")
    void 검색조회() {
        //given
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("홍길동");

        //when
        List<Order> searchList = orderRepository.findAllByCriteria(orderSearch);

        //then
        System.out.println("searchList = " + searchList);

    }

}

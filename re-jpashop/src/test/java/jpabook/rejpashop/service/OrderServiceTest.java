package jpabook.rejpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.rejpashop.domain.Address;
import jpabook.rejpashop.domain.Member;
import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderStatus;
import jpabook.rejpashop.domain.item.Book;
import jpabook.rejpashop.domain.item.Item;
import jpabook.rejpashop.exception.NotEnoughStockException;
import jpabook.rejpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemService itemService;
    

    @Test
    @DisplayName("상품주문")
    void 상품주문() {
        //given
        Member member = createMember("홍길동", new Address("서울시", "밤고개로", "123456"));

        Book book = createBook(30000, "정의란 무엇인가", 10);

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), 2);

        //then
        Order order = orderRepository.findOne(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER); //상품 주문상태
        assertThat(order.getOrderItems().size()).isEqualTo(1); //주문한 상품 종류 = 책 -> 1 종류
        assertThat(order.getTotalPrice()).isEqualTo(30000 * 2);
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("상품주문 재고수량 초과")
    void 재고수량_초과() {
        //given
        Member member = createMember("홍길동", new Address("서울시", "밤고개로", "123456"));
        Book book = createBook(30000, "정의란 무엇인가", 10);

        //when
        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class, () ->
            orderService.order(member.getId(), book.getId(), orderCount));

    }

    @Test
    @DisplayName("주문 취소")
    void 주문_취소() {
        //given
        Member member = createMember("홍길동", new Address("서울시", "밤고개로", "123456"));
        Book book = createBook(30000, "정의란 무엇인가", 10);

        int orderCount = 4;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Item item = itemService.findOne(book.getId());
        assertThat(item.getStockQuantity()).isEqualTo(10);

        Order order = orderRepository.findOne(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);

    }

    private Book createBook(int price, String name, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);

        em.persist(book);
        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);
        return member;
    }
}
package jpabook.rejpashop.service;

import jpabook.rejpashop.domain.Address;
import jpabook.rejpashop.domain.Delivery;
import jpabook.rejpashop.domain.DeliveryStatus;
import jpabook.rejpashop.domain.Member;
import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderItem;
import jpabook.rejpashop.domain.item.Item;
import jpabook.rejpashop.repository.ItemRepository;
import jpabook.rejpashop.repository.MemberRepository;
import jpabook.rejpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    /**
     * @Method         : order
     * @Description    : 주문
     * @Author         : gimeast
     * @Date           : 2024. 03. 02.
     * @params         : memberId, itemId, count
     * @return         : 키값
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member findMember = memberRepository.findOne(memberId);
        Item findItem = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress()); //회원의 주소를 배달 주소로 한다. (운영에서는 주소지를 입력 받아야 한다.)

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(findMember, delivery, orderItem);

        //저장
        orderRepository.save(order); //cascade를 사용하여 delivery와 orderItem은 order를 저장할때 같이 저장된다.
        //cascade 사용시 주의점은 delivery 또는 orderItem을 다른곳에서 참조해서 사용한다면 함부로 사용하면 안된다.. (기본 코드로 작성하고 나중에 리팩토링시 적용시키는게 좋다.)

        return order.getId();
    }

    /**
     * @Method         : cancelOrder
     * @Description    : 주문 취소
     * @Author         : gimeast
     * @Date           : 2024. 03. 02.
     * @params         : orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //검색

}

package jpabook.rejpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.rejpashop.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager em;

    public Order find(Long id) {
        return em.find(Order.class, id);
    }

}

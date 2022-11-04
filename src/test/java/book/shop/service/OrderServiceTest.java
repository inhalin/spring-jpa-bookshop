package book.shop.service;

import book.shop.domain.Address;
import book.shop.domain.Member;
import book.shop.domain.Order;
import book.shop.domain.OrderStatus;
import book.shop.domain.item.Book;
import book.shop.domain.item.Item;
import book.shop.exception.NotEnoughStockException;
import book.shop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext EntityManager em;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderService orderService;

    @Test
    void 상품주문() {
        int price = 1000;
        int stockQuantity = 10;
        int orderCount = 2;

        Member member = createMember();
        Item item = createBook(price, stockQuantity);

        // 주문하기
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // 주문 내역
        Order order = orderRepository.findOne(orderId);

        assertThat(order.getStatus()).as("주문시 상태는 ORDER")
                .isEqualTo(OrderStatus.ORDER);
        assertThat(order.getOrderItems().size()).as("상품 종류는 1개")
                .isEqualTo(1);
        assertThat(order.getTotalPrice()).as("주문 가격 = 상품가격 * 주문수량")
                .isEqualTo(price * orderCount);
        assertThat(item.getStockQuantity()).as("상품재고는 주문수량만큼 감소")
                .isEqualTo(stockQuantity - orderCount);
    }

    @Test
    void 주문취소() {
        int stockQuantity = 10;
        int orderCount = 2;

        Member member = createMember();
        Item item = createBook(1000, stockQuantity);

        // 주문하기
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // 주문 내역
        Order order = orderRepository.findOne(orderId);

        assertThat(order.getStatus()).as("주문시 상태는 ORDER")
                .isEqualTo(OrderStatus.ORDER);
        assertThat(item.getStockQuantity()).as("상품재고는 주문수량만큼 감소")
                .isEqualTo(stockQuantity - orderCount);

        // 주문 취소
        orderService.cancelOrder(orderId);

        assertThat(order.getStatus()).as("취소된 주문의 상태는 CANCEL")
                .isEqualTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity()).as("취소된 주문은 상품 수량이 도로 올라간다.")
                .isEqualTo(stockQuantity);
    }

    @Test
    void 상품주문_재고수량초과() {
        int stockQuantity = 10;
        int orderCount = 11;

        Member member = createMember();
        Item item = createBook(1000, stockQuantity);

        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("경기", "백양로", "1234"));
        em.persist(member);

        return member;
    }

    private Book createBook(int price, int stockQuantity) {
        Book book = new Book();
        book.setName("order test");
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }
}
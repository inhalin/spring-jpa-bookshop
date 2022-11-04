package book.shop.domain.item;

import book.shop.exception.NotEnoughStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item item;
    int stockQuantity = 10;

    @BeforeEach
    void before() {
        item = new Book();
        item.setStockQuantity(stockQuantity);
    }

    @Test
    void 재고수량_증가() {
        item.addStock(2);

        assertThat(item.getStockQuantity()).isEqualTo(12);
    }

    @Test
    void 재고수량_감소() {
        item.removeStock(2);

        assertThat(item.getStockQuantity()).isEqualTo(8);
    }

    @Test
    void 재고수량_부족() {
        assertThatThrownBy(() ->
                item.removeStock(11)
        ).isInstanceOf(NotEnoughStockException.class);
    }
}
package book.shop.service;

import book.shop.domain.item.Book;
import book.shop.domain.item.Item;
import book.shop.domain.item.Movie;
import book.shop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ItemServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Test
    void 상품저장() {
        Item book = createBook("Spring JPA", 30000, 50);

        itemService.saveItem(book);

        assertThat(book).isEqualTo(itemRepository.findOne(book.getId()));
    }

    @Test
    void 상품조회() {
        Item book = createBook("Hello World", 1000, 1);
        Item movie = createMovie("Harry Marry", 12000, 10);

        itemService.saveItem(book);
        itemService.saveItem(movie);

        List<Item> items = itemService.findItems();

        assertThat(items.size()).isEqualTo(2);
        assertThat(items).contains(book, movie);
    }

    private Book createBook(String title, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(title);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }

    private Movie createMovie(String title, int price, int stockQuantity) {
        Movie movie = new Movie();
        movie.setName(title);
        movie.setPrice(price);
        movie.setStockQuantity(stockQuantity);
        em.persist(movie);

        return movie;
    }
}
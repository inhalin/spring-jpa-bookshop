package book.shop.controller;

import book.shop.domain.form.BookForm;
import book.shop.domain.item.Book;
import book.shop.domain.item.Item;
import book.shop.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static book.shop.PageUtils.setTitle;

@Slf4j
@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
        createBook("Book 1", 10000, 30, "Kim", "1234");
        createBook("Book 2", 15000, 40, "Lee", "4321");
        createBook("Book 3", 20000, 50, "Park", "5678");
    }

    private void createBook(String name, int price, int stockQuantity, String author, String isbn) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);
        itemService.saveItem(book);
    }

    @GetMapping
    public String index(Model model) {
        setTitle(model, "상품 목록");
        model.addAttribute("items", itemService.findItems());
        return "items/index";
    }

    @GetMapping("/new")
    public String create(Model model) {
        setTitle(model, "상품 등록");
        model.addAttribute("form", new BookForm());
        return "items/create";
    }

    @PostMapping("/new")
    public String store(@Valid BookForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "items/create";
        }

        createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setTitle(model, "상품 수정");
        Book item = (Book) itemService.findOne(id);
        BookForm form = mapToForm(item);
        model.addAttribute("form", form);

        return "items/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, BookForm form) {
        System.out.println(form);
        itemService.updateItem(id, form.getName(), form.getPrice());
        return "redirect:/items";
    }

    private BookForm mapToForm(Book item) {
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        return form;
    }
}

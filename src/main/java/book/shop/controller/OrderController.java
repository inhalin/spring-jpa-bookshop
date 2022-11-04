package book.shop.controller;

import book.shop.repository.OrderSearch;
import book.shop.service.ItemService;
import book.shop.service.MemberService;
import book.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static book.shop.PageUtils.setTitle;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/orders")
    public String index(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        setTitle(model, "주문 내역");
        model.addAttribute("orders", orderService.findOrders(orderSearch));
        return "orders/index";
    }

    @GetMapping("/order")
    public String create(Model model) {
        setTitle(model, "상품 주문");
        model.addAttribute("members", memberService.findMembers());
        model.addAttribute("items", itemService.findItems());

        return "orders/create";
    }

    @PostMapping("/order")
    public String store(@RequestParam Long memberId, @RequestParam Long itemId, @RequestParam int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
}

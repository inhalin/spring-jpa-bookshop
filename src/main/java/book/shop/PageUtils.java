package book.shop;

import org.springframework.ui.Model;

public class PageUtils {

    public static void setTitle(Model model, String title) {
        model.addAttribute("title", title);
    }
}

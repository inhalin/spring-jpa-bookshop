package book.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    // Logger log = LoggerFactory.getLogger(getClass()); // 롬복 애노테이션으로 대체 가능

    @RequestMapping("/")
    public String home() {
        log.debug("HomeController.home");
        return "home";
    }
}

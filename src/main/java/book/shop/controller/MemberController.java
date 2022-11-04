package book.shop.controller;

import book.shop.domain.Address;
import book.shop.domain.Member;
import book.shop.domain.form.MemberForm;
import book.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

import static book.shop.PageUtils.setTitle;

@Slf4j
@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        createMember("John", new Address("서울", "호현로", "1234"));
        createMember("Marry", new Address("서울", "광화문대로", "4321"));
    }

    private void createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        memberService.join(member);
    }

    @GetMapping
    public String index(Model model) {
        setTitle(model, "회원 목록");
        model.addAttribute("members", memberService.findMembers());
        return "members/index";
    }

    @GetMapping("/new")
    public String create(Model model) {
        setTitle(model, "회원 가입");
        model.addAttribute("form", new MemberForm());
        return "members/create";
    }

    @PostMapping("/new")
    public String store(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/create";
        }

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(new Address(form.getCity(), form.getStreet(), form.getZipcode()));

        memberService.join(member);

        return "redirect:/";
    }
}

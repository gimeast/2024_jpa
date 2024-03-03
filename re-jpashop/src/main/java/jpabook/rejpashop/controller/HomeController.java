package jpabook.rejpashop.controller;

import jpabook.rejpashop.service.ItemService;
import jpabook.rejpashop.service.MemberService;
import jpabook.rejpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final ItemService itemService;
    
    @GetMapping("/")
    public String home() {
        log.info("home 진입");
        return "home";
    }

}

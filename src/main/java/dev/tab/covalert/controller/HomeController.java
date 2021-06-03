package dev.tab.covalert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller(value = "/")
public class HomeController {
    
    public HomeController() {

    }

    @GetMapping
    public String showHomePage() {
        return "home";
    }

}

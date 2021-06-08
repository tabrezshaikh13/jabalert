package dev.tab.covalert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import dev.tab.covalert.service.UnsubscribeService;

@Controller
public class UnsubscribeController {

    private UnsubscribeService unsubscribeService;

    public UnsubscribeController(UnsubscribeService unsubscribeService) {
        this.unsubscribeService = unsubscribeService;
    }

    @ModelAttribute(value = "unsubscribeDTO")
    public UnsubscribeDTO getUnsubscribeDTO() {
        return new UnsubscribeDTO();
    }

    @GetMapping(value = "/unsubscribe")
    public String showUnsubscribePage(Model model) {
        model.addAttribute("unsubscribeDTO", getUnsubscribeDTO());
        return "unsubscribe";
    }

    @PostMapping(value = "/unsubscribe")
    public String processSubscription(@ModelAttribute(value = "unsubscribeDTO") UnsubscribeDTO dto) {
        if(unsubscribeService.unsubscribeClient(dto.getEmail())) {
            return "redirect:/unsubscribe?unsubscribeSuccess";
        } else {
            return "redirect:/unsubscribe?emailNotExists";
        }
    }
    
}

package dev.tab.covalert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import dev.tab.covalert.service.ClientRegistrationService;


@Controller(value = "/")
public class HomeController {

    private ClientRegistrationService clientRegistrationService;
    
    public HomeController(ClientRegistrationService clientRegistrationService) {
        this.clientRegistrationService = clientRegistrationService;
    }

    @ModelAttribute(name = "clientDTO")
    public ClientDTO getClientDTO() {
        return new ClientDTO();
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("clientDTO", getClientDTO());
        model.addAttribute("totalRegistrations", clientRegistrationService.getTotalRegistrations());
        return "home";
    }
    
    @PostMapping
    public String registerClient(@ModelAttribute(name = "clientDTO") ClientDTO client) {
        if(String.valueOf(client.getPincode()).length() == 6 && String.valueOf(client.getPincode()).charAt(0) != '0') {
            if(clientRegistrationService.checkIfEmailExists(client.getEmail())) {
                return "redirect:/?userAlreadyExists";
            } else {
                clientRegistrationService.registerClient(client);
                return "redirect:/?registrationSuccess";
            }
        } else {
            return "redirect:/?pincodeError";
        }
        
    }

}

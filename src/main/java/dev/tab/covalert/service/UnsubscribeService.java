package dev.tab.covalert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import dev.tab.covalert.model.Client;
import dev.tab.covalert.repository.ClientRepository;

@Service
public class UnsubscribeService {
    
    @Autowired
    private EmailService mailService;
    @Autowired
    private ClientRepository clientRepo;

    public boolean unsubscribeClient(String email) {
        boolean clientExists = false;
        Client client = clientRepo.findByEmail(email);
        if(client != null) {
            clientExists = true;
            unsubscribeNotification(email);
            clientRepo.deleteByEmail(email);
        }
        return clientExists;
    }

    @Async
    public void unsubscribeNotification(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jabalert.com@gmail.com");
        mailMessage.setSubject("Unsubscribe Alert");
        mailMessage.setText(email + " just unsubscribed from JabAlert.");
        mailMessage.setTo("jabalert.com@gmail.com");
        mailService.sendNotification(mailMessage);
    }

}

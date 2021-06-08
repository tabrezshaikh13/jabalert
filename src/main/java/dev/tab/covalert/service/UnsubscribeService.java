package dev.tab.covalert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tab.covalert.model.Client;
import dev.tab.covalert.repository.ClientRepository;

@Service
public class UnsubscribeService {
    
    @Autowired
    private ClientRepository clientRepo;

    public boolean unsubscribeClient(String email) {
        boolean clientExists = false;
        Client client = clientRepo.findByEmail(email);
        if(client != null) {
            clientExists = true;
            clientRepo.deleteByEmail(email);
        }
        return clientExists;
    }

}

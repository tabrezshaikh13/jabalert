package dev.tab.covalert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tab.covalert.controller.ClientDTO;
import dev.tab.covalert.model.Client;
import dev.tab.covalert.repository.ClientRepository;

@Service
public class ClientRegistrationService {

    @Autowired
    private ClientRepository clientRepo;

    public ClientRegistrationService() {

    }

    public void registerClient(ClientDTO clientDto) {
        Client client = new Client(clientDto.getEmail(), clientDto.getPincode());
        clientRepo.save(client);
    }
    
}
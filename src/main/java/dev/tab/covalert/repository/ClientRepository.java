package dev.tab.covalert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dev.tab.covalert.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Client findByEmail(String email);
    
}

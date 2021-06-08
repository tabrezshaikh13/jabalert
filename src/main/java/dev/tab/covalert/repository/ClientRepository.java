package dev.tab.covalert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import dev.tab.covalert.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Client findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM covalert.clients c WHERE c.client_email = :email", nativeQuery = true)
    void deleteByEmail(@Param("email") String email);
    
}

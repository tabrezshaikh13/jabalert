package dev.tab.covalert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "clients", schema = "covalert", uniqueConstraints = @UniqueConstraint(columnNames = {"client_email"}))
public class Client {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;

    
    @Column(name = "client_email", nullable = false, unique = true, table = "clients")
    private String email;

    @Column(name = "client_pincode", nullable = false, table = "clients")
    private int pincode;

    public Client() {

    }

    public Client(String email, int pincode) {
        this.email = email;
        this.pincode = pincode;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
    
}

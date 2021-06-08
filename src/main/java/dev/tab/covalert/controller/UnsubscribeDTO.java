package dev.tab.covalert.controller;

public class UnsubscribeDTO {

    private String email;

    public UnsubscribeDTO() {

    }

    public UnsubscribeDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}

package dev.tab.covalert.controller;

public class ClientDTO {

    private String email;
    private int pincode;

    public ClientDTO() {

    }

    public ClientDTO(String email, int pincode) {
        this.email = email;
        this.pincode = pincode;
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

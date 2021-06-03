package dev.tab.covalert.model;


public class Client {

    private String email;
    private int pincode;

    public Client() {

    }

    public Client(String email, int pincode) {
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

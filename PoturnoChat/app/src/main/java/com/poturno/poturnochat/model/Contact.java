package com.poturno.poturnochat.model;

/**
 * Created by vitor on 14/09/2017.
 */

public class Contact {

    private String userIdentifier;
    private String name;
    private String email;

    public Contact() {
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

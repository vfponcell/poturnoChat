package com.poturno.poturnochat.model;

/**
 * Created by vitor on 24/09/2017.
 */

public class Chat {

    private String userId;
    private String name;
    private String mensageValue;

    public Chat() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMensageValue() {
        return mensageValue;
    }

    public void setMensageValue(String mensageValue) {
        this.mensageValue = mensageValue;
    }
}

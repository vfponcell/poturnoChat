package com.poturno.poturnochat.model;

/**
 * Created by vitor on 21/09/2017.
 */

public class Mensage {

    private String UserId;
    private String mensage;

    public Mensage() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }
}

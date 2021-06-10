package com.example.contactlist.exception;

import java.util.ArrayList;
import java.util.List;

public class BadResourceException extends Exception {

    private List<String> errorMessage = new ArrayList<>();

    public BadResourceException() {

    }

    public BadResourceException(String msg) {
        super(msg);
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addErrorMessage(String message) {
        this.errorMessage.add(message);
    }
}

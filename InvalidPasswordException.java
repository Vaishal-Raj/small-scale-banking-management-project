package com.password_checker;

public class InvalidPasswordException extends Exception {
    public String message;

    public InvalidPasswordException(String message) {
        this.message = message;
    }
}
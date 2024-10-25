package com.elvo.service.impl;

public class UserServiceImpl {

    // Method to validate the password based on the given rules
    public static int validatePassword(String password) {
        // Check if the password has at least 8 characters
        if (password.length() >= 8) {
            return 1;
        }

        // Check if the password contains at least one uppercase letter
        if (password.matches(".*[A-Z].*")) {
            return 3;
        }

        // Check if the password contains at least one symbol (non-alphanumeric character)
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return 2;
        }

        return 0;
    }


}

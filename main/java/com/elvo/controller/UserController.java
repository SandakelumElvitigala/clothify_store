package com.elvo.controller;

import com.elvo.entity.User;
import com.elvo.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {

    public static void addUser(String name, String email, String password, String role){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole(role);
        UserRepository.createUser(user);
    }

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

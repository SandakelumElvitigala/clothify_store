package com.elvo.service;

import lombok.Getter;


public class Session {
    @Getter
    private static String type;
    @Getter
    private static String email;

    public static void setType(String type) {
        Session.type = type;
    }
    public static void setEmail(String email) { Session.email = email; }


}

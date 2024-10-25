package com.elvo.service;

import com.elvo.controller.ui.UIManager;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public class LoginService {

    public static String generateOTP(){
        String numbers = "0123456789";
        Random rnd;
        rnd = new Random();
        char[] OTP = new char[6];

        for (int i=0; i<6; i++){
            OTP[i]=numbers.charAt(rnd.nextInt(numbers.length()));
        }

        System.out.println( OTP);
        return String.valueOf(OTP);
    }

    private static final int OTPseconds = 90;
    private static Timer timer;

    public static void startCountdown(TextField countdownLabel, AnchorPane ancOTP) {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            int countdown = OTPseconds;  // Start countdown from 30 seconds

            @Override
            public void run() {
                if (countdown > 0) {
                    // Convert countdown to minutes and seconds
                    int minutes = countdown / 60;
                    int seconds = countdown % 60;

                    // Update the label on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        countdownLabel.setPromptText(String.format("Time left: %02d:%02d", minutes, seconds));
                    });

                    countdown--;
                } else {
                    Platform.runLater(() -> {
                        countdownLabel.setPromptText("OTP has expired!");
                        UIManager.closeOverlays(ancOTP);
                    });
                    timer.cancel();  // Stop the timer after OTP expires
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000); // Schedule task to run every 1 second
    }

}


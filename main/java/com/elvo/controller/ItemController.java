package com.elvo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemController {

    public static String convertImageToBase64(File imageFile) throws IOException {
        // Read the image file into a byte array
        try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
            byte[] imageBytes = new byte[(int) imageFile.length()];
            fileInputStream.read(imageBytes);

            // Encode the byte array to Base64
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }



}

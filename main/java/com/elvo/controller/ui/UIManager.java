package com.elvo.controller.ui;

import com.elvo.controller.inventory.CollectionFormController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class UIManager {

    public static void openOverlays(AnchorPane pane){
        pane.setVisible(true);
    }

    public static void closeOverlays(AnchorPane pane){
        pane.setVisible(false);
    }

    public static void showDialogueBox(String title, String errorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(errorMsg);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close(); // Close the overlay when OK is clicked
        }
    }

    public static String getCurrentTime() {
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return currentTime.format(formatter);
    }

    // Reusable method to start updating any label in real-time
    public static void startRealTimeClock(Label label) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            // Update the label with the current time
            label.setText(getCurrentTime());
        }),
                new KeyFrame(Duration.seconds(1)) // Update every 1 second
        );
        clock.setCycleCount(Timeline.INDEFINITE); // Make it run indefinitely
        clock.play(); // Start the timeline
    }

    public static void openScenes(String fxmlFile) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Set up a new stage (window) and scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Show the new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

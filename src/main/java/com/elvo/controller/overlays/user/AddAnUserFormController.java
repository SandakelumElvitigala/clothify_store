package com.elvo.controller.overlays;

import com.elvo.controller.UserController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class AddAnUserFormController {

    String password;

    @FXML
    private JFXButton btnAddAsAnAdmin;

    @FXML
    private JFXButton btnAddAsAnEmployee;

    @FXML
    private Label lblEightCharacters;

    @FXML
    private Label lblIsPasswordMatching;

    @FXML
    private Label lblSymbol;

    @FXML
    private Label lblText;

    @FXML
    private Label lblUpperCase;

    @FXML
    private JFXTextField txtAddEmail;

    @FXML
    private JFXPasswordField txtAddPassword;

    @FXML
    private JFXTextField txtAddUserName;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    public void initialize() {
        // Set up key event listener on the password field to validate as the user types
        txtAddPassword.setOnKeyReleased(event -> {
            password = txtAddPassword.getText();
            int x = UserController.validatePassword(password);

            if (x==1){
                lblEightCharacters.setTextFill(Color.WHITE);
            } else if (x==3) {
                lblUpperCase.setTextFill(Color.WHITE);
            } else if (x==2) {
                lblSymbol.setTextFill(Color.WHITE);
            }
        });
        txtConfirmPassword.setOnKeyReleased(event -> {
            String password1 = txtConfirmPassword.getText();
            if ((password1.equals(password))) {
                lblIsPasswordMatching.setText("Passwords matching");
                lblIsPasswordMatching.textFillProperty().set(Color.GREEN);
                btnAddAsAnAdmin.setVisible(true);
                btnAddAsAnEmployee.setVisible(true);
            } else {
                lblIsPasswordMatching.setText("Passwords mis-matching");
                lblIsPasswordMatching.textFillProperty().set(Color.RED);
            }
        });
    }

    @FXML
    void btnAddAsAnAdminOnAction(ActionEvent event) {
        UserController.addUser(txtAddUserName.getText(), txtAddEmail.getText(), password, "admin");
    }

    @FXML
    void btnAddAsAnEmployeeOnAction(ActionEvent event) {
        UserController.addUser(txtAddUserName.getText(), txtAddEmail.getText(), password, "user");
    }

}

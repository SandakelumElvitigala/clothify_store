package com.elvo.controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.event.MouseEvent;

public class LoginFormController {

    @FXML
    private JFXButton btnForgotPassword;

    @FXML
    private Label lblEightCharacters;

    @FXML
    private Label lblOneSymbol;

    @FXML
    private Label lblOneUppercase;

    @FXML
    private Label lblOtpTimer;

    @FXML
    private Pane pnlCreatePassword;

    @FXML
    private Pane pnlEnterOtp;

    @FXML
    private PasswordField txtConfirmNewPassword;

    @FXML
    private JFXTextField txtEmailLogin;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private TextField txtOtp;

    @FXML
    private JFXPasswordField txtPasswordLogin;

    @FXML
    void btnCreateNewPasswordOnAction(MouseEvent event) {

    }

    @FXML
    void btnExitOnAction(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void btnExitFromCreateNewPasswordOnAction(MouseEvent event) {

    }

    @FXML
    void btnLoginOnAction(MouseEvent event) {

    }

    @FXML
    void btnOtpCloseOnAction(MouseEvent event) {

    }

    @FXML
    void btnOtpProceedOnAction(MouseEvent event) {

    }

}

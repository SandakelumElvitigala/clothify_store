package com.elvo.controller.login;
import com.elvo.controller.ui.UIManager;
import com.elvo.repository.UserRepository;
import com.elvo.service.LoginService;
import com.elvo.service.Session;
import com.elvo.service.impl.SendEmailImpl;
import com.elvo.service.impl.UserServiceImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

@Getter
@Setter

public class LoginFormController {

    String sentOTP;
    String password;

    @FXML
    private JFXButton btnCreateNewPassword;

    @FXML
    private Label lblPasswordMatching;

    @FXML
    private Label lblPasswordShouldContain;

    @FXML
    private Label lblEightCharacters;

    @FXML
    private Label lblOneSymbol;

    @FXML
    private Label lblOneUppercase;

    @FXML
    private Label lblOtpTimer;

    @FXML
    private AnchorPane pnlCreatePassword;

    @FXML
    private AnchorPane pnlEnterOtp;

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
    public void initialize() {
        // Set up key event listener on the password field to validate as the user types
        txtNewPassword.setOnKeyReleased(event -> {
            password = txtNewPassword.getText();
            int x = UserServiceImpl.validatePassword(password);

            if (x==1){
                lblEightCharacters.setTextFill(Color.WHITE);
            } else if (x==3) {
                lblOneUppercase.setTextFill(Color.WHITE);
            } else if (x==2) {
                lblOneSymbol.setTextFill(Color.WHITE);
            }
            lblPasswordShouldContain.setTextFill(Color.WHITE);
        });
        txtConfirmNewPassword.setOnKeyReleased(event -> {
            String password1 = txtConfirmNewPassword.getText();
            if ((password1.equals(password))) {
                lblPasswordMatching.setText("Password is matching");
                btnCreateNewPassword.setDisable(false);
            } else {
                lblPasswordMatching.setText("Passwords mis-matching");
            }
        });
    }

    // otp
    public void btnForgotPassword() {
        if (txtEmailLogin.getText() != null && !txtEmailLogin.getText().isEmpty()) {
            sentOTP = LoginService.generateOTP();  // Generate OTP
            SendEmailImpl.sendMail(txtEmailLogin.getText(), "Testing", sentOTP);  // Send email with OTP
            UIManager.openOverlays( pnlEnterOtp);  // Open overlay for OTP
            LoginService.startCountdown(txtOtp, pnlEnterOtp);  // Start OTP countdown
        } else {
            UIManager.showDialogueBox("Error","Please enter a valid Email");
            UIManager.closeOverlays(pnlEnterOtp);
        }
    }

    public void btnOtpProceed() {
        if (txtOtp.getText().equals(sentOTP)){
            UIManager.closeOverlays(pnlEnterOtp);
            UIManager.openOverlays(pnlCreatePassword);
        }
        else{
            txtOtp.setPromptText("Please enter a Valid OTP");
            UIManager.showDialogueBox("Invalid OTP","Please enter a valid OTP");
        }
    }

    public void btnOtpClose() {
        UIManager.closeOverlays(pnlEnterOtp);
    }

    // create password
    public void btnCreateNewPassword() {
        if ((UserRepository.updatePassword(txtEmailLogin.getText(), password))) {
            UIManager.closeOverlays(pnlCreatePassword);
        }
    }

    public void btnExitFromCreateNewPassword() {
        UIManager.closeOverlays(pnlCreatePassword);
    }

    // login
    public void btnLogin() throws IOException {
        Parent root;
        if (UserRepository.isUserValid(txtEmailLogin.getText(), txtPasswordLogin.getText())){

            // set values to session ( role, email )
            Session.setType(UserRepository.getUserByEmail(txtEmailLogin.getText()).getRole());
            Session.setEmail(txtEmailLogin.getText());


            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/dash_form_admin.fxml")));

            Stage currentStage = (Stage) txtPasswordLogin.getScene().getWindow();
            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);

            currentStage.setTitle("Dashboard");

            currentStage.show();
        }else{
            UIManager.showDialogueBox("Error","Unable to login. Please double check your login data");
        }


    }

    // exit
    public void btnExit() {
        Platform.exit();
    }
}

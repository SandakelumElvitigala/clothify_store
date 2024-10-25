package com.elvo.controller.overlays.user;

import com.elvo.controller.ui.UIManager;
import com.elvo.entity.User;
import com.elvo.repository.UserRepository;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DeleteUserFormController {

    private User user = new User();

    @FXML
    private AnchorPane ancResults;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblId;

    @FXML
    private Label lblName;

    @FXML
    private Label lblRole;

    @FXML
    private JFXTextField txtIndexToSearch;

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        user = UserRepository.getUserByIndex(txtIndexToSearch.getText());
        if (user != null){
            ancResults.setVisible(true);
            lblId.setText(String.valueOf(user.getId()));
            lblName.setText(user.getName());
            lblEmail.setText(user.getEmail());
            lblRole.setText(user.getRole());
        }else {
            UIManager.showDialogueBox("Error","User not found");
            txtIndexToSearch.clear();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        UserRepository.deleteUser(user);
    }



}

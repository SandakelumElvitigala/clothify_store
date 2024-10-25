package com.elvo.controller.overlays.supplier;

import com.elvo.repository.SupplierRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AddSuppliersFormController {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtName;


    @FXML
    public void btnAddOnAction(ActionEvent event) {
        if (!txtName.getText().isEmpty() && !txtCompany.getText().isEmpty() && !txtEmail.getText().isEmpty()){
            SupplierRepository.addSupplier(txtName.getText(), txtCompany.getText(), txtEmail.getText());
        }
    }

}

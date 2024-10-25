package com.elvo.controller.overlays.supplier;

import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Supplier;
import com.elvo.repository.SupplierRepository;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DeleteSupplierFormController {

    private Supplier supplier = new Supplier();

    @FXML
    private AnchorPane ancDelete;

    @FXML
    private Label lblCompany;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblId;

    @FXML
    private Label lblName;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        SupplierRepository.deleteSupplier(supplier.getId());
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        supplier = SupplierRepository.searchSupplier(txtSearch.getText());
        if (supplier != null){
            ancDelete.setVisible(true);
            lblId.setText(String.valueOf(supplier.getId()));
            lblName.setText(supplier.getName());
            lblCompany.setText(supplier.getCompany());
            lblEmail.setText(supplier.getEmail());
        }else {
            UIManager.showDialogueBox("Error","Supplier not found");
            txtSearch.clear();
        }
    }

}

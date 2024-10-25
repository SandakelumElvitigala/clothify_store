package com.elvo.controller.overlays.supplier;

import com.elvo.entity.Supplier;
import com.elvo.repository.SupplierRepository;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditSupplierFormController {

    private Supplier supplier = new Supplier();

    @FXML
    private AnchorPane ancEdit;

    @FXML
    private Label lblId;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtSearch;

    public void setSearchText(String id) {
        txtSearch.setText(id);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        supplier = SupplierRepository.searchSupplier(txtSearch.getText());
        if (supplier != null){
            ancEdit.setVisible(true);
            lblId.setText(String.valueOf(supplier.getId()));
            txtName.setText(supplier.getName());
            txtCompany.setText(supplier.getCompany());
            txtEmail.setText(supplier.getEmail());
        }
    }

    public void btnUpdateOnAction() {
        SupplierRepository.updateSupplier(supplier.getId(), txtName.getText(), txtCompany.getText(), txtEmail.getText());
    }

}

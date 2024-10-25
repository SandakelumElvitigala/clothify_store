package com.elvo.controller.overlays.supplier;

import com.elvo.controller.overlays.item.EditItemFormController;
import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Supplier;
import com.elvo.repository.SupplierRepository;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SupplierListFormController {

    private List<Supplier> suppliers;
    private SupplierRepository supplierRepository;

    @FXML
    private TableView<Supplier> supplierTable; // Declare your TableView here

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private TableColumn<Supplier, Long> colId;

    @FXML
    private TableColumn<Supplier, String> colName;

    @FXML
    private TableColumn<Supplier, String> colCompany;

    @FXML
    private TableColumn<Supplier, String> colEmail;

    private Long id;

    @FXML
    private TableColumn<Supplier, String> colReg;

    public SupplierListFormController() {
        this.supplierRepository = new SupplierRepository();
    }

    @FXML
    public void initialize() {
        loadSuppliers();
        setupTableViewClickListener();
    }

    private void loadSuppliers() {
        suppliers = supplierRepository.getAllSuppliers(); // Fetch the supplier data
        ObservableList<Supplier> observableSuppliers = FXCollections.observableArrayList(suppliers);
        supplierTable.setItems(observableSuppliers); // Set the items for the TableView

        // Optionally, set the cell value factories for the columns if using custom objects
        colId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCompany.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompany()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        // You can set the Registration date column similarly if you have that data in Supplier
    }

    private void setupTableViewClickListener() {
        supplierTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click
                Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
                if (selectedSupplier != null) {
                    handleRowClick(selectedSupplier);
                }
            }
        });
    }

    private void handleRowClick(Supplier selectedSupplier) {
        System.out.println("Selected Supplier: " + selectedSupplier.getName());
        btnDelete.setVisible(true);
        btnEdit.setVisible(true);
        id = selectedSupplier.getId();
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        SupplierRepository.deleteSupplier(id);
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        // Load the correct FXML for the Edit Supplier Form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/overlays/supplier/edit_supplier_form.fxml"));
        try {
            Parent overlay = loader.load();
            Stage overlayStage = new Stage();
            overlayStage.setScene(new Scene(overlay));

            // Get the controller for the Edit Supplier Form
            EditSupplierFormController editController = loader.getController();

            // Set the supplier ID in txtSearch and trigger the search
            editController.setSearchText(String.valueOf(id));
            editController.btnSearchOnAction(null); // Call the search action

            overlayStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

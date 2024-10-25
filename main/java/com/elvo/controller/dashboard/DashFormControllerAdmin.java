package com.elvo.controller.dashboard;

import com.elvo.controller.inventory.CollectionFormController;
import com.elvo.controller.ui.UIManager;
import com.elvo.service.Session;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashFormControllerAdmin {

    private CollectionFormController collectionFormController;
    public DashFormControllerAdmin(){
        this.collectionFormController=new CollectionFormController();
    }

    @FXML
    private JFXButton btnAddAnUser;

    @FXML
    private JFXButton btnRemoveAnUser;


    private JFXButton btnRemoveSupplier;

    public void initialize(){
        if (Session.getType().matches("user")){

            btnAddAnUser.setVisible(false);
            btnRemoveAnUser.setVisible(false);
        }
    }

    // add users
    @FXML
    void btnAddAnUserOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/user/add_an_user_form.fxml");
    }

    // delete users
    @FXML
    void btnRemoveAnUserOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/user/delete_user_form.fxml");
    }

    // add suppliers
    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/supplier/add_suppliers_form.fxml");
    }

    // delete suppliers
    @FXML
    void btnRemoveSupplierOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/supplier/delete_supplier_form.fxml");
    }

    // update suppliers
    @FXML
    void btnEditSupplierOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/supplier/edit_supplier_form.fxml");
    }

    @FXML
    public void btnAddItemOnAction() {
        UIManager.openScenes("/view/overlays/item/add_item_form.fxml");
    }

    public void btnRemoveItemOnAction() {
        UIManager.openScenes("/view/overlays/item/delete_item_form.fxml");
    }

    public void btnEditItemOnAction() {
        UIManager.openScenes("/view/overlays/item/edit_item_form.fxml");
    }

    @FXML
    void btnOpenLadiesCollectionOnAction(ActionEvent event) throws IOException {
        // Load the FXML and get the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/collection_form.fxml"));
        Parent root = loader.load();

        CollectionFormController collectionFormControllerLadies = loader.getController();

        collectionFormControllerLadies.btnOpenLadiesCollectionOnAction();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        // Close the current stage if needed
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnOpenGentsCollectionOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/collection_form.fxml"));
        Parent root = loader.load();

        CollectionFormController collectionFormControllerGents = loader.getController();
        collectionFormControllerGents.btnOpenGentsCollectionOnAction();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnOpenKidsCollectionOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/collection_form.fxml"));
        Parent root = loader.load();

        CollectionFormController collectionFormControllerKids = loader.getController();
        collectionFormControllerKids.btnOpenKidsCollectionOnAction();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnAddAnOrderOnAction(ActionEvent event) {
        UIManager.openScenes("/view/place_order_form.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnPreviousOrdersOnAction() {
        UIManager.openScenes("/view/overlays/orders/previous_orders_form.fxml");
    }

    public void btnReturnedOrdersOnAction() {
        UIManager.openScenes("/view/overlays/orders/returned_orders_form.fxml");
    }

    public void btnSuppliersOnAction() {
        UIManager.openScenes("/view/overlays/supplier/supplier_list_form.fxml");
    }

    public void btnStatsOnAction(ActionEvent event) {
        UIManager.openScenes("/view/stats_form.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnExitOnAction() {
        Platform.exit();
    }


    public JFXButton getBtnRemoveSupplier() {
        return btnRemoveSupplier;
    }

    public void setBtnRemoveSupplier(JFXButton btnRemoveSupplier) {
        this.btnRemoveSupplier = btnRemoveSupplier;
    }
}

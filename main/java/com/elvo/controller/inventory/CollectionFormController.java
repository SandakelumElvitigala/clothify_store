package com.elvo.controller.inventory;

import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Item;
import com.elvo.repository.ItemRepository;
import com.elvo.service.InventoryService;
import com.elvo.util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
public class CollectionFormController {

    @Setter
    private ItemRepository itemRepository;
    private InventoryService inventoryService;

    public CollectionFormController() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
        this.inventoryService = new InventoryService();
    }

    @FXML
    private JFXButton btnOpenGentsCollection;

    @FXML
    private JFXButton btnOpenKidsCollection;

    @FXML
    private JFXButton btnOpenLadiesCollection;

    @FXML
    private AnchorPane ancLadiesCollectionOpen;

    @FXML
    private GridPane gridCards;

    @FXML
    private Label lblTitle;

    List<Item> items;

    @FXML
    public void initialize() {
        items = itemRepository.getItems();
    }

    public void btnOpenLadiesCollectionOnAction() {
        inventoryService.createItemCards(items, Item.Category.Ladies, gridCards);
    }

    public void btnOpenGentsCollectionOnAction() {
        inventoryService.createItemCards(items, Item.Category.Gents, gridCards);
    }

    public void btnOpenKidsCollectionOnAction() {
        inventoryService.createItemCards(items, Item.Category.Kids, gridCards);
    }


    @FXML
    void btnAddAnOrderOnAction(ActionEvent event) {
        UIManager.openScenes("/view/place_order_form.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    public void btnPreviousOrdersOnAction() {
        UIManager.openScenes("/view/overlays/orders/previous_orders_form.fxml");
    }

    public void btnReturnedOrdersOnAction() {
        UIManager.openScenes("/view/overlays/orders/returned_orders_form.fxml");
    }

    public void btnSuppliresOnAction() {
        UIManager.openScenes("/view/overlays/supplier/supplier_list_form.fxml");
    }

    public void btnStatsOnAction(ActionEvent event) {
        UIManager.openScenes("/view/stats_form.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnAddItemOnAction() {
        UIManager.openScenes("/view/overlays/item/add_item_form.fxml");
    }

    public void btnDashBoardOnAction(ActionEvent event) {
        UIManager.openScenes("/view/dash_form_admin.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}

package com.elvo.controller.inventory;

import com.elvo.controller.ItemController;
import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Item;
import com.elvo.entity.Supplier;
import com.elvo.repository.ItemRepository;
import com.elvo.repository.SupplierRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class LadiesCollectionFormController {

    @FXML
    private AnchorPane ancButtonPanel;


    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/add_item_form.fxml");
    }



    @FXML
    void btnFrocksOnAction(ActionEvent event) {

    }

    @FXML
    void btnOthersOnAction(ActionEvent event) {

    }

    @FXML
    void btnSareesOnAction(ActionEvent event) {

    }

    @FXML
    void btnSkirtsOnAction(ActionEvent event) {

    }

    @FXML
    void btnTopsOnAction(ActionEvent event) {

    }

    @FXML
    void btnTrousersOnAction(ActionEvent event) {

    }

    @FXML
    void btnUserList(ActionEvent event) {

    }

    public void btnAddImageOnAction(ActionEvent actionEvent) {
        // TODO document why this method is empty
    }
}

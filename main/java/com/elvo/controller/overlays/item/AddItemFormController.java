package com.elvo.controller.overlays.item;

import com.elvo.controller.ItemController;
import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Item;
import com.elvo.entity.Supplier;
import com.elvo.repository.ItemRepository;
import com.elvo.repository.SupplierRepository;
import com.elvo.util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddItemFormController {

    List<Supplier> suppliers = SupplierRepository.getAllSuppliers();

    String imageText;

    @Setter
    private ItemRepository itemRepository;

    public AddItemFormController() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
    }

    @FXML
    private JFXButton btnAddImage;

    @FXML
    private JFXButton btnAddItem;

    @FXML
    private ComboBox<Item.Category> comboCategory;

    @FXML
    private ComboBox<Item.Size> comboSize;

    @FXML
    private ComboBox<Item.SubCategory> comboSubCategory;

    @FXML
    private ComboBox<String> comboSupplier;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private ImageView imgImageView;



    @FXML
    public void initialize() {

        comboCategory.getItems().addAll(Item.Category.values());
        comboSubCategory.getItems().addAll(Item.SubCategory.values());
        comboSize.getItems().addAll(Item.Size.values());

        if (suppliers != null) {
            // Clear previous items
            comboSupplier.getItems().clear();

            // Add each supplier's details to the ComboBox
            for (Supplier supplier : suppliers) {
                String supplierInfo = supplier.getId() + " - " + supplier.getName() + " (" + supplier.getCompany() + ")";
                comboSupplier.getItems().add(supplierInfo);
            }
        }

        // Set the button action to open a file chooser
        btnAddImage.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");

            // Set file extension filters
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            // Show open file dialog
            File selectedFile = fileChooser.showOpenDialog(btnAddImage.getScene().getWindow());

            if (selectedFile != null) {
                try {
                    // Load the selected image into the ImageView
                    Image image = new Image(selectedFile.toURI().toString());
                    imgImageView.setImage(image);

                    // Convert the image file to Base64
                    imageText = ItemController.convertImageToBase64(selectedFile);

                    // You can store the base64Image string for further processing (e.g., saving to a database)

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        if (!txtItemName.getText().isEmpty() &&
                !txtStock.getText().isEmpty() &&
                !txtPrice.getText().isEmpty() &&
                comboSupplier.getValue() != null &&
                comboCategory.getValue() != null &&
                comboSubCategory.getValue() != null &&
                comboSize.getValue() != null) {

            Item item = new Item();
            item.setItemName(txtItemName.getText());
            item.setPrice(Double.valueOf(txtPrice.getText()));
            item.setSize(comboSize.getValue());
            item.setCategory(comboCategory.getValue());
            item.setSubCategory(comboSubCategory.getValue()); // Add this line
            item.setImage(imageText);
            item.setSupplier(comboSupplier.getValue());
            item.setStock(Integer.valueOf(txtStock.getText()));
            itemRepository.saveOrUpdateItem(item);
        } else {
            UIManager.showDialogueBox("Error","Please give all the required data");
        }
    }

}

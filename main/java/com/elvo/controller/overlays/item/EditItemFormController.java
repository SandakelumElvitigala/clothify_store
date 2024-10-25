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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class EditItemFormController{

    List<Supplier> suppliers = SupplierRepository.getAllSuppliers();

    String imageText;

    private Item item = new Item();

    @Setter
    private ItemRepository itemRepository;

    public EditItemFormController() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
    }

    @FXML
    private ComboBox<Item.Category> comboCategory;

    @FXML
    private ComboBox<Item.Size> comboSize;

    @FXML
    private ComboBox<Item.SubCategory> comboSubCategory;

    @FXML
    private ComboBox<String> comboSupplier;

    @FXML
    private ImageView imgItem;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXButton btnEditImage;

    @FXML
    private JFXButton btnSearch;

    // Method to set the ID
    public void setItemId(String id) {
        txtId.setText(id);
    }

    // Method to automatically click the search button
    public void autoClickSearch() {
        btnSearch.fire(); // This simulates a button click
    }



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
        btnEditImage.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");

            // Set file extension filters
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            // Show open file dialog
            File selectedFile = fileChooser.showOpenDialog(btnEditImage.getScene().getWindow());

            if (selectedFile != null) {
                try {
                    // Load the selected image into the ImageView
                    Image image = new Image(selectedFile.toURI().toString());
                    imgItem.setImage(image);

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
    void btnEditOnAction(ActionEvent event) {
        if (!txtName.getText().isEmpty() &&
                !txtStock.getText().isEmpty() &&
                !txtPrice.getText().isEmpty() &&
                comboSupplier.getValue() != null &&
                comboCategory.getValue() != null &&
                comboSubCategory.getValue() != null &&
                comboSize.getValue() != null) {

            item.setItemName(txtName.getText());
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

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        item = itemRepository.getItemById(txtId.getText());
        if (item != null) {
            txtName.setText(item.getItemName());
            txtPrice.setText(String.valueOf(item.getPrice()));
            txtStock.setText(String.valueOf(item.getStock()));

            String itemSize = String.valueOf(item.getSize());
            comboSize.getSelectionModel().select(Item.Size.valueOf(itemSize));

            String category = String.valueOf(item.getCategory());
            comboCategory.getSelectionModel().select(Item.Category.valueOf(category));

            String subCategory = String.valueOf(item.getSubCategory());
            comboSubCategory.getSelectionModel().select(Item.SubCategory.valueOf(subCategory));

            comboSupplier.getSelectionModel().select(item.getSupplier());

            String base64Image = item.getImage();
            // Decode the Base64 string into bytes
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

            // Convert byte array into InputStream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

            // Create an Image object from the InputStream
            Image image = new Image(inputStream);
            imgItem.setImage(image);
        } else UIManager.showDialogueBox("Error","Item not found");
    }

}

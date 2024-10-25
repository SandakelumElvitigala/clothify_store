package com.elvo.controller.overlays.item;

import com.elvo.entity.Item;
import com.elvo.repository.ItemRepository;
import com.elvo.util.HibernateUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class DeleteItemFormController {

    private Item item = new Item();

    @Setter
    private ItemRepository itemRepository;

    public DeleteItemFormController() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
    }


    @FXML
    private ImageView imgItem;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblSize;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblSubCategory;

    @FXML
    private Label lblSupplier;

    @FXML
    private JFXTextField txtId;

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        item = itemRepository.getItemById(txtId.getText());
        if (item != null){
            lblName.setText(item.getItemName());
            lblCategory.setText(String.valueOf(item.getCategory()));
            lblSubCategory.setText(String.valueOf(item.getSubCategory()));
            lblSize.setText(String.valueOf(item.getSize()));
            lblPrice.setText(String.valueOf(item.getPrice()));
            lblStock.setText(String.valueOf(item.getStock()));
            lblSupplier.setText(item.getSupplier());

            String base64Image = item.getImage();
            // Decode the Base64 string into bytes
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

            // Convert byte array into InputStream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

            // Create an Image object from the InputStream
            Image image = new Image(inputStream);
            imgItem.setImage(image);
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        itemRepository.deleteItemById(String.valueOf(item.getId()));
    }
}

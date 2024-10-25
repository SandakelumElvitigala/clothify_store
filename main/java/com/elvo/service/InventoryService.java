package com.elvo.service;

import com.elvo.controller.overlays.item.EditItemFormController;
import com.elvo.entity.Item;
import com.elvo.repository.ItemRepository;
import com.elvo.util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class InventoryService {

    @Setter
    private ItemRepository itemRepository;

    public InventoryService() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
    }



    public void createItemCards(List<Item> items, Item.Category type, GridPane gridCards) {
        gridCards.getChildren().clear(); // Clear existing cards

        int column = 0;
        int row = 0;

        for (Item item : items) {
            // Filter by Ladies category with null check
            if (item.getCategory() != type) {
                continue; // Skip items not in the Ladies category
            }

            VBox itemCard = new VBox();
            itemCard.setSpacing(10);
            itemCard.setAlignment(Pos.CENTER);
            itemCard.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ccc; -fx-border-radius: 5;");
            itemCard.setPrefSize(200, 250); // Ensure the card is sized properly

            // Check for image data
            String imageData = item.getImage();
            if (imageData != null && !imageData.isEmpty()) {
                try {
                    ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(imageData))));
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);
                    itemCard.getChildren().add(imageView);
                } catch (IllegalArgumentException e) {
                    System.err.println("Failed to load image for item: " + item.getItemName());
                    e.printStackTrace();
                }
            }

            // Item Name
            Label itemName = new Label(item.getItemName());
            itemCard.getChildren().add(itemName);

            // Item Category | Sub Category
            Label itemCategory = new Label(item.getCategory() + " | " + item.getSubCategory() + " | " + item.getId());
            itemCard.getChildren().add(itemCategory);

            // Supplier
            Label supplier = new Label(item.getSupplier());
            itemCard.getChildren().add(supplier);

            // Price
            Label priceLabel = new Label("Rs." + item.getPrice());
            itemCard.getChildren().add(priceLabel);

            // Edit button
            JFXButton editOnAction = new JFXButton("Edit");
            itemCard.getChildren().add(editOnAction);
            editOnAction.setOnAction(event -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/overlays/item/edit_item_form.fxml"));
                try {
                    Parent overlay = loader.load();
                    Stage overlayStage = new Stage();
                    overlayStage.setScene(new Scene(overlay));

                    EditItemFormController editController = loader.getController();
                    editController.setItemId(String.valueOf(item.getId()));
                    editController.autoClickSearch();

                    overlayStage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Delete button
            JFXButton deleteOnAction = new JFXButton("Delete");
            itemCard.getChildren().add(deleteOnAction);
            deleteOnAction.setOnAction(event -> itemRepository.deleteItemById(String.valueOf(item.getId())));

            // Add card to grid at the specified column and row
            gridCards.add(itemCard, column, row);

            // Move to the next column
            column++;

            // If the current row is filled (e.g., 4 columns), move to the next row
            if (column >= 4) {
                column = 0;
                row++;
            }
        }

        gridCards.setGridLinesVisible(true); // Enable grid lines to see where cards are being added
    }

}

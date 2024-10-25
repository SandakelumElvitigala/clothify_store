package com.elvo.controller.inventory;

import com.elvo.entity.Item;
import com.elvo.repository.ItemRepository;
import com.elvo.util.HibernateUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@ToString
public class InventoryController {

    @Setter
    private ItemRepository itemRepository;

    // Lists to store items by category
    private List<Item> ladiesItems = new ArrayList<>();
    private List<Item> gentsItems = new ArrayList<>();
    private List<Item> kidsItems = new ArrayList<>();

    List<Item> items = itemRepository.getItems();

    public InventoryController() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
    }

    public List<Item> getLadiesItems(){
        ladiesItems = items.stream()
                .filter(item -> "Ladies".equals(item.getCategory()))
                .collect(Collectors.toList());
        return ladiesItems;
    }
    public List<Item> getGentsItems(){
        gentsItems = items.stream()
                .filter(item -> "Gents".equals(item.getCategory()))
                .collect(Collectors.toList());
        return gentsItems;
    }
    public List<Item> getKidsItems(){
        kidsItems = items.stream()
                .filter(item -> "Ladies".equals(item.getCategory()))
                .collect(Collectors.toList());
        return kidsItems;
    }



    public void createItemCards(GridPane gridCards, List<Item> listItems) {
        gridCards.getChildren().clear(); // Clear previous cards

        int column = 0;
        int row = 0;

        for (Item item : items) {
            VBox itemCard = new VBox();
            itemCard.setSpacing(10);
            itemCard.setAlignment(Pos.CENTER);
            itemCard.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ccc; -fx-border-radius: 5;");

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
            itemCard.getChildren().add(new Label(item.getItemName()));
            // Item Category | Sub Category
            itemCard.getChildren().add(new Label( item.getSubCategory() + " | " + item.getId()));
            // Supplier
            itemCard.getChildren().add(new Label(item.getSupplier()));
            // Stock
            itemCard.getChildren().add(new Label(String.valueOf(item.getStock())));
            // Price
            itemCard.getChildren().add(new Label("Rs." + item.getPrice()));

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
    }

}

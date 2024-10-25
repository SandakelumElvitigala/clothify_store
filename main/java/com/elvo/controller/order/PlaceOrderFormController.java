package com.elvo.controller.order;

import com.elvo.controller.inventory.CollectionFormController;
import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Item;
import com.elvo.entity.Order;
import com.elvo.entity.OrderItems;
import com.elvo.repository.ItemRepository;
import com.elvo.service.Session;
import com.elvo.service.impl.EBillGenerator;
import com.elvo.service.impl.SendBill;
import com.elvo.util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;
import org.hibernate.Transaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

public class PlaceOrderFormController {

    String empEmail = Session.getEmail();

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXTextField txtSearchId;

    @FXML
    private AnchorPane ancItemDisplay;

    @FXML
    private JFXButton brnAdd;

    @FXML
    private ImageView imgItem;

    @FXML
    private Label lblDashBoard;

    @FXML
    private Label lblItemCategory;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblItemStock;

    @FXML
    private Label lblItemSubCategory;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblTotal;

    @FXML
    private JFXTextField txtCustomerEmail;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TableView<OrderItems> tblOrder;

    @FXML
    private TableColumn<?, ?> tblRowId;

    @FXML
    private TableColumn<?, ?> tblRowQty;

    @FXML
    private TableColumn<?, ?> tblRowTot;

    @FXML
    private TableColumn<?, ?> tblRowUnit;

    private ObservableList<OrderItems> orderItems = FXCollections.observableArrayList();

    @Setter
    private ItemRepository itemRepository;

    public PlaceOrderFormController() {
        this.itemRepository = new ItemRepository(HibernateUtil.getSessionFactory());
    }

    private Item item = new Item();

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        UIManager.openScenes("/view/dash_form_admin.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btnOpenGentsCollectionOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/collection_form.fxml"));
        Parent root = loader.load();

        CollectionFormController collectionFormController = loader.getController();
        collectionFormController.btnOpenGentsCollectionOnAction();

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

        CollectionFormController collectionFormController = loader.getController();
        collectionFormController.btnOpenKidsCollectionOnAction();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnOpenLadiesCollectionOnAction(ActionEvent event) throws IOException {
        // Load the FXML and get the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/collection_form.fxml"));
        Parent root = loader.load();

        CollectionFormController collectionFormController = loader.getController();

        collectionFormController.btnOpenLadiesCollectionOnAction();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        // Close the current stage if needed
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnPreviousOrdersOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/orders/previous_orders_form.fxml");
    }

    @FXML
    void btnReturnedOrdersOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/orders/returned_orders_form.fxml");
    }

    @FXML
    void btnStatsOnAction(ActionEvent event) {
        UIManager.openScenes("/view/stats_form.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnSuppliresOnAction(ActionEvent event) {
        UIManager.openScenes("/view/overlays/supplier/supplier_list_form.fxml");
    }

    public void btnSearchOnAction() {
        item = itemRepository.getItemById(txtSearchId.getText());
        if (item != null) {
            ancItemDisplay.setVisible(true);
            lblItemName.setText(item.getItemName());
            lblItemCategory.setText(String.valueOf(item.getCategory()));
            lblItemSubCategory.setText(String.valueOf(item.getSubCategory()));
            lblItemPrice.setText(String.valueOf(item.getPrice()));
            lblItemStock.setText(String.valueOf(item.getStock()));
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(item.getImage()))));
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imgItem.setImage(imageView.getImage());
        }else{
            UIManager.showDialogueBox("Not found","Item not found");
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        // Validate input fields
        if (txtQty.getText().isEmpty()) {
            UIManager.showDialogueBox("Error", "Quantity cannot be empty");
            return;
        }

        if (lblItemStock.getText().isEmpty()) {
            UIManager.showDialogueBox("Error", "Item stock information is missing");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(txtQty.getText());
        } catch (NumberFormatException e) {
            UIManager.showDialogueBox("Error", "Invalid quantity format");
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(lblItemStock.getText());
        } catch (NumberFormatException e) {
            UIManager.showDialogueBox("Error", "Invalid stock format");
            return;
        }

        // Check if the quantity is greater than available stock
        if (quantity > stock) {
            UIManager.showDialogueBox("Error", "Stock is not enough");
            return;
        }

        // Check for duplicate item in the order
        for (OrderItems existingItem : orderItems) {
            if (existingItem.getItemId().equals(txtSearchId.getText())) {
                UIManager.showDialogueBox("Error", "Item already added to the order");
                return;
            }
        }

        // Fetch the item from the repository using the item ID
        Item itemToAdd = itemRepository.getItemById(txtSearchId.getText());
        if (itemToAdd == null) {
            UIManager.showDialogueBox("Error", "Item not found");
            return;
        }

        // Create a new OrderItem and add it to the orderItems list
        OrderItems orderItem = new OrderItems(
                null,  // order will be set later when placing the order
                itemToAdd,
                quantity,
                Double.parseDouble(lblItemPrice.getText())
        );

        // Add the OrderItem to the ObservableList
        orderItems.add(orderItem);

        // Set the data to the TableView
        tblOrder.setItems(orderItems);

        // Set up the table columns to map to the OrderItem properties
        tblRowId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblRowQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblRowUnit.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblRowTot.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Calculate the total price of all items in the order
        double total = 0.0; // Initialize the total
        for (OrderItems item : orderItems) {
            total += item.getTotalPrice();  // Sum up the total price for each item
        }

        // Display the total in the lblTotal label
        lblTotal.setText(String.format("%.2f", total));  // Format the total to 2 decimal places

        // Optionally, update the stock label after adding the order
        int remainingStock = stock - quantity;
        lblItemStock.setText(String.valueOf(remainingStock));

        // Clear the quantity input after adding
        txtQty.clear();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }


    @FXML
    public void btnInsertAnOrderOnAction(ActionEvent event) {
        System.out.println(empEmail);
        LocalDateTime orderDate = LocalDateTime.now();

        // Check if there are items to place the order
        if (orderItems.isEmpty()) {
            UIManager.showDialogueBox("Error", "No items added to the order.");
            return;
        }

        Double totalAmount;
        try {
            totalAmount = Double.parseDouble(lblTotal.getText());
        } catch (NumberFormatException e) {
            UIManager.showDialogueBox("Error", "Invalid total amount.");
            return;
        }

        String customerEmail = txtCustomerEmail.getText().trim();
        if (customerEmail.isEmpty() || !isValidEmail(customerEmail)) {
            UIManager.showDialogueBox("Error", "Invalid or empty customer email.");
            return;
        }

        // Create new Order object
        Order newOrder = new Order(orderDate, totalAmount, customerEmail, empEmail, "ok", null);

        List<OrderItems> orderItemsList = orderItems.stream()
                .map(orderItem -> new OrderItems(newOrder, itemRepository.getItemById(String.valueOf(orderItem.getItemId())), orderItem.getQuantity(), orderItem.getUnitPrice()))
                .toList();

        newOrder.setOrderItems(orderItemsList);

        Transaction transaction = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Save the order
            session.save(newOrder);

            // Update the stock for each item in the order and save to database
            for (OrderItems orderItem : orderItemsList) {
                Item item = session.get(Item.class, orderItem.getItemId());
                if (item != null) {
                    int newStock = item.getStock() - orderItem.getQuantity();
                    if (newStock < 0) {
                        UIManager.showDialogueBox("Error", "Insufficient stock for " + item.getItemName());
                        transaction.rollback();
                        return;
                    }
                    item.setStock(newStock);

                    // Save or update the item using itemRepository
                    itemRepository.saveOrUpdateItem(item);
                }
            }

            // Commit the transaction after successful update
            transaction.commit();
            UIManager.showDialogueBox("Success", "Order placed successfully! Order ID: " + newOrder.getId());

            // Create bill
            String pdfPath = EBillGenerator.generateBill(newOrder);

            // Send e-bill via email outside the transaction
            sendEmailWithBill(newOrder, pdfPath);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            UIManager.showDialogueBox("Error", "Failed to place the order. Please try again.");
        }
    }

    private void sendEmailWithBill(Order newOrder, String pdfPath) {
        // Send e-bill via email
        if (pdfPath != null) {
            SendBill.sendBill(newOrder.getCustomerEmail(), "Your E-Bill from Clothify",
                    "Thank you for your order! Please find the attached e-bill.", pdfPath);
        } else {
            UIManager.showDialogueBox("Error", "Failed to generate the e-bill. Email not sent.");
        }
    }


}

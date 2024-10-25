package com.elvo.controller.overlays.orders;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.elvo.entity.Order;
import com.elvo.entity.OrderItems; // Make sure you import the OrderItem class
import com.elvo.entity.Item; // Import your Item class
import com.elvo.util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

public class PreviousOrdersFormController {

    @FXML
    public TableView<Order> tblPreviousOrders;

    @FXML
    private TableColumn<Order, Long> colID;

    @FXML
    private TableColumn<Order, String> colPlacedAt;

    @FXML
    private TableColumn<Order, Double> colAmount;

    @FXML
    private TableColumn<Order, String> colCustEmail;

    @FXML
    private TableColumn<Order, String> colEmpEmail;

    // This method will be called when the window opens
    public void initialize() {
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<Order> ordersList = FXCollections.observableArrayList();

        // Fetch orders from the database
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Query to get orders with status 'ok'
            List<Order> orders = session.createQuery("FROM Order WHERE status = :status", Order.class)
                    .setParameter("status", "ok")
                    .getResultList();

            ordersList.addAll(orders); // Add the retrieved orders to the ObservableList

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle exceptions properly in production code
        }

        // Set the items to the table view
        tblPreviousOrders.setItems(ordersList);

        // Bind the columns to the properties of the Order entity
        colID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colPlacedAt.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate().toString())); // Adjust format as needed
        colAmount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalAmount()));
        colCustEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCustomerEmail()));
        colEmpEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployeeEmail()));
    }

    @FXML
    void btnReturnOnAction(ActionEvent event) {
        Order selectedOrder = tblPreviousOrders.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            // Show an alert if no order is selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Order Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an order to return.");
            alert.showAndWait();
            return; // Exit the method if no order is selected
        }

        // Attempt to change the order status
        try {
            changeOrderStatus(selectedOrder, "returned"); // Replace "returned" with the desired status
        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
    }

    private void changeOrderStatus(Order order, String newStatus) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Refresh the order to ensure it's attached to the current session and all fields are initialized
            session.refresh(order);

            // Update the order status and set the returned date
            order.setStatus(newStatus);
            order.setReturnedDate(LocalDateTime.now());

            // Iterate over the order items and update stock
            for (OrderItems orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem(); // Access the item directly
                item.setStock(item.getStock() + orderItem.getQuantity()); // Add the quantity back to stock
                session.update(item); // Update the item in the database
            }

            session.update(order); // Update the order in the database
            transaction.commit(); // Commit the transaction if everything is successful
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback(); // Rollback transaction on error
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace(); // Handle rollback failure
                }
            }
            e.printStackTrace(); // Log or handle the exception appropriately
        }
    }


}

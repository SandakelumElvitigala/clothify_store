package com.elvo.controller.overlays.orders;

import com.elvo.entity.Order;
import com.elvo.util.HibernateUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReturnedOrdersFormController {

    @FXML
    private TableColumn<Order, Long> colId;

    @FXML
    private TableColumn<Order, String> colOrderedAt;

    @FXML
    private TableColumn<Order, String> colReturnedAt;

    @FXML
    private TableColumn<Order, Double> colValue;

    @FXML
    private TableColumn<Order, String> colCustEmail;

    @FXML
    private TableColumn<Order, String> colEmpEmail;

    @FXML
    private TableView<Order> tblReturned;

    public void initialize() {
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<Order> ordersList = FXCollections.observableArrayList();

        // Fetch orders from the database
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Query to get orders with status 'returned'
            List<Order> orders = session.createQuery("FROM Order WHERE status = :status", Order.class)
                    .setParameter("status", "returned")
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
        tblReturned.setItems(ordersList);

        // Bind the columns to the properties of the Order entity
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colOrderedAt.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate().toString())); // Adjust format as needed
        colReturnedAt.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReturnedDate() != null ? cellData.getValue().getReturnedDate().toString() : "N/A"));
        colValue.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalAmount()));
        colCustEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCustomerEmail()));
        colEmpEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployeeEmail()));
    }
}

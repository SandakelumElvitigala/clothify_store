package com.elvo.controller.reports;

import com.elvo.controller.inventory.CollectionFormController;
import com.elvo.controller.ui.UIManager;
import com.elvo.util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class StatsFormController {

    @FXML
    private JFXButton btnSalesRep;

    public void initialize(){
        if (com.elvo.service.Session.getType().matches("admin")){
            btnSalesRep.setVisible(true);
        }
    }

    public void btnUserRepOnAction() {
        Session session = null;
        Connection connection = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Get the JDBC connection from the Hibernate session
            connection = session.doReturningWork(Connection::createStatement)
                    .getConnection();

            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/user.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void bnOrderRepOnAction() {
        Session session = null;
        Connection connection = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Get the JDBC connection from the Hibernate session
            connection = session.doReturningWork(Connection::createStatement)
                    .getConnection();

            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/orders.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnItemsRepOnAction() {
        Session session = null;
        Connection connection = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Get the JDBC connection from the Hibernate session
            connection = session.doReturningWork(Connection::createStatement)
                    .getConnection();

            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/items.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSalesRepOnAction() {
        Session session = null;
        Connection connection = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Get the JDBC connection from the Hibernate session
            connection = session.doReturningWork(Connection::createStatement)
                    .getConnection();

            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/sales.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnAddAnOrderOnAction() {
        UIManager.openScenes("/view/place_order_form.fxml");
    }

    public void btnPreviousOrdersOnAction() {
        UIManager.openScenes("/view/overlays/orders/previous_orders_form.fxml");
    }

    public void btnReturnedOrdersOnAction() {
        UIManager.openScenes("/view/overlays/orders/returned_orders_form.fxml");
    }

    public void btnOpenGentsCollectionOnAction(ActionEvent event) throws IOException {
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

    public void btnOpenKidsCollectionOnAction(ActionEvent event) throws IOException {
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

    public void btnOpenLadiesCollectionOnAction(ActionEvent event) throws IOException {
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

    public void btnSuppliresOnAction() {
        UIManager.openScenes("/view/overlays/supplier/supplier_list_form.fxml");
    }

    public void btnStatsOnAction(ActionEvent event) {
        UIManager.openScenes("/view/stats_form.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnExitOnAction() {
        Platform.exit();
    }

    public void btnDashboardOnAction(ActionEvent event) {
        UIManager.openScenes("/view/dash_form_admin.fxml");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}

package com.elvo.repository.user;

import com.elvo.controller.ui.UIManager;
import com.elvo.model.Supplier;
import com.elvo.model.User;
import com.elvo.repository.db.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierRepository {

    public static void addSupplier(String name, String company, String email) {
        String generatedSupplierId = null;

        try {
            // Get the database connection
            Connection connection = DatabaseConnection.getInstance().getConnection();

            // Retrieve the current row count (number of rows in the table)
            String countQuery = "SELECT COUNT(*) AS total FROM suppliers";
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            ResultSet countResultSet = countStatement.executeQuery();

            if (countResultSet.next()) {
                int rowCount = countResultSet.getInt("total");
                // Generate the custom supplier ID as "Sup_RowNumber"
                generatedSupplierId = "Sup_" + (rowCount + 1);
            }

            // Prepare the insert statement
            String query = "INSERT INTO suppliers (id, name, company, email) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the values for supplier_id, name, company, and email
            preparedStatement.setString(1, generatedSupplierId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, company);
            preparedStatement.setString(4, email);

            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if rows were affected to confirm the insertion
            if (rowsAffected <= 0) {
                UIManager.showDialogueBox("Something Went Wrong..!", "Supplier Insertion Failed. Please try again later.");
            } else {
                UIManager.showDialogueBox("Successfully Added", "Successfully added new Supplier with ID: " + generatedSupplierId);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
            UIManager.showDialogueBox("Error", "An error occurred while adding the supplier. Please try again.");
        }
    }

    public static Supplier searchSupplier(String index){
        String query = "SELECT * FROM suppliers WHERE email = ? OR name = ? OR id = ?";
        Supplier supplier = null;

        try {
            // Get the database connection
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for email, username, and userId
            preparedStatement.setString(1, index); // email
            preparedStatement.setString(2, index); // username
            preparedStatement.setString(3, index); // userId

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a user with the given index is found, populate the UserModel object
            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String company = resultSet.getString("company");

                // Create a new UserModel object with the retrieved data
                supplier = new Supplier(id, name, company, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        } catch (NumberFormatException e) {
            // Handle cases where the index is not an integer (e.g., username or email)
            System.out.println("The provided index is not a valid userId.");
        }

        return supplier;  // Return the user if found, otherwise null
    }

    public static void updateSupplier(String id, String name, String company, String email) {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            // SQL UPDATE query
            String query = "UPDATE suppliers SET name = ?, company = ?, email = ? WHERE supplier_id = ?";

            // Establish the connection
            connection = DatabaseConnection.getInstance().getConnection();

            // Create the PreparedStatement
            statement = connection.prepareStatement(query);

            // Set the values in the query
            statement.setString(1, name);    // Set name
            statement.setString(2, company); // Set company
            statement.setString(3, email);   // Set email
            statement.setString(4, id);      // Set the supplier ID

            // Execute the update
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                UIManager.showDialogueBox("Success","Supplier information updated successfully.");
            } else {
                UIManager.showDialogueBox("Not found","No supplier found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            UIManager.showDialogueBox("Error","Error updating supplier: " + e.getMessage());
        } finally {
            // Close the resources
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteSupplier(String id) {
        String query = "DELETE FROM suppliers WHERE id = ?"; // Adjust the column name if necessary

        try {
            // Get the database connection
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the ID parameter
            preparedStatement.setString(1, id);

            // Execute the delete query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if any rows were affected
            if (rowsAffected > 0) {
                UIManager.showDialogueBox("Successfully deleted..!","The supplier with the Id "+id+" was successfully deleted");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }


    }





}

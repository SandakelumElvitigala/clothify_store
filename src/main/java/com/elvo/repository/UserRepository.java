package com.elvo.repository.user;

import com.elvo.controller.ui.UIManager;
import com.elvo.model.User;
import com.elvo.repository.db.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static void getAllUsers(){
        String query = "SELECT*FROM users";

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("userId");
                String username = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                System.out.println("ID: " + id + ", Username: " + username + ", Email: " + email);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isUserValid(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a matching user is found
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, hashedPassword);  // Compare passwords
            } else {
                return false; // No matching user
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log and handle SQL exceptions
            return false;
        }
    }

    public static boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returns true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return false; // Return false if the update failed
    }

    public static String addUsers(String name, String email, String password, String role) {
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        String generatedUserId = null;

        try {
            // Get the database connection
            Connection connection = DatabaseConnection.getInstance().getConnection();

            // Prepare the statement with RETURN_GENERATED_KEYS
            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // Set the values for name, email, password, and role
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);

            // Hash the password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setString(4, role);

            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();

            // Retrieve the generated userId
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Get the generated userId
                    generatedUserId = String.valueOf(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }

        return generatedUserId;  // Return the generated userId as a string
    }

    public static User searchUser(String index) {
        String query = "SELECT * FROM users WHERE email = ? OR name = ? OR userId = ?";
        User user = null;

        try {
            // Get the database connection
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for email and name
            preparedStatement.setString(1, index); // email
            preparedStatement.setString(2, index); // name

            // Check if the index is a valid integer for userId
            if (isInteger(index)) {
                preparedStatement.setInt(3, Integer.parseInt(index)); // userId
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER); // Set userId as NULL if not an integer
            }

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a user with the given index is found, populate the User object
            if (resultSet.next()) {
                String userId = String.valueOf(resultSet.getInt("userId"));
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password"); // Password is hashed
                String role = resultSet.getString("role");

                // Create a new User object with the retrieved data
                user = new User(userId, email, password, name, role);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }

        return user;  // Return the user if found, otherwise null
    }

    // Helper method to check if a string is an integer
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static void deleteUser(String id) {
        String query = "DELETE FROM users WHERE userId = ?"; // Adjust the column name if necessary

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
                UIManager.showDialogueBox("Successfully deleted..!","The user account with the Id "+id+" was successfully deleted");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }


    }



}

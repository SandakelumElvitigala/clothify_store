package com.elvo.repository;

import com.elvo.controller.ui.UIManager;
import com.elvo.entity.User;
import com.elvo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import javax.transaction.Transactional;

public class UserRepository {

    public static boolean isUserValid(String email, String password){
        User user = getUserByEmail(email);

        // If user is found, compare the stored hashed password with the provided password
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword()); // This checks if the password matches
        }

        // Return false if no user is found or password doesn't match
        return false;
    }

    @Transactional
    public static void createUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);  // Save user object to database
            transaction.commit();
            UIManager.showDialogueBox("Success","User Successfully Registered");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            UIManager.showDialogueBox("Error","Something went wrong");
        }
    }

    public static User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);  // Retrieve user by ID
        }
    }

    public static User getUserByIndex(String index) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Attempt to convert the index to a Long for the userId field
            Long id = null;
            try {
                id = Long.valueOf(index); // If index is numeric, it's a userId
            } catch (NumberFormatException e) {
                // If the index is not a number, it's treated as an email
            }

            // Query the database for either email or userId
            return session.createQuery("FROM User WHERE email = :email OR userId = :id", User.class)
                    .setParameter("email", index)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }


    public static User getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    public static boolean updatePassword(String email, String newPassword) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start the transaction
            transaction = session.beginTransaction();

            // Retrieve the user by email
            User user = getUserByEmail(email);

            // Check if the user exists
            if (user != null) {
                // BCrypt the new password
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                // Update the user's password
                user.setPassword(hashedPassword);

                // Save the updated user object
                session.update(user);

                // Commit the transaction
                transaction.commit();

                UIManager.showDialogueBox("Successful","Password successfully updated");
                return true;
            } else {
                // If user doesn't exist, rollback the transaction
                if (transaction != null) {
                    transaction.rollback();
                }
                UIManager.showDialogueBox("Un-successful","User not found..");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            UIManager.showDialogueBox("Un-successful","Something went wrong");
            return false;
        }
    }


    public static void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);  // Update user details
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void deleteUser(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);  // Attempt to delete user
            transaction.commit();
            UIManager.showDialogueBox("Successful", "User deleted successfully");  // Set to true if no exception occurs
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            UIManager.showDialogueBox("Error","Something went wrong");
        }


    }


}

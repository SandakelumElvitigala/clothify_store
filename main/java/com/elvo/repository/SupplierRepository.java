package com.elvo.repository;

import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Supplier;
import com.elvo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.util.List;

public class SupplierRepository {

    @Transactional
    public static void addSupplier(String name, String company, String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Supplier supplier = new Supplier();
            supplier.setName(name);
            supplier.setCompany(company);
            supplier.setEmail(email);

            // Set ID to null to let Hibernate handle ID generation
            supplier.setId(null);

            session.beginTransaction();
            session.save(supplier);
            session.getTransaction().commit();

            UIManager.showDialogueBox("Successfully Added", "Successfully added new Supplier with ID: " + supplier.getId());
        } catch (Exception e) {
            e.printStackTrace();
            UIManager.showDialogueBox("Error", "An error occurred while adding the supplier. Please try again.");
        }
    }

    public static List<Supplier> getAllSuppliers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Create a query to get all suppliers
            Query<Supplier> query = session.createQuery("FROM Supplier", Supplier.class);
            // Return the list of suppliers
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Supplier searchSupplier(String index) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Supplier> query = session.createQuery("FROM Supplier s WHERE s.email = :index OR s.name = :index OR s.id = :index", Supplier.class);
            query.setParameter("index", index);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace(); // Log any exception
            return null;
        }
    }

    public static void updateSupplier(Long id, String name, String company, String email) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Supplier supplier = session.get(Supplier.class, id);
            if (supplier != null) {
                supplier.setName(name);
                supplier.setCompany(company);
                supplier.setEmail(email);
                session.update(supplier);
                transaction.commit();
                UIManager.showDialogueBox("Success", "Supplier information updated successfully.");
            } else {
                UIManager.showDialogueBox("Not found", "No supplier found with the given ID.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            UIManager.showDialogueBox("Error", "Error updating supplier: " + e.getMessage());
        }
    }

    public static void deleteSupplier(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Supplier supplier = session.get(Supplier.class, id);
            if (supplier != null) {
                session.delete(supplier);
                transaction.commit();
                UIManager.showDialogueBox("Successful", "Supplier deleted successfully");
            } else {
                UIManager.showDialogueBox("Not found", "No supplier found with the given ID.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            UIManager.showDialogueBox("Error", "Something went wrong");
        }
    }
}

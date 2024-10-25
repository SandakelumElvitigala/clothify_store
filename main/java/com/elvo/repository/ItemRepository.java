package com.elvo.repository;

import com.elvo.controller.ui.UIManager;
import com.elvo.entity.Item;
import com.elvo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ItemRepository {

    private final SessionFactory sessionFactory;

    public ItemRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save or update an item (non-static now)
    public void saveOrUpdateItem(Item item) {
        Session session = null;
        Transaction transaction = null;

        try {
            // Open a session
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Save or update the item
            session.saveOrUpdate(item);

            // Commit the transaction
            transaction.commit();
            UIManager.showDialogueBox("Success","Item added successfully");
        } catch (Exception e) {
            // Rollback the transaction if there's an error
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Log the error for debugging
        } finally {
            // Close the session
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Item> getItems() {
        List<Item> items = null;
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // HQL to retrieve all items
            Query<Item> query = session.createQuery("FROM Item", Item.class);
            items = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return items;
    }

    // Get item by id
    public Item getItemById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Item.class, Long.parseLong(id));
        }
    }

    // Get all items
    public List<Item> getAllItems() {
        try (Session session = sessionFactory.openSession()) {
            Query<Item> query = session.createQuery("from Item", Item.class);
            return query.list();
        }
    }

    // Find items by category
    public List<Item> findItemsByCategory(Item.Category category) {
        try (Session session = sessionFactory.openSession()) {
            Query<Item> query = session.createQuery("from Item where category = :category", Item.class);
            query.setParameter("category", category);
            return query.list();
        }
    }

    // Find items by subcategory
    public List<Item> findItemsBySubCategory(Item.SubCategory subCategory) {
        try (Session session = sessionFactory.openSession()) {
            Query<Item> query = session.createQuery("from Item where subCategory = :subCategory", Item.class);
            query.setParameter("subCategory", subCategory);
            return query.list();
        }
    }

    // Delete an item by id
    public void deleteItemById(String id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, Long.parseLong(id));
            if (item != null) {
                session.delete(item);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}

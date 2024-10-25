package com.elvo.entity;

import com.elvo.util.HibernateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    // Constructor with order and item details
    public OrderItems(Order order, Item item, Integer quantity, Double unitPrice) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalPrice(); // Calculate total price
    }

    public OrderItems(Item item, Integer quantity, Double unitPrice) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalPrice(); // Calculate total price
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice(); // Update total price when quantity changes
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice(); // Update total price when unit price changes
    }

    private void calculateTotalPrice() {
        this.totalPrice = unitPrice * quantity; // Recalculate total price
    }

    // New method to get the itemId from the associated Item entity
    public Serializable getItemId() {
        return (item != null) ? item.getId() : null; // Assuming Item has a getId() method
    }

    private Item getItemById(Long itemId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Item.class, itemId);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle exceptions as needed
        }
    }

}

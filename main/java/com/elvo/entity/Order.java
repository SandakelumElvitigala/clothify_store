package com.elvo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedDate;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;


    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "employee_email", nullable = false)
    private String employeeEmail;
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;

    // Constructor for creating new orders
    public Order(LocalDateTime orderDate, Double totalAmount, String customerEmail, String employeeEmail, String status, List<OrderItems> orderItems) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customerEmail = customerEmail;
        this.employeeEmail = employeeEmail;
        this.status = status;
        this.orderItems = orderItems;
    }
}

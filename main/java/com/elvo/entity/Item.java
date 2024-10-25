package com.elvo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;  // Changed to Long

    @Column(name = "itemName")
    private String itemName;

    public enum Category {
        Ladies,
        Gents,
        Kids
    }

    public enum SubCategory {
        Tops,
        Skirts,
        Trousers,
        Frocks,
        Sarees,
        Others,
        Shirts,
        Tshirts
    }

    public enum Size {
        XS,
        S,
        M,
        L,
        XL,
        XXL
    }

    @Enumerated(EnumType.STRING) // Store as string
    @Column(name = "category")
    private Category category;

    @Enumerated(EnumType.STRING) // Store as string
    @Column(name = "subCategory")
    private SubCategory subCategory;

    @Enumerated(EnumType.STRING) // Store as string
    @Column(name = "size")
    private Size size;

    @Column(name = "introduced")
    private LocalDateTime introduced;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private Double price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "sold")
    private Integer sold;

    // Lifecycle callback to set the current date and time before the entity is persisted
    @PrePersist
    protected void onCreate() {
        this.introduced = LocalDateTime.now();
    }
}

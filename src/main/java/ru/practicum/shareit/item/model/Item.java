package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.io.Serializable;







@Data
@Entity
@Table
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @OneToOne
    private User owner;
    @OneToOne
    private ItemRequest request;


    public Item(String name, String description, Boolean available, User owner, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;

    }
}

package ru.practicum.shareit.item.coments.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.user.model.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Comments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Long itemId;
    @ManyToOne
    private User author;
    private LocalDateTime created;
}

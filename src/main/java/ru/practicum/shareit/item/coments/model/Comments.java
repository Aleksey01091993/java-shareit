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
@Table(name = "comments")
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Comments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "item_id")
    private Long itemId;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "created_time")
    private LocalDateTime created;
}

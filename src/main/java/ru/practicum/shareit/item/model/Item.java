package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.user.model.User;

import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name = "item")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "available")
    private Boolean available;
    @Transient
    private BookingToItem lastBooking;
    @Transient
    private BookingToItem nextBooking;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Comments> comments;

}

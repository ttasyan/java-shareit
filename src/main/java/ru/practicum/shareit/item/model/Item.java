package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private boolean available;
    @Column(name = "last_booking")
    private LocalDateTime lastBooking = LocalDateTime.now();
    @Column(name = "next_booking")
    private LocalDateTime nextBooking = LocalDateTime.now();
    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;

}

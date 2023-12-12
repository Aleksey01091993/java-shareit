package ru.practicum.shareit.item.storage.dao;


import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.storage.dal.ItemStorage;

public class ItemDbStorage {

    @Autowired
    private ItemStorage itemStorage;
}

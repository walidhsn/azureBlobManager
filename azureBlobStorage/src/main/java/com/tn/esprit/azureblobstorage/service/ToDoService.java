package com.tn.esprit.azureblobstorage.service;

import com.tn.esprit.azureblobstorage.entity.ToDo;

import java.util.List;
import java.util.Optional;

public interface ToDoService {
    List<ToDo> findAll();
    Optional<ToDo> findById(Long ID);
    ToDo create(ToDo toDo);
    ToDo update(ToDo toDo);
    void delete(ToDo toDo);
}

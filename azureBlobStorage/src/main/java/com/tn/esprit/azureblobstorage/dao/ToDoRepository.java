package com.tn.esprit.azureblobstorage.dao;

import com.tn.esprit.azureblobstorage.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Optional<ToDo> findByFileName(String fileName);
}

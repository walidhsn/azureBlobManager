package com.tn.esprit.azureblobstorage.serviceImpl;

import com.tn.esprit.azureblobstorage.dao.ToDoRepository;
import com.tn.esprit.azureblobstorage.entity.ToDo;
import com.tn.esprit.azureblobstorage.service.BlobService;
import com.tn.esprit.azureblobstorage.service.ToDoService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final BlobService blobService;
    @Autowired
    public ToDoServiceImpl(ToDoRepository toDoRepository,BlobService blobService) {
        this.toDoRepository = toDoRepository;
        this.blobService = blobService;
    }

    @Override
    public List<ToDo> findAll() {
        return toDoRepository.findAll();
    }

    @Override
    public Optional<ToDo> findById(Long ID) {
        return toDoRepository.findById(ID);
    }

    @Override
    public ToDo create(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    @Override
    public ToDo update(ToDo toDo) {
        Optional<ToDo> optionalToDo = toDoRepository.findById(toDo.getId());
        if(optionalToDo.isPresent()){
            ToDo updateTodo = optionalToDo.get();
            updateTodo.setFileUrl(toDo.getFileUrl());
            updateTodo.setFileName(toDo.getFileName());
            updateTodo.setDescription(toDo.getDescription());
            return toDoRepository.save(toDo);
        }
       return null;
    }

    @Override
    public void delete(ToDo toDo) {
        if(!toDo.getFileName().isEmpty()){
            blobService.deleteFile(toDo.getFileName());
        }
        toDoRepository.delete(toDo);
    }

}

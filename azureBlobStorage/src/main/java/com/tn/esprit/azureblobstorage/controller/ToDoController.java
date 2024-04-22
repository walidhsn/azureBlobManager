package com.tn.esprit.azureblobstorage.controller;

import com.tn.esprit.azureblobstorage.dao.ToDoRepository;
import com.tn.esprit.azureblobstorage.entity.ToDo;
import com.tn.esprit.azureblobstorage.entity.UploadedFile;
import com.tn.esprit.azureblobstorage.service.BlobService;
import com.tn.esprit.azureblobstorage.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToDoController {
    private final ToDoService toDoService;
    private final BlobService blobService;
    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoController(ToDoService toDoService,BlobService blobService,ToDoRepository toDoRepository) {
        this.toDoService = toDoService;
        this.blobService = blobService;
        this.toDoRepository = toDoRepository;
    }

    @GetMapping("/todo")
    public ResponseEntity<List<ToDo>> getAllToDos(){
        List<ToDo> toDos = toDoService.findAll();
        return ResponseEntity.ok(toDos);
    }
    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable("id") Long id){
        Optional<ToDo> toDo = toDoService.findById(id);
        return toDo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping(value = "/todo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ToDo> createToDo(@RequestPart("todo") ToDo toDo,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        UploadedFile information = new UploadedFile();
        if (file != null && !file.isEmpty()) {
            information = blobService.storeFile(file.getOriginalFilename(),file.getInputStream(), file.getSize());
            toDo.setFileName(information.getUniqueFileName());
            toDo.setFileUrl(information.getUrl());
        }
        ToDo result = toDoService.create(toDo);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping(value = "/todo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ToDo> updateToDo(@RequestPart("todo") ToDo toDo,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        UploadedFile information = new UploadedFile();
        if (file != null && !file.isEmpty()) {
            if(toDo.getFileName() == null && toDo.getFileUrl() == null){
                information = blobService.storeFile(file.getOriginalFilename(),file.getInputStream(), file.getSize());
            }else{
                information = blobService.updateFile(toDo.getFileName(),file.getOriginalFilename(),file.getInputStream(), file.getSize());
            }
            if(!information.getUniqueFileName().isEmpty() && !information.getUrl().isEmpty()){
                toDo.setFileName(information.getUniqueFileName());
                toDo.setFileUrl(information.getUrl());
            }
        }
        ToDo result = toDoService.update(toDo);
        if(result!=null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Map<String, String>> deleteToDo(@PathVariable("id") Long id){
        Optional<ToDo> toDelete = toDoService.findById(id);
        if(toDelete.isPresent()){
            toDoService.delete(toDelete.get());
            Map<String, String> response = new HashMap<>();
            response.put("message", "ToDo deleted successfully.");
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/todo/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        Optional<ToDo> optionalToDo = toDoRepository.findByFileName(fileName);
        if(optionalToDo.isPresent()){
            byte[] output = blobService.downloadFile(fileName).toByteArray();
            return ResponseEntity.ok(output);
        }
        return ResponseEntity.notFound().build();
    }
}

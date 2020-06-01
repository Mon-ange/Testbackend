package com.roborock.testbackend;

import com.roborock.testbackend.storage.Node;
import com.roborock.testbackend.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DirectoryController {
    @Autowired
    private final StorageService storageService;
    public DirectoryController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/browse")
    public Node browse() {
        return storageService.browse();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("upload request get");
        storageService.store(file);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

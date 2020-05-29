package com.roborock.testbackend;

import com.roborock.testbackend.storage.Node;
import com.roborock.testbackend.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryController {
    private final StorageService storageService;
    @Autowired
    public DirectoryController(StorageService storageService) {
        this.storageService = storageService;
    }
    @GetMapping("/browse")
    public Node browse(@RequestParam(name="capdir", required=true)String capdir, Model model) {
        return storageService.browse(capdir);
    }
}

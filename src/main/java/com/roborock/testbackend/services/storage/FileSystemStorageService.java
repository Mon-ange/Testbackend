package com.roborock.testbackend.services.storage;

import net.lingala.zip4j.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService{
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file, Path dir) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, dir.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        if (filename.endsWith("zip")) {
            String destination = dir.toString();
            String sourceFile = dir.resolve(filename).toString();
            System.out.println("destination = " + destination);
            System.out.println("sourceFile = " + sourceFile);
            ZipFile zipFile = new ZipFile(sourceFile);
            zipFile.extractAll(destination);
            Files.delete(dir.resolve(filename));
        }
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("storage rootLocation = " + this.rootLocation);
        System.out.println(filename);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            store(file, this.rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Node browse() {
        String uploadDir = this.rootLocation.getFileName().toString();
        Node root = new Node(uploadDir, uploadDir, "folder", "folder");
        File directory = this.rootLocation.toFile();
        walkFileTree(root, directory);
        return root;
    }
    private void walkFileTree(Node root, File directory) {
        for (File file: directory.listFiles()) {
            if (file.isFile()) {
                root.addChild(new Node(file.getPath(), file.getName(), "file", "file file-jpg"));
            } else if (file.isDirectory()) {
                Node subdir = new Node(file.getPath(), file.getName(), "folder", "folder");
                root.addChild(subdir);
                walkFileTree(subdir, file);
            }
        }
    }
}

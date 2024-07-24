package com.example.multipartstub.controller;

import com.example.multipartstub.domain.FileEntity;
import com.example.multipartstub.dto.FileDtoRequest;
import com.example.multipartstub.dto.FileUploadResponse;
import com.example.multipartstub.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @SneakyThrows
    @PostMapping("/uploadAll")
    public ResponseEntity<List<FileUploadResponse>> uploadFiles(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("metadata") List<FileDtoRequest> fileDtos
    ) {
        var responses = fileStorageService.saveFiles(files, fileDtos);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFileById(@PathVariable Long id) {
        var fileEntity = fileStorageService.getFileById(id);
        var resource = new ByteArrayResource(fileEntity.getData());
        return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(fileEntity.getFileName()))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
    }
}
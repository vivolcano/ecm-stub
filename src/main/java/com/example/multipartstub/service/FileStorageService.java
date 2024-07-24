package com.example.multipartstub.service;

import com.example.multipartstub.domain.FileEntity;
import com.example.multipartstub.dto.FileDtoRequest;
import com.example.multipartstub.dto.FileUploadResponse;
import com.example.multipartstub.repository.FileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileRepository fileRepository;

    @SneakyThrows
    public List<FileUploadResponse> saveFiles(List<MultipartFile> files, List<FileDtoRequest> metadata) {
        return files.stream()
                .map(file -> saveFile(file, metadata.get(files.indexOf(file))))
                .toList();
    }

    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }

    @SneakyThrows
    private FileUploadResponse saveFile(MultipartFile file, FileDtoRequest fileDto) {
        var fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setData(file.getBytes());
        fileEntity.setCorrelationId(fileDto.correlationId());
        fileEntity.setExternalId(fileDto.externalId());

        var savedFile = fileRepository.save(fileEntity);
        return new FileUploadResponse(savedFile.getId(), savedFile.getExternalId());
    }
}
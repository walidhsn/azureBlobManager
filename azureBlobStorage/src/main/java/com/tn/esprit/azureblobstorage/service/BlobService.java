package com.tn.esprit.azureblobstorage.service;

import com.tn.esprit.azureblobstorage.entity.UploadedFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public interface BlobService {
    UploadedFile storeFile(String fileName, InputStream content, Long length);
    UploadedFile updateFile(String existingFilename, String newFilename, InputStream content, Long length);
    List<String> listFiles();
    void deleteFile(String fileName);
    ByteArrayOutputStream downloadFile(String blobItem);
    String generateUniqueFileName(String originalFileName);
}

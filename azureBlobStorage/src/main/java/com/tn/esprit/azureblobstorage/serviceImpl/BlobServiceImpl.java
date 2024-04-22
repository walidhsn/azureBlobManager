package com.tn.esprit.azureblobstorage.serviceImpl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.tn.esprit.azureblobstorage.configuration.AzureProperties;
import com.tn.esprit.azureblobstorage.entity.ToDo;
import com.tn.esprit.azureblobstorage.entity.UploadedFile;
import com.tn.esprit.azureblobstorage.service.BlobService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlobServiceImpl implements BlobService {
    private final AzureProperties azureProperties;
    private BlobContainerClient containerClient(){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(azureProperties.getConnectionstring()).buildClient();
        BlobContainerClient container = serviceClient.getBlobContainerClient(azureProperties.getContainer());
        return container;
    }
    @Override
    public UploadedFile storeFile(String fileName, InputStream content, Long length) {
        String uniqueFileName = generateUniqueFileName(fileName);
        BlobClient client = containerClient().getBlobClient(uniqueFileName);
        client.upload(content, length);
        System.out.println("The file successfully uploaded to Azure Cloud");
        String fileUrl = client.getBlobUrl();
        return new UploadedFile(fileUrl,uniqueFileName); // Return the URL + unique name of the uploaded file
    }
    public UploadedFile updateFile(String existingFilename, String newFilename, InputStream content, Long length) {
        BlobClient existingClient = containerClient().getBlobClient(existingFilename);
        // Check if the existing file exists
        if (existingClient.exists()) {
            existingClient.delete(); // Delete the existing file
        } else {
            return null;
        }
        // Upload the new file
        String uniqueFileName = generateUniqueFileName(newFilename);
        BlobClient newClient = containerClient().getBlobClient(uniqueFileName);
        newClient.upload(content, length);
        System.out.println("The file successfully uploaded to Azure Cloud");
        String fileUrl = newClient.getBlobUrl();
        return new UploadedFile(fileUrl,uniqueFileName); // Return the URL + unique name of the updated file
    }

    @Override
    public List<String> listFiles() {
        BlobContainerClient container = containerClient();
        val list = new ArrayList<String>();
        for (BlobItem blobItem : container.listBlobs()) {
            list.add(blobItem.getName());
        }
        return list; //Return a List of names of the uploaded files
    }

    @Override
    public void deleteFile(String fileName) {
        BlobClient client = containerClient().getBlobClient(fileName);
        // Check if the existing file exists
        if (client.exists()) {
            client.delete(); // Delete the existing file
        }
    }

    @Override
    public ByteArrayOutputStream downloadFile(String blobItem) {
        BlobContainerClient containerClient = containerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobItem);
        if(blobClient.exists()){
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            blobClient.downloadStream(os);
            return os;
        }
        return null;
    }

    @Override
    public String generateUniqueFileName(String originalFileName) {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID + "_" + originalFileName;
    }
}

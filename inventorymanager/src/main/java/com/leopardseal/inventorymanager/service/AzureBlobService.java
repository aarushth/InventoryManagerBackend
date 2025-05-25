package com.leopardseal.inventorymanager.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlockBlobClient;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AzureBlobService {

    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    private BlobContainerClient containerClient;

    @PostConstruct
    public void init() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        containerClient = blobServiceClient.getBlobContainerClient(containerName);
    }

    public String uploadImageWithSas(Long id, String type) {
        BlockBlobClient blobClient = containerClient
                .getBlobClient(type + "_" + id + ".jpg")
                .getBlockBlobClient();

        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plus(15, ChronoUnit.MINUTES),
                BlobSasPermission.parse("rcw")
        ).setStartTime(OffsetDateTime.now().minusMinutes(5));

        String sasToken = blobClient.generateSas(sasValues);
        return blobClient.getBlobUrl() + "?" + sasToken;
    }
}

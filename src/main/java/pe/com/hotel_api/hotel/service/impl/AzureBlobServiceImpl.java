package pe.com.hotel_api.hotel.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.service.interfaces.AzureBlobService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AzureBlobServiceImpl implements AzureBlobService {
    private final BlobServiceClient blobServiceClient;
    private static final List<String> VALID_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    public static final long MAX_FILE_SIZE = (long) 5 * 1024 * 1024;

    @Value("${azure.storage.container.name}")
    private String nombreContenedor;

    @Override
    public String cargarImagen(MultipartFile file) throws IOException {
        if (file.isEmpty() || file == null) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        if (!isImageFile(file.getOriginalFilename())) {
            throw new IllegalArgumentException("El archivo no es una imagen valida");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido");
        }

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(nombreContenedor);
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        blobClient.upload(file.getResource().getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }

    private boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return VALID_EXTENSIONS.contains(extension.toLowerCase());
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        return baseName + "_" + System.currentTimeMillis() + "." + extension;
    }
}

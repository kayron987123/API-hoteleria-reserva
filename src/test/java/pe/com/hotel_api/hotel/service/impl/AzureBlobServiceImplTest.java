package pe.com.hotel_api.hotel.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AzureBlobServiceImplTest {

    @Mock
    private BlobServiceClient blobServiceClient;

    @Mock
    private BlobContainerClient containerClient;

    @Mock
    private BlobClient blobClient;

    @InjectMocks
    private AzureBlobServiceImpl azureBlobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(azureBlobService, "nombreContenedor", "testcontainer");
        when(blobServiceClient.getBlobContainerClient(anyString())).thenReturn(containerClient);
        when(containerClient.getBlobClient(anyString())).thenReturn(blobClient);
    }

    @Test
    void cargarImagen() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test Image Content".getBytes()
        );

        doNothing().when(blobClient).upload(any(), anyLong(), eq(true));
        when(blobClient.getBlobUrl()).thenReturn("https://mockstorage.blob.core.windows.net/container/test.jpg");

        String url = azureBlobService.cargarImagen(mockFile);

        assertNotNull(url);
        assertEquals("https://mockstorage.blob.core.windows.net/container/test.jpg", url);
        verify(blobServiceClient).getBlobContainerClient(anyString());
        verify(containerClient).getBlobClient(anyString());
        verify(blobClient, times(1)).upload(any(), anyLong(), eq(true));
    }

    @Test
    void cargarImagenInvalidExtension() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Invalid file".getBytes()
        );
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            azureBlobService.cargarImagen(mockFile);
        });

        assertEquals("El archivo no es una imagen valida", exception.getMessage());
    }

    @Test
    void cargarImagenEmptyFile() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "",
                "image/jpeg",
                new byte[0]
        );
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            azureBlobService.cargarImagen(mockFile);
        });

        assertEquals("El archivo no puede estar vacío", exception.getMessage());
    }

    @Test
    void cargarImagenFileTooLarge() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[(int) (AzureBlobServiceImpl.MAX_FILE_SIZE + 1)]
        );
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            azureBlobService.cargarImagen(mockFile);
        });

        assertEquals("El archivo excede el tamaño máximo permitido", exception.getMessage());
    }
}
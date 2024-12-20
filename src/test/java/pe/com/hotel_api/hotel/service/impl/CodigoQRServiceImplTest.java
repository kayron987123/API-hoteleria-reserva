package pe.com.hotel_api.hotel.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.google.zxing.WriterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodigoQRServiceImplTest {

    @Mock
    private BlobServiceClient blobServiceClient;

    @Mock
    private BlobClient blobClient;

    @Mock
    private BlobContainerClient containerClient;

    @InjectMocks
    private CodigoQRServiceImpl codigoQRService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(codigoQRService, "nombreContenedor", "testcontainer");
        when(blobServiceClient.getBlobContainerClient(anyString())).thenReturn(containerClient);
        when(containerClient.getBlobClient(anyString())).thenReturn(blobClient);
    }

    @Test
    void generarCodigoQR() throws IOException, WriterException {
        String codigoReserva = "R001232";
        String email = "test@gmail.com";
        LocalDateTime fechaEntrada = LocalDateTime.now();
        LocalDateTime fechaSalida = LocalDateTime.now().plusDays(2).plusHours(4);

        doNothing().when(blobClient).upload(any(), anyLong(), eq(true));
        when(blobClient.getBlobUrl()).thenReturn("https://mockstorage.blob.core.windows.net/container/codQR_2021-09-01T12:00:00_R001232.png");

        String result = codigoQRService.generarCodigoQR(codigoReserva, email, fechaEntrada, fechaSalida);

        assertNotNull(result);
        assertTrue(result.contains("codQR_"));
        assertTrue(result.endsWith(".png"));
        verify(blobClient, times(1)).upload(any(), anyLong(), eq(true));
    }
}
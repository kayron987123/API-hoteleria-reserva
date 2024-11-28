package pe.com.hotel_api.hotel.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.service.interfaces.CodigoQRService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CodigoQRServiceImpl implements CodigoQRService {
    private static final int WIDTH_QR = 300;
    private static final int HEIGHT_QR = 300;
    private static final String FORMAT_QR = "PNG";

    @Value("${azure.storage.container.name}")
    private String nombreContenedor;

    @Value("${azure.storage.connection.string}")
    private String cadenaConexion;

    @Override
    public String generarCodigoQR(String codigoReserva, String email, LocalDateTime fechaEntrada, LocalDateTime fechaSalida) throws WriterException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaEntradaFormateada = fechaEntrada.format(formatter);
        String fechaSalidaFormateada = fechaSalida.format(formatter);

        String qrData = String.format("Codigo Reserva: %s\nEmail: %s\nFecha de entrada: %s\nFecha de salida: %s",
                codigoReserva, email, fechaEntradaFormateada, fechaSalidaFormateada);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, WIDTH_QR, HEIGHT_QR);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, FORMAT_QR, pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(cadenaConexion)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(nombreContenedor);
        String fileName = "codQR_" + LocalDateTime.now() + "_" + codigoReserva + ".png";
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pngData)) {
            blobClient.upload(inputStream, pngData.length, true);
        }

        return blobClient.getBlobUrl();
    }
}

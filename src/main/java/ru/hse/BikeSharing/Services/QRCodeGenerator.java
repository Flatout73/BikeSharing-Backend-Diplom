package ru.hse.BikeSharing.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.hse.BikeSharing.domain.DBFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeGenerator {

    @Autowired
    DBFileStorageService storageService;

    public String generateQRCodeImage(String text, int width, int height, String imageName) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        byte[] pngData = new byte[10];
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);


            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            pngData = pngOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBFile dbFile = storageService.storeImage(pngData, imageName);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();


        return fileDownloadUri;
    }
}

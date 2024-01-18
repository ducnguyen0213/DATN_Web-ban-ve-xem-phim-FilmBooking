package com.example.filmBooking.service.impl.QrServiceImpl;

import com.example.filmBooking.repository.TicketRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GenerateQR {
    @Autowired
    private TicketRepository service;

    // Function to create the QR code
    public void createQR(String data, String path,
                         String charset, Map hashMap,
                         int height, int width)
            throws WriterException, IOException {

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToFile(
                matrix,
                path.substring(path.lastIndexOf('.') + 1),
                new File(path));
    }

    // Driver code
    public void driver(String idTicket) throws IOException, WriterException {

        // The data that the QR code will contain
        System.out.println(service.findById(idTicket).toString());
        String data = null;

        // The path where the image will get saved
//        String path = "demo.jpg";

        // Encoding charset
//        String charset = "UTF-8";

//        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
//                = new HashMap<EncodeHintType,
//                ErrorCorrectionLevel>();
//
//        hashMap.put(EncodeHintType.ERROR_CORRECTION,
//                ErrorCorrectionLevel.L);
//
//        // Create the QR code and save
//        // in the specified folder
//        // as a jpg file
//        createQR(data, path, charset, hashMap, 200, 200);
//        System.out.println(hashMap);
//        System.out.println("QR Code Generated!!! ");
    }
}

package zw.co.biometricwebcore.domain.service.impl;

import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.service.ConvertImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class ConvertImageServiceImpl implements ConvertImageService {

    @Override
    public String convertBufferedImageToBase64(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("BufferedImage is null! Ensure the image is properly captured or loaded.");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//    public String convertBufferedImageToBase64(BufferedImage image) {
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            ImageIO.write(image, "png", baos);
//            byte[] bytes = baos.toByteArray();
//            return Base64.getEncoder().encodeToString(bytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

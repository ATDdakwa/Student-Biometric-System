package zw.co.biometricwebcore.domain.model.scanner;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageStore {
    private List<byte[]> capturedImages = new ArrayList<>();
    private List<byte[]> savedImages = new ArrayList<>();
    private List<BufferedImage[]> savedBufferedImages = new ArrayList<>();
    private List<byte[]> templates = new ArrayList<>();
    BufferedImage bufferedImage;

    public void captureImage(byte[] image) {
        capturedImages.add(image);
    }

    public BufferedImage setCapturedBufferedImage(BufferedImage bufferedImage) {
        return bufferedImage;
    }

    public void saveImage(byte[] image) {
        savedImages.add(image);
    }

    public void saveBufferedImage(BufferedImage[] bufferedImage) {
        savedBufferedImages.add(bufferedImage);
    }

    public void addTemplate(byte[] template) {
        templates.add(template);
    }

    public List<byte[]> getTemplates() {
        return templates;
    }

    public List<byte[]> getCapturedImages() {
        return capturedImages;
    }

    public List<byte[]> getSavedImages() {
        return savedImages;
    }

}

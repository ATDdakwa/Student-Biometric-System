package zw.co.biometricwebcore.domain.service;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.enums.ImageFormat;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public interface ImageFileProcessService {

    public String saveImage(MorfinAuth morfinAuth, DeviceInfo deviceInfo, ScannedImage scannedImage, String imageFormat,
                            String templateFormat,String memberNumberAndSuffix, String option);


    public void writeImageFile(MorfinAuth morfinAuth, ImageFormat format, byte[] data);

    public void writeTemplateFile(MorfinAuth morfinAuth, String templateFormat,String memberNumberAndSuffix, String option);

    public void writeFile(MorfinAuth morfinAuth, String directory, String filename, byte[] data);

    public CompletableFuture<BufferedImage> getBitmapOnComplete(MorfinAuth morfinAuth, DeviceInfo deviceInfo);

    public CompletableFuture<ScannedImage> templateProcess(MorfinAuth morfinAuth, ScannedImage scannedImage,
                                                           String templateFormat, String memberNumberAndSuffix);

}

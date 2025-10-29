package zw.co.biometricwebcore.domain.service.impl;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;
import zw.co.biometricwebcore.domain.service.CaptureImageService;
import zw.co.biometricwebcore.domain.service.ImageFileProcessService;

import java.util.concurrent.CompletableFuture;

@Service
public class CaptureImageServiceImpl implements CaptureImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptureImageServiceImpl.class);

    private final ImageFileProcessService imageFileProcessService;
    private static final int MINI_QUALITY = 60;
    private static final int TIMEOUT = 0;
    private ScannedImage scannedImage;

    public CaptureImageServiceImpl(ImageFileProcessService imageFileProcessService) {

        this.imageFileProcessService = imageFileProcessService;
    }

    @Override
    public CompletableFuture<ScannedImage> startSyncCapture(MorfinAuth morfinAuth, DeviceInfo deviceInfo,
                                                            boolean captureStarted, boolean deviceInitialised) {
        CompletableFuture<ScannedImage> future = new CompletableFuture<>();

        new Thread(() -> {
            int minQuality = MINI_QUALITY;
            if (!isValidQuality(minQuality)) {
                logInvalidQuality();
                future.complete(scannedImage);
                return;
            }

            int timeout = TIMEOUT;
            if (!isValidTimeout(timeout)) {
                logInvalidTimeout();
                future.complete(scannedImage);
                return;
            }

            int[] quality = new int[1];
            int[] nfiq = new int[1];
            int ret = morfinAuth.AutoCapture(minQuality, timeout, quality, nfiq);
            if (ret != 0) {
                logError(ret, morfinAuth);
                future.complete(scannedImage);
                return;
            }

            logCaptureSuccess(quality[0], nfiq[0]);
            processImage(morfinAuth, deviceInfo);

            future.complete(scannedImage);
        }).start();

        return future;
    }

    private boolean isValidQuality(int quality) {
        return quality > 0 && quality <= 100;
    }

    private void logInvalidQuality() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Please enter valid quality");
        }
    }

    private boolean isValidTimeout(int timeout) {
        return timeout >= 0 || timeout < 1000;
    }

    private void logInvalidTimeout() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Please enter valid timeout");
        }
    }

    private void logError(int ret, MorfinAuth morfinAuth) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(morfinAuth.GetErrorMessage(ret));
        }
    }

    private void logCaptureSuccess(int quality, int nfiq) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Capture Success");
            LOGGER.info(String.format("Quality: %d, NFIQ: %d", quality, nfiq));
        }
    }

    private void processImage(MorfinAuth morfinAuth, DeviceInfo deviceInfo) {
        try {
            imageFileProcessService.getBitmapOnComplete(morfinAuth, deviceInfo);
//            imageFileProcessService.templateProcess(morfinAuth,scannedImage);
//            imageFileProcessService.saveImage(morfinAuth, deviceInfo);
        } catch (Exception ex) {
            ex.printStackTrace(); // Consider logging the exception instead
        }
    }

}

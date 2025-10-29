package zw.co.biometricwebcore.domain.service.impl;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.MorfinAuthNative;
import com.mantra.morfinauth.MorfinAuth_Callback;
import com.mantra.morfinauth.enums.DeviceDetection;
import com.mantra.morfinauth.enums.DeviceModel;
import com.mantra.morfinauth.enums.FingerPostion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.scanner.ImageStore;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;
import zw.co.biometricwebcore.domain.service.BiometricScannerService;
import zw.co.biometricwebcore.domain.service.CaptureImageService;
import zw.co.biometricwebcore.domain.service.ImageFileProcessService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BiometricScannerServiceImpl implements BiometricScannerService, MorfinAuth_Callback {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiometricScannerServiceImpl.class);

    private final MorfinAuth morfinAuth;
    private final ImageStore imageStore;
    private final ScannedImage scannedImage;
    private final CaptureImageService captureImageService;
    private final ImageFileProcessService imageFileProcessService;

    private List<String> connectedDevices = new ArrayList<>();
    private DeviceInfo deviceInfo;
    private boolean captureStarted;
    private boolean deviceInitialised;
    private static final String CONNECT_DEVICE_INSTRUCTION = "Please connect device";
    private static final String CAPTURE_ALREADY_STARTED = "Capture already started";
    private static final String IMAGE_PROCESSING_SUCCESS = "Image processed successfully";
    private static final String ERROR = "Error:";
    private CompletableFuture<ScannedImage> captureFuture;

    public BiometricScannerServiceImpl(ImageStore imageStore, CaptureImageService captureImageService,
                                       ImageFileProcessService imageFileProcessService, ScannedImage scannedImage) {
        this.morfinAuth = new MorfinAuth(this);
        this.deviceInfo = new DeviceInfo();
        this.imageStore = imageStore;
        this.scannedImage = scannedImage;
        this.captureImageService = captureImageService;
        this.imageFileProcessService = imageFileProcessService;
    }

    @Override
    public String getSDKVersion() {
        return morfinAuth.GetSDKVersion();
    }

    @Override
    public String checkDevice(String model) {
        if (connectedDevices.isEmpty()) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(CONNECT_DEVICE_INSTRUCTION);
            }
            return CONNECT_DEVICE_INSTRUCTION;
        }
        boolean isDeviceConnected = morfinAuth.IsDeviceConnected(DeviceModel.valueFor(model));
        LOGGER.debug("{} {}", model, isDeviceConnected ? " Device Connected" : " Device Not Connected");
        return (isDeviceConnected ? model + " Device Connected" : " Device Not Connected");
    }

    @Override
    public String initialiseDevice(String model) {

        if (deviceInitialised) {
            LOGGER.info("Device already initialised");
            return "Device already initialised";
        }

        if (connectedDevices.isEmpty()) {
            LOGGER.info(CONNECT_DEVICE_INSTRUCTION);
            return CONNECT_DEVICE_INSTRUCTION;
        }

        byte[] clientKey = "".getBytes();
        DeviceInfo info = new DeviceInfo();
        int ret = morfinAuth.Init(DeviceModel.valueFor(model), clientKey, info);

        if (ret != 0) {
            logError(ret);
            showDeviceInfo(null);
            return morfinAuth.GetErrorMessage(ret);
        }

        deviceInfo = info;
        LOGGER.info("DeviceInfo: {}", deviceInfo);
        deviceInitialised = true;
        LOGGER.info("Init Success");
        return showDeviceInfo(deviceInfo);
    }

    @Override
    public CompletableFuture<ScannedImage> startCapture() {
        CompletableFuture<ScannedImage> future = new CompletableFuture<>();
        if (captureStarted) {
            LOGGER.info(CAPTURE_ALREADY_STARTED);
            scannedImage.setMessage(CAPTURE_ALREADY_STARTED);
            future.complete(scannedImage);
            return future;
        }

        if (!isDeviceInitialized()) {
            scannedImage.setMessage("Device not initialized");
            future.complete(scannedImage);
            return future;
        }

        int minQuality = 60; // Set minimum quality
        captureStarted = true;
        LOGGER.info("To start capture ");
        int ret = morfinAuth.StartCapture(minQuality, 0);
        LOGGER.info("From start capture ");

        if (ret != 0) {
            captureStarted = false;
            logError(ret);
            scannedImage.setMessage(morfinAuth.GetErrorMessage(ret));
            future.complete(scannedImage);
            return future;
        }

        LOGGER.info("Capture in Progress...");
        scannedImage.setMessage("Capture in Progress");
        LOGGER.info("Scanned Image values: {}", scannedImage);

        // Store the future in a class variable if needed to complete later
        this.captureFuture = future; // Assuming you have a class variable for this
        return future;
    }

    @Override
    public void OnComplete(int errorCode, int quality, int nfiq) {
        captureStarted = false;

        if (errorCode != 0) {
            logError(errorCode);
            if (captureFuture != null) {
                scannedImage.setMessage("Error :" + errorCode);
                captureFuture.complete(scannedImage);
            }
            return;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Capture Success");
            LOGGER.info(String.format("Quality: %s, NFIQ: %s", quality, nfiq));
            scannedImage.setQuality(quality);
            scannedImage.setNfiq(nfiq);
        }

        if (deviceInitialised) {
            LOGGER.info("deviceInitialised");

            CompletableFuture<BufferedImage> future = imageFileProcessService.getBitmapOnComplete(morfinAuth,
                    deviceInfo);
            future.thenAccept(bufferedImage -> {
                LOGGER.info(IMAGE_PROCESSING_SUCCESS);
                scannedImage.setBufferedImage(bufferedImage);
                scannedImage.setMessage(IMAGE_PROCESSING_SUCCESS);
                if (captureFuture != null) {
                    captureFuture.complete(scannedImage);
                    imageFileProcessService.templateProcess(morfinAuth,scannedImage,"FMR_V2005","");
                }
            }).exceptionally(ex -> {
                LOGGER.info("Error: {}", ex.getMessage());
                if (captureFuture != null) {
                    captureFuture.completeExceptionally(ex);
                }
                return null;
            });

        }
    }
    @Override
    public String saveImageAndTemplate(String imageFormat, String templateFormat,String memberNumberAndSuffix, String option) {
        return imageFileProcessService.saveImage(morfinAuth, deviceInfo, scannedImage, imageFormat, templateFormat, memberNumberAndSuffix,option);
    }
    @Override
    public CompletableFuture<ScannedImage> autoCapture() {
        if (captureStarted) {
            LOGGER.info(CAPTURE_ALREADY_STARTED);
            scannedImage.setMessage(CAPTURE_ALREADY_STARTED);
            return CompletableFuture.completedFuture(scannedImage);
        }

        if (!isDeviceInitialized()) {
            scannedImage.setMessage("Device not initialised");
            return CompletableFuture.completedFuture(scannedImage);
        }

        captureStarted = true;
        return captureImageService.startSyncCapture(morfinAuth, deviceInfo, captureStarted, deviceInitialised)
                .thenApply(autoCapturedImage -> {
                    captureStarted = false;
                    return autoCapturedImage;
                });
    }

    @Override
    public String stopCapture() {
        if (!isDeviceInitialized()) {
            return "Device not initiliased";
        }

        captureStarted = false;
        int ret = morfinAuth.StopCapture();

        if (ret != 0) {
            logError(ret);
            return ERROR + ret;
        }

        LOGGER.info("Capture Stopped");
        return "Capture Stopped";
    }

    @Override
    public CompletableFuture<ScannedImage> matchFinger(String templateFormat, String memberNumberAndSuffix) {
        CompletableFuture<ScannedImage> future = new CompletableFuture<>();

        if (!isDeviceInitialized()) {
            scannedImage.setMessage("Device not initialized");
            future.complete(scannedImage);
            return future;
        }

        captureStarted = true;
        CompletableFuture<ScannedImage> captureFutureImage = captureImageService.startSyncCapture(morfinAuth,
                deviceInfo, captureStarted, deviceInitialised);
        LOGGER.info("captureFutureImage Image: {}", captureFutureImage);

        return captureFutureImage.thenCompose(capturedImage -> imageFileProcessService
                .getBitmapOnComplete(morfinAuth, deviceInfo).thenApply(bufferedImage -> {
                    scannedImage.setBufferedImage(bufferedImage);
                    scannedImage.setMessage(IMAGE_PROCESSING_SUCCESS);
                    return scannedImage;
                }).thenCompose(updatedImage -> {
                    LOGGER.info("Updated Image: {}", updatedImage);
                    return imageFileProcessService.templateProcess(morfinAuth, updatedImage, templateFormat,memberNumberAndSuffix);
                })).handle((result, ex) -> {
            if (ex != null) {
                scannedImage.setMessage("Error occurred: " + ex.getMessage());
                return scannedImage;
            }
            captureStarted = false;
            return result;
        });
    }


    private boolean isDeviceInitialized() {
        if (deviceInfo == null) {
            logError(MorfinAuthNative.DEVICE_NOT_INITIALIZED);
            return false;
        }
        return true;
    }

    @Override
    public String uninitialiseDevice() {
        int ret = morfinAuth.StopCapture();

        if (ret != 0) {
            logError(ret);
            return "Error " + ret;
        }

        sleepThread(500);
        deviceInitialised = false;
        captureStarted = false;
        ret = morfinAuth.Uninit();

        if (ret != 0 && ret != MorfinAuthNative.DEVICE_NOT_INITIALIZED) {
            logError(ret);
            return "Error " + ret;
        }
        resetValues();
        LOGGER.info("Uninit Success");
        return "Uninitialise Success";
    }

    private void resetValues() {
        deviceInfo = null;
        showDeviceInfo(null);
        scannedImage.setMessage(null);
        scannedImage.setBufferedImage(null);
        scannedImage.setBufferedImageBase64(null);
        scannedImage.setNfiq(0);
        scannedImage.setQuality(0);
    }

    private void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String showDeviceInfo(DeviceInfo info) {
        if (info != null) {
            String deviceInfor = String.format("Sr: %s, Make: %s, Model: %s, W: %s, H: %s, FW: %s", info.SerialNo,
                    info.Make, info.Model, info.Width, info.Height, info.Firmware);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(deviceInfor);
            }
            return (deviceInfor);
        } else {
            LOGGER.info("Device not found");
            return "Device not found";
        }
    }

    @Override
    public void addConnectedDevices() {
        List<String> deviceList = new ArrayList<>();
        int ret = morfinAuth.GetConnectedDevices(deviceList);
        if (ret != 0) {
            logError(ret);
        } else {
            connectedDevices.addAll(deviceList);
        }
    }

    @Override
    public void OnDeviceDetection(String deviceName, DeviceDetection detection) {
        if (detection == DeviceDetection.CONNECTED) {
            connectedDevices.add(deviceName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Device Name: [%s], Status: [ATTACHED]", deviceName));
                initialiseDevice("MFS500");
            }
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Device Name: [%s], Status: [DETACHED]", deviceName));
                stopCapture();
                uninitialiseDevice();
            }
            connectedDevices.remove(deviceName);
            if (deviceInfo != null && deviceInfo.Model.equals(deviceName)) {
                showDeviceInfo(null);
                deviceInfo = null;
            }
        }
    }

    @Override
    public void OnFingerPostionDetection(int errorCode, FingerPostion fingerPosition) {
        if (errorCode == 0) {
            switch (fingerPosition) {
                case FINGER_POSTION_OK -> LOGGER.info("Finger Position OK");
                case FINGER_POSTION_LEFT -> LOGGER.info("Finger Position Left");
                case FINGER_POSTION_RIGHT -> LOGGER.info("Finger Position Right");
                case FINGER_POSTION_TOP -> LOGGER.info("Finger Position Top");
                case FINGER_POSTION_NOT_IN_BOTTOM -> LOGGER.info("Finger Position Not In Bottom");
                case FINGER_POSTION_NOT_OK -> LOGGER.info("Finger Position Not OK");
            }
        } else {
            logError(errorCode);
        }
    }

    @Override
    public void OnPreview(int errorCode, int quality, final byte[] image) {
        if (errorCode != 0) {
            logError(errorCode);
            return;
        }

        LOGGER.info("Quality: {}", quality);
        new Thread(() -> {
            try (InputStream in = new ByteArrayInputStream(image)) {
                imageStore.captureImage(image); // Store captured image
                imageStore.setCapturedBufferedImage(ImageIO.read(in));
            } catch (IOException e) {
                LOGGER.info("Error processing preview: {}", e.getMessage());
            }
        }).start();
    }

    private void logError(int errorCode) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(morfinAuth.GetErrorMessage(errorCode));
        }
    }
}


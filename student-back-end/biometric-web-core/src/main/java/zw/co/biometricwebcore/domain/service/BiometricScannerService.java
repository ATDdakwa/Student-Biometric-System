package zw.co.biometricwebcore.domain.service;

import com.mantra.morfinauth.DeviceInfo;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;

import java.util.concurrent.CompletableFuture;

public interface BiometricScannerService {

    public String getSDKVersion();

    public String checkDevice(String model);

    public void addConnectedDevices();

    public String initialiseDevice(String model);

    public CompletableFuture<ScannedImage> startCapture();

    public CompletableFuture<ScannedImage> autoCapture();

    public String stopCapture();

    public CompletableFuture<ScannedImage> matchFinger(String templateFormat,String memberNumberAndSuffix);

    public String saveImageAndTemplate(String imageFormat, String templateFormat,String memberNumberAndSuffix, String option);

    public String uninitialiseDevice();

    public String showDeviceInfo(DeviceInfo info);




}

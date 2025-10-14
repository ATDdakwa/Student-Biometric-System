package zw.co.biometricwebcore.domain.service;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;

import java.util.concurrent.CompletableFuture;

public interface CaptureImageService {
    public CompletableFuture<ScannedImage> startSyncCapture(MorfinAuth morfinAuth, DeviceInfo deviceInfo, boolean captureStarted , boolean deviceInitialised);
}

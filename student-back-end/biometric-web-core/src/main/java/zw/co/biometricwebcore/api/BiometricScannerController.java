package zw.co.biometricwebcore.api;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;
import zw.co.biometricwebcore.domain.service.BiometricScannerService;
import zw.co.biometricwebcore.domain.service.ConvertImageService;
import zw.co.biometricwebcore.domain.service.ImageFileProcessService;
import zw.co.biometricwebcore.domain.service.impl.FingerPrintService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/biometric-scanner")
@RequiredArgsConstructor
public class BiometricScannerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiometricScannerController.class);

    private final BiometricScannerService biometricScannerService;
    private final ImageFileProcessService imageFileProcessService;
    private final ConvertImageService convertImageService;
    private final FingerPrintService fingerPrintService;

    @GetMapping("/get-sdk-version")
    public ResponseEntity<String> getSDKVersion() {

        try {
            LOGGER.info("getting SDK Version");
            String sdkVersion = biometricScannerService.getSDKVersion();
            LOGGER.info("SDK Version: {}", sdkVersion);
            if (sdkVersion != null) {
                return ResponseEntity.ok(sdkVersion);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SDK version not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/check-device/{model}")
    public ResponseEntity<String> checkDevice(@PathVariable String model) {
        LOGGER.info("check device: {}", model);
        return ResponseEntity.ok(biometricScannerService.checkDevice(model));
    }

    @PostMapping("/initialise-device/{model}")
    public ResponseEntity<String> initialiseDevice(@PathVariable String model) {
        LOGGER.info("initialise device: {}", model);
        return ResponseEntity.ok(biometricScannerService.initialiseDevice(model));
    }


    @PostMapping("/start-capture/{memberNumberAndSuffix}/{option}")
    public ResponseEntity<ScannedImage> startCaptureNew(@PathVariable String memberNumberAndSuffix, @PathVariable String option) {
        biometricScannerService.initialiseDevice("MFS500");
        CompletableFuture<ScannedImage> future = biometricScannerService.startCapture();
        LOGGER.info("Future in start capture: {}", future);

        try {

            ScannedImage scannedImage = future.get();
            scannedImage.setBufferedImageBase64(
                    convertImageService.convertBufferedImageToBase64(scannedImage.getBufferedImage()));

            LOGGER.info("Scanned Image in start capture: {}", scannedImage);
            var temp = biometricScannerService.saveImageAndTemplate("BMP","FMR_V2005",memberNumberAndSuffix,option);
            return ResponseEntity.ok(scannedImage);
        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException ");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (ExecutionException e) {
            LOGGER.info("ExecutionException ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/stop-capture")
    public ResponseEntity<String> stopCapture() {
        return ResponseEntity.ok(biometricScannerService.stopCapture());
    }

    @PostMapping("uninitialise-device")
    public ResponseEntity<String> unInitialiseDevice() {
        return ResponseEntity.ok(biometricScannerService.uninitialiseDevice());
    }

    @PostMapping("/match-finger-print/{templateFormat}/{memberNumberAndSuffix}")
    public ResponseEntity<ScannedImage> matchFingerPrint(@PathVariable String templateFormat, @PathVariable String memberNumberAndSuffix) {
        CompletableFuture<ScannedImage> future = biometricScannerService.matchFinger(templateFormat,memberNumberAndSuffix);
        LOGGER.info("Future in match finger print: {}", future);
        try {
            ScannedImage scannedImage = future.get();
            scannedImage.setBufferedImageBase64(
                    convertImageService.convertBufferedImageToBase64(scannedImage.getBufferedImage()));

            LOGGER.info("Scanned Image in match finger print: {}", scannedImage);
            return ResponseEntity.ok(scannedImage);
        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException ");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (ExecutionException e) {
            LOGGER.info("ExecutionException ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @PostMapping("/save-image-template")
//    public void saveImageAndTemplate(MorfinAuth morfinAuth, DeviceInfo deviceInfo) {
//        imageFileProcessService.saveImage(morfinAuth, deviceInfo);
//    }

    @PostMapping("/matchFingerprints")
    public String matchFingerprints(@RequestBody MatchRequest matchRequest) {

        PatientFingerPrint memberFingerPrints = fingerPrintService.getMemberFingerPrints("70014022-01");
        List<byte[]> templates = new ArrayList<>();
        System.out.println("memberFingerPrints");
        System.out.println(memberFingerPrints);
        System.out.println("id is ........."+memberFingerPrints.getId());
        System.out.println("templates size is ........."+templates.size());
        byte[] byteArrayFirst = Base64.getDecoder().decode(memberFingerPrints.getFirstFingerImage());
        byte[] byteArraySecond = Base64.getDecoder().decode(memberFingerPrints.getSecondFingerImage());
        byte[] byteArrayThird = Base64.getDecoder().decode(memberFingerPrints.getThirdFingerImage());
        templates.add(byteArrayFirst);
        templates.add(byteArraySecond);
        templates.add(byteArrayThird);

        String fingerprint1 = memberFingerPrints.getFirstFingerImage(); // Fingerprint data 1
        String fingerprint2 =memberFingerPrints.getThirdFingerImage(); // Fingerprint data 2

        // Calculate Levenshtein distance between the two fingerprints
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        double similarity = 1.0 - (double) levenshtein.apply(fingerprint1, fingerprint2) / Math.max(fingerprint1.length(), fingerprint2.length());

        // Set a threshold for similarity comparison
        double threshold = 0.8; // Adjust the threshold based on your requirements

        if (similarity >= threshold) {
            System.out.println("Fingerprints match.");
            return "Fingerprints match.";
        } else {
            System.out.println("Fingerprints do not match.");
            return "Fingerprints do not match.";
        }
    }

    static class MatchRequest {
        private byte[] fingerprint1;
        private byte[] fingerprint2;

        // getters and setters
    }

}

package zw.co.biometricwebcore.domain.service.impl;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.MorfinAuthNative;
import com.mantra.morfinauth.enums.ImageFormat;
import com.mantra.morfinauth.enums.TemplateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.BiometricRecord;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.model.scanner.ScannedImage;
import zw.co.biometricwebcore.domain.service.BiometricRecordService;
import zw.co.biometricwebcore.domain.service.ImageFileProcessService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ImageFileProcessServiceImpl implements ImageFileProcessService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileProcessServiceImpl.class);

	private boolean captureStarted = false;
	private byte[] lastCapturedTemplate = null;
	private static final String MATCH_NOT_FOUND = "Finger not matched";
	private final BiometricRecordService biometricRecordService;

	private final  FingerPrintService fingerPrintService;

	public ImageFileProcessServiceImpl(BiometricRecordService biometricRecordService, FingerPrintService fingerPrintService) {
		this.biometricRecordService = biometricRecordService;
		this.fingerPrintService = fingerPrintService;
	}

	@Override
	public String saveImage(MorfinAuth morfinAuth, DeviceInfo deviceInfo, ScannedImage scannedImage, String imageFormat,
							String templateFormat, String memberNumberAndSuffix, String option) {

		if (scannedImage.getBufferedImage() == null) {
			return "Captured finger not found";
		}

		if (deviceInfo == null) {
			logAndReturnError(morfinAuth, MorfinAuthNative.DEVICE_NOT_INITIALIZED);
		}

		if (captureStarted) {
			return "Saving in progress";
		}

		return processImage(morfinAuth, deviceInfo, imageFormat, templateFormat,memberNumberAndSuffix,option);
	}

	private void logAndReturnError(MorfinAuth morfinAuth, int errorCode) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(morfinAuth.GetErrorMessage(errorCode));
		}
		throw new IllegalArgumentException(morfinAuth.GetErrorMessage(errorCode));
	}

	private String processImage(MorfinAuth morfinAuth, DeviceInfo deviceInfo, String imageFormat,
			String templateFormat,String memberNumberAndSuffix, String option) {
		ImageFormat format = ImageFormat.valueOf(imageFormat);
		int[] dataLen = new int[] { deviceInfo.Width * deviceInfo.Height + 1100 };
		byte[] data = new byte[dataLen[0]];

		int ret = morfinAuth.GetImage(data, dataLen, 10, format);
		if (ret != 0) {
			logError(morfinAuth, ret);
			return "Error: " + morfinAuth.GetErrorMessage(ret);
		}

		lastCapturedTemplate = new byte[dataLen[0]];
		lastCapturedTemplate = Arrays.copyOf(data, dataLen[0]);

		writeImageFile(morfinAuth, format, data);
		writeTemplateFile(morfinAuth, templateFormat,memberNumberAndSuffix,option);
		LOGGER.info("Image & Template Saved");
		return "Image & Template Saved Successfully";
	}

	private void logError(MorfinAuth morfinAuth, int errorCode) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Error: {}", morfinAuth.GetErrorMessage(errorCode));
		}
	}

	@Override
	public void writeImageFile(MorfinAuth morfinAuth, ImageFormat format, byte[] data) {
		String filename = switch (format) {
		case RAW -> "Raw.raw";
		case BMP -> "Bitmap.bmp";
		case JPEG2000 -> "JPEG2000.jp2";
		case WSQ -> "WSQ.wsq";
		case FIR_V2005 -> "FIR_V2005.iso";
		case FIR_V2011 -> "FIR_V2011.iso";
		case FIR_WSQ_V2005 -> "FIR_WSQ_V2005.iso";
		case FIR_WSQ_V2011 -> "FIR_WSQ_V2011.iso";
		case FIR_JPEG2000_V2005 -> "FIR_JPEG2000_V2005.iso";
		case FIR_JPEG2000_V2011 -> "FIR_JPEG2000_V2011.iso";
		};

		writeFile(morfinAuth, "FingerData/Image", filename, data);
	}

	@Override
	public void writeTemplateFile(MorfinAuth morfinAuth, String templateFormat, String memberNumberAndSuffix, String option) {
		TemplateFormat tformat = TemplateFormat.valueOf(templateFormat);
		int[] dataLen = new int[] { 2500 };
		byte[] data = new byte[dataLen[0]];
		int ret = morfinAuth.GetTemplate(data, dataLen, tformat);

		if (ret != 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(morfinAuth.GetErrorMessage(ret));
			}
			return;
		}

		String filename = switch (tformat) {
		case FMR_V2005 -> "FMR_V2005.iso";
		case FMR_V2011 -> "FMR_V2011.iso";
		case ANSI_V378 -> "ANSI_V378.iso";
		};

		writeFile(morfinAuth, "FingerData/Template", filename, data);
		System.out.println("memberNumberAndSuffix....."+memberNumberAndSuffix);
		PatientFingerPrint memberFingerPrints = fingerPrintService.getMemberFingerPrints(memberNumberAndSuffix);

		System.out.println("Option value: " + option); // Debugging statement to check the value of option

		if (memberFingerPrints == null) {
			PatientFingerPrint fingerPrint = new PatientFingerPrint();
			fingerPrint.setPersonnelNumberPlusSuffix(memberNumberAndSuffix);

			if (option.equals("first")) {
				fingerPrint.setFirstFingerTemplate(data);
			} else if (option.equals("second")) {
				fingerPrint.setSecondFingerTemplate(data);
			} else if (option.equals("third")) {
				fingerPrint.setThirdFingerTemplate(data);
			}

			fingerPrintService.createFingerPrint(fingerPrint);
		} else {
			if (option.equals("first")) {
				memberFingerPrints.setFirstFingerTemplate(data);
			} else if (option.equals("second")) {
				memberFingerPrints.setSecondFingerTemplate(data);
			} else if (option.equals("third")) {
				memberFingerPrints.setThirdFingerTemplate(data);
			}

			fingerPrintService.createFingerPrint(memberFingerPrints); // Update the existing record
		}



		BiometricRecord biometricRecord = new BiometricRecord();
		biometricRecord.setTemplate(data);
		biometricRecordService.save(biometricRecord);//Save template to ImageStore
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Image templates saved so far: {}", biometricRecordService.findAll().size());
		}
	}

	@Override
	public void writeFile(MorfinAuth morfinAuth, String directory, String filename, byte[] data) {
		try {
			String path = System.getProperty("user.dir") + "//" + directory;
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File file = new File(path + "//" + filename);
			try (FileOutputStream stream = new FileOutputStream(file)) {
				stream.write(data);
			}
		} catch (IOException e) {
			LOGGER.info("Error writing file: {}", e.getMessage());
		}
	}

	@Override
	public CompletableFuture<ScannedImage> templateProcess(MorfinAuth morfinAuth, ScannedImage scannedImage,
			String templateFormat,String memberNumberAndSuffix) {
		CompletableFuture<ScannedImage> future = new CompletableFuture<>();

		if (!isCapturedTemplateAvailable()) {
			return completeFutureWithMessage(future, scannedImage, "No previously captured images to match");
		}

		LOGGER.info("lastCapturedTemplate: {}", lastCapturedTemplate);
		TemplateFormat tformat = TemplateFormat.valueOf(templateFormat);
		byte[] data = new byte[2500];

		int ret = morfinAuth.GetTemplate(data, new int[] { data.length }, tformat);
		if (ret != 0) {
			return completeFutureWithMessage(future, scannedImage, morfinAuth.GetErrorMessage(ret));
		}

		return matchTemplate(morfinAuth, scannedImage, tformat, data, future,memberNumberAndSuffix);
	}

	private CompletableFuture<ScannedImage> matchTemplate(MorfinAuth morfinAuth, ScannedImage scannedImage,
			TemplateFormat tformat, byte[] data, CompletableFuture<ScannedImage> future, String memberNumberAndSuffix) {
		lastCapturedTemplate = data.clone();
//		List<BiometricRecord> biometricRecords = biometricRecordService.findAll();
//		List<byte[]> existingTemplates = biometricRecords.stream().map(bio -> bio.getTemplate()).toList();

		System.out.println("memberNumberAndSuffix ......"+memberNumberAndSuffix);
		PatientFingerPrint memberFingerPrints = fingerPrintService.getMemberFingerPrints(memberNumberAndSuffix);

		List<byte[]> existingTemplates = new ArrayList<>();
		if(memberFingerPrints != null){
			System.out.println("I have data "+ memberFingerPrints.getPersonnelNumberPlusSuffix());
			existingTemplates.add(memberFingerPrints.getFirstFingerTemplate());
			existingTemplates.add(memberFingerPrints.getSecondFingerTemplate());
			existingTemplates.add(memberFingerPrints.getThirdFingerTemplate());
		}


		int minThreshold = 96;
		
		for (byte[] existingTemplate : existingTemplates) {
			
		    int[] matchScore = new int[1];
		    int ret = morfinAuth.MatchTemplate(existingTemplate, lastCapturedTemplate, matchScore, tformat);

		    if (ret < 0) {
		        return completeFutureWithMessage(future, scannedImage, morfinAuth.GetErrorMessage(ret));
		    }

		    if (matchScore[0] >= minThreshold) {
		        scannedImage.setMessage("Match found with match score of: " + matchScore[0]);
		        return completeFutureWithMatch(future, scannedImage, matchScore[0]);
		    }
		}

		LOGGER.info(MATCH_NOT_FOUND);
		scannedImage.setMessage(MATCH_NOT_FOUND);
		return completeFutureWithMessage(future, scannedImage, MATCH_NOT_FOUND);
	}

	private CompletableFuture<ScannedImage> completeFutureWithMessage(CompletableFuture<ScannedImage> future,
			ScannedImage scannedImage, String message) {
		LOGGER.info("completeFutureWithMessage: {}", message);
		scannedImage.setMessage(message);
		future.complete(scannedImage);
		return future;
	}

	private CompletableFuture<ScannedImage> completeFutureWithMatch(CompletableFuture<ScannedImage> future,
			ScannedImage scannedImage, int matchScore) {
		scannedImage.setMessage("Finger matched with score: " + matchScore);
		scannedImage.setMatchScore(matchScore);
		future.complete(scannedImage);
		return future;
	}

	@Override
	public CompletableFuture<BufferedImage> getBitmapOnComplete(MorfinAuth morfinAuth, DeviceInfo deviceInfo) {
		CompletableFuture<BufferedImage> future = new CompletableFuture<>();
		if (deviceInfo == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(morfinAuth.GetErrorMessage(MorfinAuthNative.DEVICE_NOT_INITIALIZED));
			}
			future.completeExceptionally(new Exception("Device not initialized"));
			return future;
		}
		int[] dataLen = new int[] { deviceInfo.Width * deviceInfo.Height + 1100 };
		byte[] data = new byte[dataLen[0]];
		int ret = morfinAuth.GetImage(data, dataLen, 1, ImageFormat.BMP);

		if (ret != 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(morfinAuth.GetErrorMessage(ret));
			}
			future.completeExceptionally(new Exception(morfinAuth.GetErrorMessage(ret)));
			return future;
		} else {
			new Thread(() -> {
				try (InputStream in = new ByteArrayInputStream(data)) {
					BufferedImage bufferedImage = ImageIO.read(in);
					future.complete(bufferedImage);
				} catch (IOException e) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Error processing bitmap: {}", e.getMessage());
						future.completeExceptionally(new Exception("Error processing bitmap: " + e.getMessage()));
					}
				}
			}).start();
			return future;
		}
	}

	private boolean isCapturedTemplateAvailable() {
		if (biometricRecordService.findAll().isEmpty()) {
			LOGGER.info("No previous template captured");
			return false;
		}
		return true;
	}

}

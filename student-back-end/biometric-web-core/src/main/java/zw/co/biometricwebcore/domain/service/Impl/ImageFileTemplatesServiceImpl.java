package zw.co.biometricwebcore.domain.service.impl;

import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.service.ImageFileTemplatesService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageFileTemplatesServiceImpl implements ImageFileTemplatesService {

	private final  FingerPrintService fingerPrintService;
	
	private final List<byte[]> templates;

	public ImageFileTemplatesServiceImpl(FingerPrintService fingerPrintService, List<byte[]> templates) {
		this.fingerPrintService = fingerPrintService;
		this.templates = templates;
	}

	@Override
	public void addTemplate(byte[] template) {
		templates.add(template);
	}
	
	@Override
	public List<byte[]> getTemplates() {
		PatientFingerPrint memberFingerPrints = fingerPrintService.getMemberFingerPrints("70013832-00");
		List<byte[]> templates = new ArrayList<>();
		System.out.println("memberFingerPrints");
		System.out.println(memberFingerPrints);
		System.out.println("id is ........."+memberFingerPrints.getId());
		List<String> fingers = new ArrayList<>();
		System.out.println("templates size is ........."+templates.size());
		byte[] byteArrayFirst = Base64.getDecoder().decode(memberFingerPrints.getFirstFingerImage());
		byte[] byteArraySecond = Base64.getDecoder().decode(memberFingerPrints.getSecondFingerImage());
		byte[] byteArrayThird = Base64.getDecoder().decode(memberFingerPrints.getThirdFingerImage());
		templates.add(byteArrayFirst);
		templates.add(byteArraySecond);
		templates.add(byteArrayThird);

		System.out.println("templates size is ........."+templates.size());
		return templates;
	}

}

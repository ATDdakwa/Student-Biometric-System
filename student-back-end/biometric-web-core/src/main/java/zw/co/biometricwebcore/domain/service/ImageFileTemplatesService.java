package zw.co.biometricwebcore.domain.service;

import java.util.List;

public interface ImageFileTemplatesService {

	public void addTemplate(byte[] template);

	public List<byte[]> getTemplates();

}

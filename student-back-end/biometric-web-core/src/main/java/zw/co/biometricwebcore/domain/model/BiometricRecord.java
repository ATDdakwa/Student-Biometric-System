package zw.co.biometricwebcore.domain.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Arrays;

@Entity
public class BiometricRecord {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Lob
	private byte[] template = new byte[2500];
	public byte[] getTemplate() {
		return template;
	}
	public void setTemplate(byte[] template) {
		this.template = template;
	}
	@Override
	public String toString() {
		return "BiometricRecord [id=" + id + ", template=" + Arrays.toString(template) + "]";
	}

}

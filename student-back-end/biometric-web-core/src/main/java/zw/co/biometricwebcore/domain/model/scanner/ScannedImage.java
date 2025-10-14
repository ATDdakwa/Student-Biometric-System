package zw.co.biometricwebcore.domain.model.scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ScannedImage {

    @JsonProperty("bufferedImage")
    private String bufferedImageBase64;

    private BufferedImage bufferedImage;
    private String message;
    private int quality;
    private int nfiq;
    private int  matchScore;

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getNfiq() {
        return nfiq;
    }

    public void setNfiq(int nfiq) {
        this.nfiq = nfiq;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImageBase64(String bufferedImageBase64) {
        this.bufferedImageBase64 = bufferedImageBase64;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String getBufferedImageBase64() {
        return bufferedImageBase64;
    }

    @Override
    public String toString() {
        return "ScannedImage [message=" + message + ", quality=" + quality + ", nfiq=" + nfiq + ", bufferedImage="
                + bufferedImage + "]";
    }

}


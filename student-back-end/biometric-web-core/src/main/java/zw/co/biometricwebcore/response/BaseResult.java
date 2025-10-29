package zw.co.biometricwebcore.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BaseResult<T> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private T data;
    private String description;
    private String code;

    public BaseResult(final String code, final String description) {
        this.data = null;
        this.description = description;
        this.code = code;
    }


    public BaseResult(final T data, final String description, final String code) {
        this.data = data;
        this.description = description;
        this.code = code;
    }


    public BaseResult(final T data, final String code) {
        this.data = data;
        this.description = code.equals("00") ? "success" : "fail";
        this.code = code;
    }

    public BaseResult() {
    }


    @JsonIgnore
    public BaseResult buildSuccessResult(final T data) {
        this.description = "Success";
        this.data = data;
        this.code = "00";
        return this;
    }


    @JsonIgnore
    public BaseResult buildSuccessResult(String description, final T data) {
        this.description = description;
        this.data = data;
        this.code = "00";
        return this;
    }

    @JsonIgnore
    public BaseResult buildErrorResult() {
        this.description = "fail";
        this.data = null;
        this.code = "99";
        return this;
    }

    @JsonIgnore
    public BaseResult buildErrorResult(String description) {
        this.description = description;
        this.data = null;
        this.code = "99";
        return this;
    }


}
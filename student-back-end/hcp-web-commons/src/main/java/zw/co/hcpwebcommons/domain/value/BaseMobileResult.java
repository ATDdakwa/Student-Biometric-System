package zw.co.hcpwebcommons.domain.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Jackson Tungamira <tungamirajackson@gmail.com>
 */

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BaseMobileResult<T> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private T data;
    private String description;
    private String code;

    public BaseMobileResult(final String code, final String description) {
        this.data = null;
        this.description = description;
        this.code = code;
    }


    public BaseMobileResult(final T data, final String description, final String code) {
        this.data = data;
        this.description = description;
        this.code = code;
    }


    public BaseMobileResult(final T data, final String code) {
        this.data = data;
        this.description = code.equals("00") ? "success" : "fail";
        this.code = code;
    }


    @JsonIgnore
    public BaseMobileResult buildSuccessResult(final T data) {
        this.description = "Success";
        this.data = data;
        this.code = "00";
        return this;
    }


    @JsonIgnore
    public BaseMobileResult buildSuccessResult(String description, final T data) {
        this.description = description;
        this.data = data;
        this.code = "00";
        return this;
    }

    @JsonIgnore
    public BaseMobileResult buildErrorResult() {
        this.description = "fail";
        this.data = null;
        this.code = "99";
        return this;
    }

    @JsonIgnore
    public BaseMobileResult buildErrorResult(String description) {
        this.description = description;
        this.data = null;
        this.code = "99";
        return this;
    }


}
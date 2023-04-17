package pog.pgp_alpha_v1.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author wangbaonan
 */
//@ApiModel(description = "Base response object")
@Data
public class BaseResponse<T> implements Serializable {
    //@ApiModelProperty(value = "Response status code", example = "200")
    private int code;
    //@ApiModelProperty(value = "Response data")
    private T data;
    //@ApiModelProperty(value = "Response status message", example = "Success")
    private String message;
    //@ApiModelProperty(value = "Response status description", example = "Success")
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}

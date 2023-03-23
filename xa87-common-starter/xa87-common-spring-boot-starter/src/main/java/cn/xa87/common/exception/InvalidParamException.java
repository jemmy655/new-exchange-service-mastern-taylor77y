package cn.xa87.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 说明：参数校验失败
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidParamException extends BaseException {

    private static final long serialVersionUID = 3076984003548588117L;

    private IError error = DefaultError.INVALID_PARAMETER;

    private String extMessage;

    public InvalidParamException(String message) {
        super(message);
        this.error.setErrorMessage(message);
    }

    public InvalidParamException(String message, String extMessage) {
        super(message + ":" + extMessage);
        this.error.setErrorMessage(message);
        this.extMessage = extMessage;
    }
}

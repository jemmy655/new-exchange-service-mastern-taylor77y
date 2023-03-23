package cn.xa87.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TokenException extends BaseException {

    private static final long serialVersionUID = 3076984003548588117L;

    private IError error = DefaultError.ACCESS_DENIED;

    private String extMessage;

    public TokenException(String message) {
        super(message);
        this.error.setErrorMessage(message);
    }

    public TokenException(String message, String extMessage) {
        super(message + ":" + extMessage);
        this.error.setErrorMessage(message);
        this.extMessage = extMessage;
    }
}

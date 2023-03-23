package cn.xa87.common.exception;

/**
 * 验证码异常
 */
public class ValidateCodeException extends BusinessException {

    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}

package cn.xa87.common.exception;


/**
 * 手机号码不合法
 */
public class IllegalMobileException extends BusinessException {

    private static final String MESSAGE = "手机号码不合法";

    private static final long serialVersionUID = 5433858278689152626L;

    public IllegalMobileException() {
        super(MESSAGE);
    }

    public IllegalMobileException(String message) {
        super(message);
    }
}

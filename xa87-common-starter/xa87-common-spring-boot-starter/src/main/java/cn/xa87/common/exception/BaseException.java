package cn.xa87.common.exception;

/**
 * 基本异常.
 */
class BaseException extends RuntimeException {

    private static final long serialVersionUID = -2088891163114521095L;

    BaseException() {
        super();
    }

    BaseException(String message) {
        super(message);
    }

    BaseException(Throwable cause) {
        super(cause);
    }

    BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

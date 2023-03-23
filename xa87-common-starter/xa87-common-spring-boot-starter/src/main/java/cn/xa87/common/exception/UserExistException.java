package cn.xa87.common.exception;


/**
 * 用户已存在
 */
public class UserExistException extends BusinessException {

    private static final long serialVersionUID = -6374172958979577560L;

//    private static final String MESSAGE = "用户已存在";
    private static final String MESSAGE = "user already exists";

    public UserExistException() {
        super(MESSAGE);
    }

    public UserExistException(String message) {
        super(message);
    }

}

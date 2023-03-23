package cn.xa87.common.exception;

/**
 * 说明：
 *
 * @author zhangwei
 * @version 2017/11/21 15:21.
 */
public interface IError {

    /**
     * 获取nameSpace
     *
     * @return nameSpace
     */
    String getNameSpace();

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    String getErrorCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getErrorMessage();

    /**
     * 设置错误信息
     *
     * @param errorMessage 错误信息
     */
    void setErrorMessage(String errorMessage);
}

package cn.xa87.member.common.util;

import cn.xa87.member.common.enums.ExceptionEnum;

/**
 * Created by peter
 * resultutil to form the result
 * 结果封装，统一输出结果以及异常
 */
public class ResultUtil {

    /**
     * 返回成功，传入返回体具体出參
     * @param object
     * @return
     */
    public static Result success(Object object){
        Result result = new Result();
        result.setCode(ExceptionEnum.SUCCESS.getCode());
        result.setMsg(ExceptionEnum.SUCCESS.getMsg());
        result.setData(object);
        result.setSuccess(true);
        return result;
    }

    /**
     * 提供给部分不需要出參的接口
     * @return
     */
    public static Result success(){
        return success(null);
    }

    /**
     * 自定义错误信息
     * @param msg
     * @param msg
     * @return
     */
    public static Result error(String msg){
        Result result = new Result();
        result.setMsg(msg);
        result.setData(null);
        result.setSuccess(false);
        return result;
    }
    /**
     * 返回异常信息，在已知的范围内
     * @param exceptionEnum
     * @return
     */
    public static Result error(ExceptionEnum exceptionEnum){
        Result result = new Result();
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());
        result.setSuccess(false);
        result.setData(null);
        return result;
    }
}

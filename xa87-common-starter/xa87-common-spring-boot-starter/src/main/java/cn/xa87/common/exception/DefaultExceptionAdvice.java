package cn.xa87.common.exception;

import cn.xa87.common.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@ControllerAdvice
@ResponseBody
public class DefaultExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class,})
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOGGER.error("参数解析失败", e);
        Response response = Response.failure(DefaultError.INVALID_PARAMETER);
        response.setResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("不支持当前请求方法", e);
        Response response = Response.failure(DefaultError.METHOD_NOT_SUPPORTED);
        response.setResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        LOGGER.error("不支持当前媒体类型", e);
        Response response = Response.failure(DefaultError.CONTENT_TYPE_NOT_SUPPORT);
        response.setResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public ResponseEntity handleSQLException(SQLException e) {
        LOGGER.error("服务运行SQLException异常", e);
        Response response = Response.failure(DefaultError.SQL_EXCEPTION);
        response.setResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 所有异常统一处理
     *
     * @return ResponseEntity
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        LOGGER.error("未知异常", ex);
        IError error;
        String extMessage = null;
        if (ex instanceof BindException) {
            error = DefaultError.INVALID_PARAMETER;
            List<ObjectError> errors = ((BindException) ex).getAllErrors();
            if (errors.size() != 0) {
                StringBuilder msg = new StringBuilder();
                for (ObjectError objectError : errors) {
                    msg.append("Field error in object '").append(objectError.getObjectName()).append(" ");
                    if (objectError instanceof FieldError) {
                        msg.append("on field ").append(((FieldError) objectError).getField()).append(" ");
                    }
                    msg.append(objectError.getDefaultMessage()).append(" ");
                }
                extMessage = msg.toString();
            }
        } else if (ex instanceof MissingServletRequestParameterException) {
            error = DefaultError.INVALID_PARAMETER;
            extMessage = ex.getMessage();
        } else if (ex instanceof ConstraintViolationException) {
            error = DefaultError.INVALID_PARAMETER;
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ex).getConstraintViolations();
            final StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<?> constraintViolation : violations) {
                msg.append(constraintViolation.getPropertyPath()).append(":").append(constraintViolation.getMessage()).append("\n");
            }
            extMessage = msg.toString();
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            error = DefaultError.CONTENT_TYPE_NOT_SUPPORT;
            extMessage = ex.getMessage();
        } else if (ex instanceof HttpMessageNotReadableException) {
            error = DefaultError.INVALID_PARAMETER;
            extMessage = ex.getMessage();
        } else if (ex instanceof MethodArgumentNotValidException) {
            error = DefaultError.INVALID_PARAMETER;
            final BindingResult result = ((MethodArgumentNotValidException) ex).getBindingResult();
            if (result.hasErrors()) {
                extMessage = result.getAllErrors().get(0).getDefaultMessage();
            } else {
                extMessage = ex.getMessage();
            }
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            error = DefaultError.METHOD_NOT_SUPPORTED;
            extMessage = ex.getMessage();
        } else if (ex instanceof UnexpectedTypeException) {
            error = DefaultError.INVALID_PARAMETER;
            extMessage = ex.getMessage();
        } else if (ex instanceof NoHandlerFoundException) {
            error = DefaultError.SERVICE_NOT_FOUND;
            extMessage = ex.getMessage();
        } else if (ex instanceof TokenException) {
            error = DefaultError.ACCESS_DENIED;
            extMessage = ex.getMessage();
        } else {
            error = DefaultError.SYSTEM_INTERNAL_ERROR;
            extMessage = ex.getMessage();
        }


        Response response = Response.failure(error);
        response.setResult(extMessage);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * BusinessException 业务异常处理
     *
     * @return ResponseEntity
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleException(BusinessException e) {
        LOGGER.error("业务异常", e);
        Response response = Response.failure(e.getError());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * InvalidParamException 参数校验异常
     *
     * @return ResponseEntity
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity handleException(InvalidParamException e) {
        LOGGER.error("参数验证失败", e);
        Response response = Response.failure(e.getError());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * ClientException 客户端异常 给调用者 app,移动端调用
     *
     * @return ResponseEntity
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ClientException.class)
    public ResponseEntity handleException(ClientException e) {
        Response response = Response.failure(DefaultError.CLIENT_EXCEPTION);
        response.setResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * ServerException 服务端异常, 微服务服务端产生的异常
     *
     * @return ResponseEntity
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServerException.class)
    public ResponseEntity handleException(ServerException e) {
        Response response = Response.failure(DefaultError.SERVER_EXCEPTION);
        response.setResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

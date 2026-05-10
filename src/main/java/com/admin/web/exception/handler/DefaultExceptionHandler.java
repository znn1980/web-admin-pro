package com.admin.web.exception.handler;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.exception.SysLicenseException;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.response.ServerResponse;
import com.admin.web.model.os.Os;
import com.admin.web.utils.ExceptionUtils;
import com.admin.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author znn
 */
@ControllerAdvice
public class DefaultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("SYS-ERROR => {} => URL => {}", request.getMethod(), request.getRequestURI());
        if (!(e instanceof ServerResponseException)) {
            logger.error("SYS-ERROR => {}", e, e);
        }
        if (WebUtils.hasRestRequest(request)) {
            if (e instanceof ServerResponseException ex) {
                return ResponseEntity.ok(ex.getServerResponse());
            }
            if (e instanceof SysLicenseException ex) {
                return ResponseEntity.ok(ServerResponse.fail(ex.getMessage()));
            }
            if (e instanceof ConstraintViolationException ex) {
                return ResponseEntity.ok(ServerResponse.fail(ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).collect(Collectors.joining(System.lineSeparator()))));
            }
            if (e instanceof BindException ex) {
                return ResponseEntity.ok(ServerResponse.fail(ex.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage).collect(Collectors.joining(System.lineSeparator()))));
            }
            if (e instanceof NoResourceFoundException ex) {
                return ResponseEntity.ok(ServerResponse.fail("访问的资源(%s)不存在！", ex.getResourcePath()));
            }
            if (e instanceof HttpRequestMethodNotSupportedException ex) {
                return ResponseEntity.ok(ServerResponse.fail("请求方法(%s)不支持！", ex.getMethod()));
            }
            if (e instanceof MissingServletRequestParameterException ex) {
                return ResponseEntity.ok(ServerResponse.fail("请求参数(%s)缺失！", ex.getParameterName()));
            }
            if (e instanceof MaxUploadSizeExceededException ex) {
                if (ExceptionUtils.getCause(e, SizeException.class) instanceof SizeException cause) {
                    return ResponseEntity.ok(ServerResponse.fail("上传文件(%s)超出(%s)限制！"
                            , Os.asBytes(cause.getActualSize()), Os.asBytes(cause.getPermittedSize())));
                }
                return ResponseEntity.ok(ServerResponse.fail("上传文件超出%s限制！", ex.getMaxUploadSize() >= 0L ?
                        String.format("(%s)", Os.asBytes(ex.getMaxUploadSize())) : ""));
            }
            if (Objects.nonNull(e.getMessage())) {
                return ResponseEntity.ok(ServerResponse.fail("%s(%s)", ResponseCode.SERVER_ERROR.msg(), e.getMessage()));
            }
            return ResponseEntity.ok(ServerResponse.fail(ResponseCode.SERVER_ERROR));
        }
        if (e instanceof ServerResponseException) {
            return new ModelAndView("error/403", HttpStatus.FORBIDDEN);
        }
        if (e instanceof NoResourceFoundException) {
            return new ModelAndView("error/404", HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("error/500", Map.of("error", ExceptionUtils.getStackTrace(e)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

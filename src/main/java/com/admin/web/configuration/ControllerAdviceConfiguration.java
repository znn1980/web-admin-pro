package com.admin.web.configuration;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.exception.SysLicenseException;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.os.Os;
import com.admin.web.utils.ExceptionUtils;
import com.admin.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

/**
 * @author znn
 */
@ControllerAdvice
public class ControllerAdviceConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAdviceConfiguration.class);

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("SYS-ERROR => {} => URL => {}", request.getMethod(), request.getRequestURI());
        if (!(e instanceof ServerResponseException || e instanceof SysLicenseException)) {
            logger.error("SYS-ERROR => {}", e, e);
        }
        if (WebUtils.isRequestRest(request)) {
            if (e instanceof ServerResponseException ex) {
                return ResponseEntity.ok(ex.getServerResponse());
            }
            if (e instanceof SysLicenseException ex) {
                return ResponseEntity.ok(ServerResponse.fail(ex.getMessage()));
            }
            if (e instanceof BindException ex) {
                return ResponseEntity.ok(ServerResponse.fail(ex.getBindingResult()));
            }
            if (e instanceof NoResourceFoundException ex) {
                return ResponseEntity.ok(ServerResponse.fail("访问的资源(%s)不存在！", ex.getResourcePath()));
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
                return ResponseEntity.ok(ServerResponse.fail("%s(%s)", ResponseCode.ERROR.msg(), e.getMessage()));
            }
            return ResponseEntity.ok(ServerResponse.fail(ResponseCode.ERROR));
        }
        if (e instanceof ServerResponseException) {
            return new ModelAndView("error/403", HttpStatus.FORBIDDEN);
        }
        if (e instanceof NoResourceFoundException) {
            return new ModelAndView("error/404", HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("error/500", "error", ExceptionUtils.getStackTrace(e));
    }
}

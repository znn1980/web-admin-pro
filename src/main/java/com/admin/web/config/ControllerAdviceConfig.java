package com.admin.web.config;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.os.Os;
import com.admin.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @author znn
 */
@ControllerAdvice
public class ControllerAdviceConfig {
    private static final Logger log = LoggerFactory.getLogger(ControllerAdviceConfig.class);

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("SYS-ERROR => {} => URL => {}", request.getMethod(), request.getRequestURI());
        if (!(e instanceof ServerResponseException)) {
            log.error("SYS-ERROR => {}", e, e);
        }
        if (WebUtils.isRequestRest(request)) {
            if (e instanceof ServerResponseException ex) {
                return ResponseEntity.ok(ex.getServerResponse());
            }
            if (e instanceof BindException ex) {
                return ResponseEntity.ok(ServerResponse.fail(ex.getBindingResult()));
            }
            if (e instanceof NoResourceFoundException ex) {
                return ResponseEntity.ok(ServerResponse.fail(String.format("访问的资源(%s)不存在！", ex.getResourcePath())));
            }
            if (e instanceof MaxUploadSizeExceededException ex) {
                if (e.getCause().getCause() instanceof FileSizeLimitExceededException exx) {
                    return ResponseEntity.ok(ServerResponse.fail(String.format("上传文件(%s)超出(%s)限制！"
                            , Os.asBytes(exx.getActualSize()), Os.asBytes(exx.getPermittedSize()))));
                }
                return ResponseEntity.ok(ServerResponse.fail(ex.getMaxUploadSize() <= 0
                        ? "上传文件超出限制！" : String.format("上传文件超出(%s)限制！", Os.asBytes(ex.getMaxUploadSize()))));
            }
            return ResponseEntity.ok(ServerResponse.fail(ResponseCode.ERROR));
        }
        if (e instanceof ServerResponseException) {
            return new ModelAndView("error/403");
        }
        if (e instanceof NoResourceFoundException) {
            return new ModelAndView("error/404");
        }
        return new ModelAndView("error/500");
    }
}

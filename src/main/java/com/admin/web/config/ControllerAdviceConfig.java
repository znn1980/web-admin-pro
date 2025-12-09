package com.admin.web.config;

import com.admin.web.WebServerException;
import com.admin.web.model.ResponseCode;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

/**
 * @author znn
 */
@ControllerAdvice
public class ControllerAdviceConfig {
    private static final Logger log = LoggerFactory.getLogger(ControllerAdviceConfig.class);

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("ERROR:[{}] URL:{} UA:{}", request.getMethod(), request.getRequestURI(), request.getHeader(HttpHeaders.USER_AGENT), e);
        if (WebUtils.isRequestRest(request)) {
            if (e instanceof WebServerException ex) {
                return ResponseEntity.ok(Objects.requireNonNullElse(ex.getServerResponseEntity()
                        , ServerResponseEntity.fail(ex.getCode(), ex.getMessage())));
            }
            if (e instanceof NoResourceFoundException) {
                return ResponseEntity.ok(ServerResponseEntity.fail(e.getMessage()));
            }
            if (e instanceof MethodArgumentNotValidException ex) {
                FieldError fieldError = ex.getBindingResult().getFieldError();
                return ResponseEntity.ok(ServerResponseEntity.fail(fieldError.getDefaultMessage()));
            }
            if (e instanceof BindException ex) {
                FieldError fieldError = ex.getBindingResult().getFieldError();
                return ResponseEntity.ok(ServerResponseEntity.fail(fieldError.getDefaultMessage()));
            }
            return ResponseEntity.ok(ServerResponseEntity.fail(ResponseCode.ERROR));
        }
        if (e instanceof WebServerException) {
            return new ModelAndView("error/403");
        }
        if (e instanceof NoResourceFoundException) {
            return new ModelAndView("error/404");
        }
        return new ModelAndView("error/5xx") {{
            this.addObject("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }};
    }


}

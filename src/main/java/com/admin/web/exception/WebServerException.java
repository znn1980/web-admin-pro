package com.admin.web.exception;

import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.enums.ResponseCode;

/**
 * @author znn
 */
public class WebServerException extends RuntimeException {
    private final String code;

    private final ServerResponseEntity<?> serverResponseEntity;

    public WebServerException(String code, String msg, ServerResponseEntity<?> serverResponseEntity) {
        super(msg);
        this.code = code;
        this.serverResponseEntity = serverResponseEntity;
    }

    public WebServerException(ServerResponseEntity<?> serverResponseEntity) {
        this(serverResponseEntity.getCode(), serverResponseEntity.getMsg(), serverResponseEntity);
    }

    public WebServerException(ResponseCode responseCode) {
        this(responseCode.value(), responseCode.msg(), null);
    }

    public String getCode() {
        return code;
    }

    public ServerResponseEntity<?> getServerResponseEntity() {
        return serverResponseEntity;
    }
}

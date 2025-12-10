package com.admin.web;

import com.admin.web.model.ResponseCode;
import com.admin.web.model.ServerResponseEntity;

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
        this(serverResponseEntity.getMsg(), serverResponseEntity.getMsg(), serverResponseEntity);
    }

    public WebServerException(ResponseCode responseCode) {
        this(responseCode.msg(), responseCode.value(), null);
    }

    public String getCode() {
        return code;
    }

    public ServerResponseEntity<?> getServerResponseEntity() {
        return serverResponseEntity;
    }
}

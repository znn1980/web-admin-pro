package com.admin.web.exception;

import com.admin.web.model.ServerResponse;
import com.admin.web.model.enums.ResponseCode;

/**
 * @author znn
 */
public class ServerResponseException extends RuntimeException {
    private final String code;

    private final ServerResponse<?> serverResponse;

    public ServerResponseException(String code, String msg, ServerResponse<?> serverResponse) {
        super(msg);
        this.code = code;
        this.serverResponse = serverResponse;
    }

    public ServerResponseException(ServerResponse<?> serverResponse) {
        this(serverResponse.getCode(), serverResponse.getMsg(), serverResponse);
    }

    public ServerResponseException(ResponseCode responseCode) {
        this(responseCode.value(), responseCode.msg(), null);
    }

    public String getCode() {
        return code;
    }

    public ServerResponse<?> getServerResponse() {
        return serverResponse;
    }
}

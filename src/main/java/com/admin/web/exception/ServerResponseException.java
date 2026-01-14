package com.admin.web.exception;

import com.admin.web.model.ServerResponse;
import com.admin.web.model.enums.ResponseCode;

/**
 * @author znn
 */
public class ServerResponseException extends RuntimeException {
    private final ServerResponse<?> serverResponse;

    public ServerResponseException(String msg) {
        this(ServerResponse.fail(msg));
    }

    public ServerResponseException(ResponseCode responseCode) {
        this(ServerResponse.fail(responseCode));
    }

    public ServerResponseException(ServerResponse<?> serverResponse) {
        super(serverResponse.getMsg());
        this.serverResponse = serverResponse;
    }

    public ServerResponse<?> getServerResponse() {
        return serverResponse;
    }
}

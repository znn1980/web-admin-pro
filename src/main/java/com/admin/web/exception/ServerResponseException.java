package com.admin.web.exception;

import com.admin.web.model.ServerResponse;
import com.admin.web.model.enums.ResponseCode;

/**
 * @author znn
 */
public class ServerResponseException extends RuntimeException {
    private final ServerResponse<?> serverResponse;

    public ServerResponseException(ServerResponse<?> serverResponse) {
        super(serverResponse.getMsg());
        this.serverResponse = serverResponse;
    }

    public ServerResponseException(ResponseCode responseCode) {
        super(responseCode.msg());
        this.serverResponse = ServerResponse.fail(responseCode);
    }

    public ServerResponse<?> getServerResponse() {
        return serverResponse;
    }
}

package com.mariapps.qdmswiki.serviceclasses;

/**
 * Created by elby.samson on 07,December,2018
 */

public class APIException extends RuntimeException {
    public int code;
    private String description;
    private String innerException;

    public APIException(int code, String detailMessage, String errors) {
        super(detailMessage);
        this.code = code;
        this.description = detailMessage;
        this.innerException = errors;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getInnerException() {
        return innerException;
    }
}

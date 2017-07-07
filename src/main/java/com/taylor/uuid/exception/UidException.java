package com.taylor.uuid.exception;

/**
 * @author Michael.Wang
 * @date 2017/4/25
 */
public class UidException extends RuntimeException {
    private static final long serialVersionUID = -6391301470442121996L;

    public UidException() {
        super();
    }

    public UidException(String msg) {
        super(msg);
    }

    public UidException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.taylor.uuid.exception;

/**
 * @author Michael.Wang
 * @date 2017/4/25
 */
public class UidSlotsEmptyException extends UidException {
    private static final long serialVersionUID = -2448554897642524663L;

    public UidSlotsEmptyException() {
    }

    public UidSlotsEmptyException(String msg) {
        super(msg);
    }

    public UidSlotsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}

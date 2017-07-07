package com.taylor.uuid.lock;

public interface Callback {

    Object onGetLock() throws InterruptedException;

    Object onTimeout() throws InterruptedException;
}

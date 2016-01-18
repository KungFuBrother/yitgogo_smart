package com.smartown.framework.mission;

public abstract class Mission implements Runnable {

    private boolean isCanceled = false;

    @Override
    public void run() {
        start();
        cancel();
    }

    public void cancel() {
        isCanceled = true;
    }

    public abstract void start();

    public boolean isCanceled() {
        return isCanceled;
    }

}

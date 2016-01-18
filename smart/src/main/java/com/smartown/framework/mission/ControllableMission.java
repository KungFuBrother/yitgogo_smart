package com.smartown.framework.mission;

public abstract class ControllableMission extends Mission {

    ControllableListener controllableListener;

    public void setControllableListener(ControllableListener controllableListener) {
        this.controllableListener = controllableListener;
    }

    @Override
    public void start() {
        if (isCanceled()) {
            return;
        }
        controllableListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_START, "PROGRESS_START"));
        doing();
        if (isCanceled()) {
            return;
        }
        controllableListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FINISH, "PROGRESS_FINISH"));
    }

    protected abstract void doing();

}

package com.smartown.framework.mission;

import android.util.Log;

public abstract class ControllableListener extends MissionListener {

    protected abstract void onStart();

    protected abstract void onFinish();

    @Override
    protected void onMissionStart() {
        Log.i("Request", "onMissionStart");
        onStart();
    }

    @Override
    protected void onMissionCache(MissionMessage missionMessage) {
    }

    @Override
    protected void onMissionFail(MissionMessage missionMessage) {
    }

    @Override
    protected void onMissionSuccess(MissionMessage missionMessage) {
    }

    @Override
    protected void onMissionFinish() {
        Log.i("Request", "onMissionFinish");
        onFinish();
    }
}

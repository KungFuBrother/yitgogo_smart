package smartown.controller.mission;

import yitgogo.smart.tools.LogUtil;

public abstract class RequestListener extends MissionListener {

    protected abstract void onStart();

    protected void onCache(RequestMessage requestMessage) {

    }

    protected abstract void onFail(MissionMessage missionMessage);

    protected abstract void onSuccess(RequestMessage requestMessage);

    protected abstract void onFinish();


    @Override
    protected void onMissionStart() {
        LogUtil.logInfo("Request", "onMissionStart");
        onStart();
    }

    @Override
    protected void onMissionCache(MissionMessage missionMessage) {
        LogUtil.logInfo("Request", "onMissionCache " + missionMessage);
        onCache((RequestMessage) missionMessage);
    }

    @Override
    protected void onMissionFail(MissionMessage missionMessage) {
        LogUtil.logInfo("Request", "onMissionFail " + missionMessage);
        onFail(missionMessage);
    }

    @Override
    protected void onMissionSuccess(MissionMessage missionMessage) {
        LogUtil.logInfo("Request", "onMissionSuccess " + missionMessage);
        onSuccess((RequestMessage) missionMessage);
    }

    @Override
    protected void onMissionFinish() {
        LogUtil.logInfo("Request", "onMissionFinish");
        onFinish();
    }
}

package yitgogo.smart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartown.yitgogo.smart.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.json.JSONException;
import org.json.JSONObject;

import yitgogo.smart.home.HomeFragment;
import yitgogo.smart.task.CommonTask;
import yitgogo.smart.tools.NetworkMissionMessage;
import yitgogo.smart.tools.OnNetworkListener;
import yitgogo.smart.tools.PackageTool;
import yitgogo.smart.view.NormalDialog;
import yitgogo.smart.view.OnDialogListener;
import yitgogo.smart.view.UpdateDialog;

public class EntranceActivity extends BaseActivity {

    LinearLayout loadingLayout;
    TextView loadingTextView, versionTextView;
    boolean disConnect = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        neverShowAds();
        setContentView(R.layout.fragment_entrance);
        findViews();
        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(EntranceActivity.class.getName());
        if (disConnect) {
            if (isConnected()) {
                disConnect = false;
                checkUpdate();
            } else {
                showMultiChoiceDialog("未连接网络，请检查网络设置！");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(EntranceActivity.class.getName());
    }

    @Override
    protected void findViews() {
        loadingLayout = (LinearLayout) findViewById(R.id.entrance_loading_layout);
        loadingTextView = (TextView) findViewById(R.id.entrance_loading_text);
        versionTextView = (TextView) findViewById(R.id.entrance_version_text);
        initViews();
    }

    @Override
    protected void initViews() {
        versionTextView.setText("版本:" + PackageTool.getVersionName() + "(" + PackageTool.getVersionCode() + ")");
    }

    private void showLoading(String text) {
        loadingLayout.setVisibility(View.VISIBLE);
        loadingTextView.setText(text);
    }

    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    private void activeSuccess() {
        jump(HomeFragment.class.getName(), "", null);
        finish();
    }

    /**
     * 检查网络连通性
     */
    private void checkConnection() {
        if (isConnected()) {
            //能访问网络，检查更新
            checkUpdate();
        } else {
            //不能访问网络
            disConnect = true;
        }
    }

    /**
     * 判断是否连接网络
     *
     * @return
     */
    protected boolean isConnected() {
        // TODO Auto-generated method stub
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (connectivityManager.getActiveNetworkInfo().isAvailable()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void checkUpdate() {
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                hideLoading();
                if (updateStatus == UpdateStatus.Yes) {
                    UpdateDialog updateDialog = UpdateDialog.newUpdateDialog(updateResponse, new OnDialogListener() {
                        @Override
                        public void onDismiss() {
                            getMachineState();
                        }
                    });
                    updateDialog.show(getSupportFragmentManager(), null);
                    return;
                }
                getMachineState();
            }
        });
        showLoading("正在检查更新...");
        UmengUpdateAgent.update(this);
    }

    private void getMachineState() {
        CommonTask.getMachineState(this, new OnNetworkListener() {
            @Override
            public void onStart() {
                super.onStart();
                showLoading("查询设备状态中，请稍候...");
            }

            @Override
            public void onSuccess(NetworkMissionMessage message) {
                super.onSuccess(message);
                if (TextUtils.isEmpty(message.getResult())) {
                    showMultiChoiceDialog("访问出错，请检查网络设置或联系客服！");
                } else {
                    try {
                        JSONObject object = new JSONObject(message.getResult());
                        if (object.optString("message").equals("stop")) {
                            showSingleChoiceDialog("此设备已被停用！");
                        } else if (object.optString("message").equals("noexit")) {
                            new MachineActiveFragment().show(getSupportFragmentManager(), null);
                        } else if (object.optString("message").equals("ok")) {
                            activeSuccess();
                        } else {
                            showMultiChoiceDialog("访问出错，请检查网络设置或联系客服！\n" + object.optString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(NetworkMissionMessage message) {
                super.onFail(message);
                showMultiChoiceDialog("访问出错，请检查网络设置或联系客服！\n" + message.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }

        });
    }

    private void showMultiChoiceDialog(String message) {
        NormalDialog multipleChoiceDialog = NormalDialog.newMultipleChoiceDialog(
                message,
                "检查网络设置",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                },
                "退出",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        multipleChoiceDialog.show(getSupportFragmentManager(), null);
    }

    private void showSingleChoiceDialog(String message) {
        NormalDialog singleChoiceDialog = NormalDialog.newSingleChoiceDialog(
                message,
                "退出",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        singleChoiceDialog.show(getSupportFragmentManager(), null);
    }

}

package yitgogo.smart.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartown.yitgogo.smart.R;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;

import java.io.File;

public class UpdateDialog extends DialogFragment {

    private LayoutInflater layoutInflater;
    private View dialogView;

    private TextView versionTextView, logTextView;

    private LinearLayout progressLayout;
    private TextView progressNumberTextView;
    private ProgressBar progressBar;

    private LinearLayout messageLayout;
    private TextView messageTextView;
    private Button messageButton;

    private UpdateResponse updateResponse;
    private OnDialogListener onDialogListener;

    private boolean firstTime = true;

    private File downloadedFile;

    public static UpdateDialog newUpdateDialog(UpdateResponse updateResponse, OnDialogListener onDialogListener) {
        UpdateDialog updateDialog = new UpdateDialog();
        updateDialog.setUpdateResponse(updateResponse);
        updateDialog.setOnDialogListener(onDialogListener);
        return updateDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        init();
        findViews();
    }

    private void init() {
        layoutInflater = LayoutInflater.from(getActivity());
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(R.color.divider);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView, new LayoutParams(480, LayoutParams.WRAP_CONTENT));
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadedFile = UmengUpdateAgent.downloadedFile(getActivity(), updateResponse);
        if (downloadedFile != null) {
            install();
        } else {
            download();
        }
        firstTime = false;
    }

    private void install() {
        progressLayout.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        messageTextView.setText("已下载最新版本，点击安装！");
        messageButton.setText("立即安装");
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengUpdateAgent.startInstall(getActivity(), downloadedFile);
            }
        });
        if (firstTime) {
            UmengUpdateAgent.startInstall(getActivity(), downloadedFile);
        }
    }

    private void download() {
        messageLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {
            @Override
            public void OnDownloadStart() {

            }

            @Override
            public void OnDownloadUpdate(int i) {
                progressNumberTextView.setText(i + "%");
                progressBar.setProgress(i);
            }

            @Override
            public void OnDownloadEnd(int i, String s) {
                downloadedFile = UmengUpdateAgent.downloadedFile(getActivity(), updateResponse);
                if (downloadedFile != null) {
                    install();
                } else {
                    downloadFailed();
                }
            }
        });
        UmengUpdateAgent.startDownload(getActivity(), updateResponse);
    }

    private void downloadFailed() {
        progressLayout.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        messageTextView.setText("下载失败，请重新下载！");
        messageButton.setText("重新下载");
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
    }

    private void findViews() {

        dialogView = layoutInflater.inflate(R.layout.dialog_update, null);

        versionTextView = (TextView) dialogView.findViewById(R.id.update_version);
        logTextView = (TextView) dialogView.findViewById(R.id.update_log);

        progressLayout = (LinearLayout) dialogView.findViewById(R.id.update_progress_layout);
        progressNumberTextView = (TextView) dialogView.findViewById(R.id.update_progress_number);
        progressBar = (ProgressBar) dialogView.findViewById(R.id.update_progress);

        messageLayout = (LinearLayout) dialogView.findViewById(R.id.update_message_layout);
        messageTextView = (TextView) dialogView.findViewById(R.id.update_message);
        messageButton = (Button) dialogView.findViewById(R.id.update_message_button);

        progressBar = (ProgressBar) dialogView.findViewById(R.id.update_progress);
        initViews();

    }

    private void initViews() {

        versionTextView.setText(updateResponse.version);
        logTextView.setText(updateResponse.updateLog);

    }

    public void setUpdateResponse(UpdateResponse updateResponse) {
        this.updateResponse = updateResponse;
    }

    public void setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDialogListener.onDismiss();
    }
}

package yitgogo.smart.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.smartown.yitgogo.smart.R;

public class NormalDialog extends DialogFragment {

    private LayoutInflater layoutInflater;
    private View dialogView;

    private TextView messageTextView;
    private Button negativeButton;
    private Button positiveButton;

    private String message = "", negativeButtonLable = "", positiveButtonLable = "";
    private View.OnClickListener negativeOnClickListener, positiveOnClickListener;

    public static NormalDialog newSingleChoiceDialog(String message, String positiveButtonLable, View.OnClickListener positiveOnClickListener) {
        NormalDialog normalDialog = new NormalDialog();
        normalDialog.setMessage(message);
        normalDialog.setPositiveButtonLable(positiveButtonLable);
        normalDialog.setPositiveOnClickListener(positiveOnClickListener);
        return normalDialog;
    }

    public static NormalDialog newMultipleChoiceDialog(String message, String positiveButtonLable, View.OnClickListener positiveOnClickListener, String negativeButtonLable, View.OnClickListener negativeOnClickListener) {
        NormalDialog normalDialog = new NormalDialog();
        normalDialog.setMessage(message);
        normalDialog.setPositiveButtonLable(positiveButtonLable);
        normalDialog.setPositiveOnClickListener(positiveOnClickListener);
        normalDialog.setNegativeButtonLable(negativeButtonLable);
        normalDialog.setNegativeOnClickListener(negativeOnClickListener);
        return normalDialog;
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
        dialog.setContentView(dialogView, new LayoutParams(540, LayoutParams.WRAP_CONTENT));
        return dialog;
    }

    private void findViews() {

        dialogView = layoutInflater.inflate(R.layout.dialog_normal, null);
        messageTextView = (TextView) dialogView.findViewById(R.id.single_choice_dialog_message);
        negativeButton = (Button) dialogView.findViewById(R.id.single_choice_dialog_button_negative);
        positiveButton = (Button) dialogView.findViewById(R.id.single_choice_dialog_button_positive);
        initViews();

    }

    private void initViews() {

        if (!TextUtils.isEmpty(message)) {
            messageTextView.setText(message);
        } else {
            messageTextView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(positiveButtonLable)) {
            positiveButton.setText(positiveButtonLable);
            positiveButton.setOnClickListener(positiveOnClickListener);
        } else {
            positiveButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(negativeButtonLable)) {
            negativeButton.setText(negativeButtonLable);
            negativeButton.setOnClickListener(negativeOnClickListener);
        } else {
            negativeButton.setVisibility(View.GONE);
        }

    }

    /**
     * 提示信息
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 蓝色按钮点击事件
     *
     * @param positiveOnClickListener
     */
    public void setPositiveOnClickListener(View.OnClickListener positiveOnClickListener) {
        this.positiveOnClickListener = positiveOnClickListener;
    }

    /**
     * 白色按钮点击事件
     *
     * @param negativeOnClickListener
     */
    public void setNegativeOnClickListener(View.OnClickListener negativeOnClickListener) {
        this.negativeOnClickListener = negativeOnClickListener;
    }

    /**
     * 蓝色按钮文字
     *
     * @param positiveButtonLable
     */
    public void setPositiveButtonLable(String positiveButtonLable) {
        this.positiveButtonLable = positiveButtonLable;
    }

    /**
     * 白色按钮文字
     *
     * @param negativeButtonLable
     */
    public void setNegativeButtonLable(String negativeButtonLable) {
        this.negativeButtonLable = negativeButtonLable;
    }
}

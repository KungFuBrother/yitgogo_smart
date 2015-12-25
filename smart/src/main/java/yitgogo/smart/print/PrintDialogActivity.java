package yitgogo.smart.print;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.smartown.yitgogo.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;

import yitgogo.smart.BaseActivity;

public class PrintDialogActivity extends BaseActivity {

    private TextView messageTextView;

    private TextView closeButton;

    private PrintData printData = new PrintData();

    private boolean isAlive = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (isAlive) {
                switch (msg.what) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        messageTextView.setText("连接打印机中...");
                        break;
                    case 2:
                        messageTextView.setText("正在打印订单...");
                        break;
                    case 3:
                        messageTextView.setText("打印成功");
                        break;
                    case 4:
                        messageTextView.setText("连接打印机失败");
                        break;
                    case 5:
                        messageTextView.setText("打印失败");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        isAlive = true;
        super.onCreate(arg0);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_dialog_print, null);
        LayoutParams layoutParams = new LayoutParams(360, LayoutParams.WRAP_CONTENT);
        setContentView(view, layoutParams);
        init();
        findViews();
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        super.onDestroy();
    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("message")) {
                String message = intent.getStringExtra("message");
                try {
                    printData = new PrintData(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void findViews() {
        messageTextView = (TextView) findViewById(R.id.print_message);
        closeButton = (TextView) findViewById(R.id.print_close);
        initViews();
        registerViews();
    }

    @Override
    protected void initViews() {
        new PrintTask().execute();
    }

    @Override
    protected void registerViews() {
        closeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class PrintTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... paramVarArgs) {
            Printer printer = new Printer(printData);
            try {
                handler.sendEmptyMessage(1);
                printer.connect();
                handler.sendEmptyMessage(2);
                printer.print();
                handler.sendEmptyMessage(3);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(4);
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(5);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

}

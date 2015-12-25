package yitgogo.smart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartown.yitgogo.smart.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yitgogo.smart.home.model.ModelAds;
import yitgogo.smart.home.model.ModelAdsImage;
import yitgogo.smart.task.HomeTask;
import yitgogo.smart.tools.NetworkMissionMessage;
import yitgogo.smart.tools.OnNetworkListener;

/**
 * Created by Tiger on 2015-10-29.
 */
public class AdsActivity extends FragmentActivity {

    private ImageView imageView;

    private List<ModelAdsImage> adsImages = new ArrayList<>();

    private int count = 0;

    private boolean isAlive = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (isAlive) {
                    ImageLoader.getInstance().displayImage(adsImages.get(count % adsImages.size()).getAdverImg(), imageView);
                    count++;
                    handler.sendEmptyMessageDelayed(0, 10000);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ads);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAlive = true;
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(AdsActivity.class.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAlive = false;
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(AdsActivity.class.getName());
    }

    private void findViews() {
        imageView = (ImageView) findViewById(R.id.ads_image);
        initViews();
    }

    private void initViews() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAds();
    }

    private void getAds() {
        HomeTask.getAds(this, new OnNetworkListener() {

            @Override
            public void onSuccess(NetworkMissionMessage message) {
                super.onSuccess(message);
                if (!TextUtils.isEmpty(message.getResult())) {
                    try {
                        JSONObject object = new JSONObject(message.getResult());
                        if (object.optString("state").equalsIgnoreCase("SUCCESS")) {
                            JSONArray array = object.optJSONArray("dataList");
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    ModelAds ad = new ModelAds(array.getJSONObject(i));
                                    if (ad.getAdverPosition().equals("全屏广告")) {
                                        if (ad.getAdsImages().isEmpty()) {
                                            if (!TextUtils.isEmpty(ad.getDefaultadver())) {
                                                adsImages.add(new ModelAdsImage(ad.getDefaultadver()));
                                            }
                                        } else {
                                            adsImages.addAll(ad.getAdsImages());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (adsImages.isEmpty()) {
                    imageView.setImageResource(R.drawable.ads);
                } else if (adsImages.size() == 1) {
                    ImageLoader.getInstance().displayImage(adsImages.get(0).getAdverImg(), imageView);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

}

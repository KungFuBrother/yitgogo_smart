package yitgogo.smart.suning.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.smartown.yitgogo.smart.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.smartown.framework.mission.MissionController;
import com.smartown.framework.mission.MissionMessage;
import com.smartown.framework.mission.Request;
import com.smartown.framework.mission.RequestListener;
import com.smartown.framework.mission.RequestMessage;
import yitgogo.smart.BaseNotifyFragment;
import yitgogo.smart.suning.model.GetNewSignature;
import yitgogo.smart.suning.model.ModelProductDetail;
import yitgogo.smart.suning.model.ModelProductImage;
import yitgogo.smart.suning.model.ModelProductPrice;
import yitgogo.smart.suning.model.SuningCarController;
import yitgogo.smart.suning.model.SuningManager;
import yitgogo.smart.tools.API;
import yitgogo.smart.tools.Parameters;
import yitgogo.smart.tools.ScreenUtil;
import yitgogo.smart.view.Notify;

public class ProductDetailFragment extends BaseNotifyFragment {

    ViewPager imagePager;
    TextView nameTextView, brandTextView, modelTextView, stateTextView, priceTextView;
    ImageView lastImageButton, nextImageButton;
    TextView imageIndexText;
    TextView carButton, buyButton;

    WebView webView;
    ProgressBar progressBar;

    ImageAdapter imageAdapter;

    ModelProductDetail productDetail = new ModelProductDetail();
    List<ModelProductImage> productImages = new ArrayList<>();
    ModelProductPrice productPrice = new ModelProductPrice();

    Bundle bundle = new Bundle();
    String skuId = "";

    String state = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_suning_product_detail);
        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        findViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(ProductDetailFragment.class.getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(ProductDetailFragment.class.getName());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProductDetail();
    }

    private void init() throws JSONException {
        measureScreen();
        bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("skuId")) {
                skuId = bundle.getString("skuId");
            }
        }
        imageAdapter = new ImageAdapter();
    }

    protected void findViews() {
        imagePager = (ViewPager) contentView
                .findViewById(R.id.product_detail_images);
        lastImageButton = (ImageView) contentView
                .findViewById(R.id.product_detail_image_last);
        nextImageButton = (ImageView) contentView
                .findViewById(R.id.product_detail_image_next);
        imageIndexText = (TextView) contentView
                .findViewById(R.id.product_detail_image_index);

        nameTextView = (TextView) contentView
                .findViewById(R.id.product_detail_name);
        brandTextView = (TextView) contentView
                .findViewById(R.id.product_detail_brand);
        modelTextView = (TextView) contentView
                .findViewById(R.id.product_detail_model);
        stateTextView = (TextView) contentView
                .findViewById(R.id.product_detail_state);

        priceTextView = (TextView) contentView
                .findViewById(R.id.product_detail_price);
        carButton = (TextView) contentView
                .findViewById(R.id.product_detail_car);
        buyButton = (TextView) contentView
                .findViewById(R.id.product_detail_buy);

        webView = (WebView) contentView.findViewById(R.id.web_webview);
        progressBar = (ProgressBar) contentView.findViewById(R.id.web_progress);

        initViews();
        registerViews();
    }

    protected void initViews() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                ScreenUtil.getScreenWidth() / 3);
        imagePager.setLayoutParams(layoutParams);
        imagePager.setAdapter(imageAdapter);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getActivity().getCacheDir().getPath());
        settings.setAppCacheEnabled(true);
    }

    private void showDetail() {
        nameTextView.setText(productDetail.getName());
        brandTextView.setText(productDetail.getBrand());
        modelTextView.setText(productDetail.getModel());
        webView.loadData(productDetail.getIntroduction(),
                "text/html; charset=utf-8", "utf-8");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void registerViews() {
        lastImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageAdapter.getCount() > 0) {
                    if (imagePager.getCurrentItem() == 0) {
                        setImagePosition(imageAdapter.getCount() - 1);
                    } else {
                        setImagePosition(imagePager.getCurrentItem() - 1);
                    }
                }
            }
        });
        nextImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageAdapter.getCount() > 0) {
                    if (imagePager.getCurrentItem() == imageAdapter.getCount() - 1) {
                        setImagePosition(0);
                    } else {
                        setImagePosition(imagePager.getCurrentItem() + 1);
                    }
                }
            }
        });
        imagePager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                imageIndexText.setText((imagePager.getCurrentItem() + 1) + "/"
                        + imageAdapter.getCount());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        carButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("00")) {
                    if (productPrice.getPrice() > 0) {
                        if (SuningCarController.addProduct(productDetail)) {
                            Notify.show("添加到购物车成功");
                        } else {
                            Notify.show("已添加过此商品");
                        }
                    } else {
                        Notify.show("商品信息有误，不能购买");
                    }
                } else {
                    Notify.show("此商品暂不能购买");
                }
            }
        });
        buyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("00")) {
                    if (productPrice.getPrice() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("product", productDetail.getJsonObject().toString());
                        openWindow(SuningProductBuyFragment.class.getName(), productDetail.getName(), bundle);
                    } else {
                        Notify.show("商品信息有误，不能购买");
                    }
                } else {
                    Notify.show("此商品暂不能购买");
                }
            }
        });
    }

    /**
     * 点击左右导航按钮切换图片
     *
     * @param imagePosition
     */
    private void setImagePosition(int imagePosition) {
        imagePager.setCurrentItem(imagePosition, true);
        imageIndexText.setText((imagePosition + 1) + "/"
                + imageAdapter.getCount());
    }

    /**
     * viewpager适配器
     */
    private class ImageAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return productImages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = layoutInflater.inflate(
                    R.layout.adapter_viewpager, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.view_pager_img);
            final ProgressBar spinner = (ProgressBar) imageLayout
                    .findViewById(R.id.view_pager_loading);
            ImageLoader.getInstance().displayImage(productImages.get(position).getPath(), imageView, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri,
                                              View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    private void getProductDetail() {
        Request request = new Request();
        request.setUrl(API.API_SUNING_PRODUCT_DETAIL);
        JSONObject data = new JSONObject();
        try {
            data.put("accessToken", SuningManager.getSignature().getToken());
            data.put("appKey", SuningManager.appKey);
            data.put("v", SuningManager.version);
            data.put("sku", skuId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addRequestParam("data", data.toString());
        MissionController.startRequestMission(getActivity(), request, new RequestListener() {
                    @Override
                    protected void onStart() {
                        showLoading();
                    }

                    @Override
                    protected void onFail(MissionMessage missionMessage) {

                    }

                    @Override
                    protected void onSuccess(RequestMessage requestMessage) {
                        if (!TextUtils.isEmpty(requestMessage.getResult())) {
                            if (SuningManager.isSignatureOutOfDate(requestMessage.getResult())) {
                                GetNewSignature getNewSignature = new GetNewSignature() {
                                    @Override
                                    protected void onPreExecute() {
                                        showLoading();
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean isSuccess) {
                                        hideLoading();
                                        if (isSuccess) {
                                            getProductDetail();
                                        }
                                    }
                                };
                                getNewSignature.execute();
                                return;
                            }
                            try {
                                JSONObject object = new JSONObject(requestMessage.getResult());
                                if (object.optBoolean("isSuccess")) {
                                    productDetail = new ModelProductDetail(object);
                                    showDetail();
                                    getProductImage();
                                    getProductPrice();
                                    getProductStock();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    protected void onFinish() {
                        hideLoading();
                    }
                }

        );
    }

    private void getProductImage() {
        Request request = new Request();
        request.setUrl(API.API_SUNING_PRODUCT_IMAGES);
        JSONArray dataArray = new JSONArray();
        dataArray.put(productDetail.getSku());
        JSONObject data = new JSONObject();
        try {
            data.put("accessToken", SuningManager.getSignature().getToken());
            data.put("appKey", SuningManager.appKey);
            data.put("v", SuningManager.version);
            data.put("sku", dataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addRequestParam("data", data.toString());
        MissionController.startRequestMission(getActivity(), request, new RequestListener() {
            @Override
            protected void onStart() {
                showLoading();
            }

            @Override
            protected void onFail(MissionMessage missionMessage) {

            }

            @Override
            protected void onSuccess(RequestMessage requestMessage) {
                if (!TextUtils.isEmpty(requestMessage.getResult())) {
                    if (SuningManager.isSignatureOutOfDate(requestMessage.getResult())) {
                        GetNewSignature getNewSignature = new GetNewSignature() {
                            @Override
                            protected void onPreExecute() {
                                showLoading();
                            }

                            @Override
                            protected void onPostExecute(Boolean isSuccess) {
                                hideLoading();
                                if (isSuccess) {
                                    getProductImage();
                                }
                            }
                        };
                        getNewSignature.execute();
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(requestMessage.getResult());
                        if (object.optBoolean("isSuccess")) {
                            JSONArray array = object.optJSONArray("result");
                            if (array != null) {
                                if (array.length() > 0) {
                                    JSONObject imageObject = array.optJSONObject(0);
                                    if (imageObject != null) {
                                        JSONArray imageArray = imageObject.optJSONArray("urls");
                                        if (imageArray != null) {
                                            for (int i = 0; i < imageArray.length(); i++) {
                                                productImages.add(new ModelProductImage(imageArray.optJSONObject(i)));
                                            }
                                            imageAdapter.notifyDataSetChanged();
                                            imageIndexText.setText("1/" + imageAdapter.getCount());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onFinish() {
                hideLoading();
            }
        });
    }

    private void getProductPrice() {
        Request request = new Request();
        request.setUrl(API.API_SUNING_PRODUCT_PRICE);

        JSONArray skuJsonArray = new JSONArray();
        skuJsonArray.put(skuId);

        JSONObject data = new JSONObject();
        try {
            data.put("accessToken", SuningManager.getSignature().getToken());
            data.put("appKey", SuningManager.appKey);
            data.put("v", SuningManager.version);
            data.put("cityId", SuningManager.getSuningAreas().getCity().getCode());
            data.put("sku", skuJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addRequestParam("data", data.toString());

        MissionController.startRequestMission(getActivity(), request, new RequestListener() {
            @Override
            protected void onStart() {
                showLoading();
            }

            @Override
            protected void onFail(MissionMessage missionMessage) {
                Notify.show("查询价格失败");
            }

            @Override
            protected void onSuccess(RequestMessage requestMessage) {
                if (!TextUtils.isEmpty(requestMessage.getResult())) {
                    if (SuningManager.isSignatureOutOfDate(requestMessage.getResult())) {
                        GetNewSignature getNewSignature = new GetNewSignature() {
                            @Override
                            protected void onPreExecute() {
                                showLoading();
                            }

                            @Override
                            protected void onPostExecute(Boolean isSuccess) {
                                hideLoading();
                                if (isSuccess) {
                                    getProductImage();
                                }
                            }
                        };
                        getNewSignature.execute();
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(requestMessage.getResult());
                        if (object.optBoolean("isSuccess")) {
                            JSONArray array = object.optJSONArray("result");
                            if (array != null) {
                                if (array.length() > 0) {
                                    productPrice = new ModelProductPrice(array.optJSONObject(0));
                                    priceTextView.setText(Parameters.CONSTANT_RMB + decimalFormat.format(productPrice.getPrice()));
                                    return;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Notify.show("查询价格失败");
            }

            @Override
            protected void onFinish() {
                hideLoading();
            }
        });
    }

    private void getProductStock() {
        final Request request = new Request();
        request.setUrl(API.API_SUNING_PRODUCT_STOCK);
        JSONObject data = new JSONObject();
        try {
            data.put("accessToken", SuningManager.getSignature().getToken());
            data.put("appKey", SuningManager.appKey);
            data.put("v", SuningManager.version);
            data.put("cityId", SuningManager.getSuningAreas().getCity().getCode());
            data.put("countyId", SuningManager.getSuningAreas().getDistrict().getCode());
            data.put("sku", productDetail.getSku());
            data.put("num", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addRequestParam("data", data.toString());
        MissionController.startRequestMission(getActivity(), request, new RequestListener() {
            @Override
            protected void onStart() {
                showLoading();
            }

            @Override
            protected void onFail(MissionMessage missionMessage) {

            }

            @Override
            protected void onSuccess(RequestMessage requestMessage) {
                if (!TextUtils.isEmpty(requestMessage.getResult())) {
                    if (SuningManager.isSignatureOutOfDate(requestMessage.getResult())) {
                        GetNewSignature getNewSignature = new GetNewSignature() {

                            @Override
                            protected void onPreExecute() {
                                showLoading();
                            }

                            @Override
                            protected void onPostExecute(Boolean isSuccess) {
                                hideLoading();
                                if (isSuccess) {
                                    getProductStock();
                                }
                            }
                        };
                        getNewSignature.execute();
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(requestMessage.getResult());
                        if (object.optBoolean("isSuccess")) {
                            state = object.optString("state");
                            if (state.equals("00")) {
                                stateTextView.setText("有货");
                            } else if (state.equals("01")) {
                                stateTextView.setText("暂不销售");
                            } else {
                                stateTextView.setText("无货");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onFinish() {
                hideLoading();
            }
        });
    }

}

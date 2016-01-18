package yitgogo.smart.suning.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartown.yitgogo.smart.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartown.framework.mission.MissionController;
import com.smartown.framework.mission.MissionMessage;
import com.smartown.framework.mission.Request;
import com.smartown.framework.mission.RequestListener;
import com.smartown.framework.mission.RequestMessage;
import yitgogo.smart.BaseNotifyFragment;
import yitgogo.smart.suning.model.GetNewSignature;
import yitgogo.smart.suning.model.ModelProduct;
import yitgogo.smart.suning.model.ModelProductClass;
import yitgogo.smart.suning.model.ModelProductPrice;
import yitgogo.smart.suning.model.SuningManager;
import yitgogo.smart.tools.API;
import yitgogo.smart.tools.Parameters;
import yitgogo.smart.view.InnerGridView;
import yitgogo.smart.view.Notify;


public class SuningProductFragment extends BaseNotifyFragment {

    PullToRefreshScrollView refreshScrollView;
    InnerGridView productGridView;

    TextView productAreaTextView;
    LinearLayout productAreaSelectButton;

    ModelProductClass productClass = new ModelProductClass();

    List<ModelProduct> products = new ArrayList<>();
    HashMap<String, ModelProductPrice> priceHashMap = new HashMap<>();

    ProductAdapter productAdapter;

    SuningClassesFragment classesFragment = new SuningClassesFragment() {

        @Override
        public void onClassSelected(ModelProductClass selectedProductClass) {
            if (productClass == selectedProductClass) return;
            productClass = selectedProductClass;
            refresh();
        }

    };

    int pagesize = 12, pagenum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_suning_product_list);
        init();
        findViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(SuningProductFragment.class.getName());
        if (!TextUtils.isEmpty(SuningManager.getSuningAreas().getTown().getCode())) {
            showSuningAreas();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(SuningProductFragment.class.getName());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(SuningManager.getSuningAreas().getTown().getCode())) {
            SuningAreaDialog suningAreaDialog = new SuningAreaDialog() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    super.onDismiss(dialog);
                    if (TextUtils.isEmpty(SuningManager.getSuningAreas().getTown().getCode())) {
                        getActivity().finish();
                    } else {
                        showSuningAreas();
                        getFragmentManager().beginTransaction().replace(R.id.suning_product_list_class, classesFragment).commit();
                    }
                }
            };
            suningAreaDialog.show(getFragmentManager(), null);
        } else {
            showSuningAreas();
            getFragmentManager().beginTransaction().replace(R.id.suning_product_list_class, classesFragment).commit();
        }
    }

    private void init() {
        measureScreen();
        productAdapter = new ProductAdapter();
    }

    @Override
    protected void findViews() {
        refreshScrollView = (PullToRefreshScrollView) contentView
                .findViewById(R.id.suning_product_list_scroll);
        productGridView = (InnerGridView) contentView
                .findViewById(R.id.suning_product_list);
        productAreaTextView = (TextView) contentView
                .findViewById(R.id.suning_product_area);
        productAreaSelectButton = (LinearLayout) contentView
                .findViewById(R.id.suning_product_area_select);
        initViews();
        registerViews();
    }

    @Override
    protected void initViews() {
        refreshScrollView.setMode(Mode.BOTH);
        productGridView.setAdapter(productAdapter);
    }

    @Override
    protected void registerViews() {
        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getSuningProducts();
            }
        });
        productGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ModelProduct product = products.get(arg2);
                if (priceHashMap.containsKey(product.getSku())) {
                    if (priceHashMap.get(product.getSku()).getPrice() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("skuId", product.getSku());
                        openWindow(ProductDetailFragment.class.getName(), product.getName(), bundle);
                    } else {
                        Notify.show("此商品暂未设置价格");
                    }
                } else {
                    Notify.show("此商品暂未设置价格");
                }
            }
        });
        productAreaSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuningAreaDialog suningAreaDialog = new SuningAreaDialog() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        super.onDismiss(dialog);
                        if (TextUtils.isEmpty(SuningManager.getSuningAreas().getTown().getCode())) {
                            getActivity().finish();
                        } else {
                            showSuningAreas();
                            new GetSuningProvince().execute();
                        }
                    }
                };
                suningAreaDialog.show(getFragmentManager(), null);
            }
        });
    }

    private void refresh() {
        pagenum = 0;
        products.clear();
        priceHashMap.clear();
        productAdapter.notifyDataSetChanged();
        getSuningProducts();
    }

    private void getSuningProducts() {
        pagenum++;
        Request request = new Request();
        request.setUrl(API.API_SUNING_PRODUCT_LIST);
        request.addRequestParam("classId", productClass.getId());
        request.addRequestParam("pagenum", String.valueOf(pagenum));
        request.addRequestParam("pagesize", String.valueOf(pagesize));
        MissionController.startRequestMission(getActivity(), request, new RequestListener() {
            @Override
            protected void onStart() {
                showLoading();
            }

            @Override
            protected void onFail(MissionMessage missionMessage) {
                loadingEmpty("获取商品数据失败");
            }

            @Override
            protected void onSuccess(RequestMessage requestMessage) {
                if (!TextUtils.isEmpty(requestMessage.getResult())) {
                    try {
                        JSONObject object = new JSONObject(requestMessage.getResult());
                        if (object.optString("state").equalsIgnoreCase("SUCCESS")) {
                            JSONArray array = object.optJSONArray("dataList");
                            if (array != null) {
                                if (array.length() < pagesize) {
                                    refreshScrollView.setMode(Mode.PULL_FROM_START);
                                }
                                JSONArray priceJsonArray = new JSONArray();
                                for (int i = 0; i < array.length(); i++) {
                                    ModelProduct product = new ModelProduct(array.optJSONObject(i));
                                    products.add(product);
                                    priceJsonArray.put(product.getSku());
                                }
                                if (products.isEmpty()) {
                                    loadingEmpty();
                                } else {
                                    productAdapter.notifyDataSetChanged();
                                    if (priceJsonArray.length() > 0) {
                                        getSuningProductPrice(priceJsonArray);
                                    }
                                }
                            }
                            return;
                        }
                        Notify.show(object.optString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (products.isEmpty()) {
                    loadingEmpty();
                }
            }

            @Override
            protected void onFinish() {
                hideLoading();
                refreshScrollView.onRefreshComplete();
            }
        });
    }

    private void getSuningProductPrice(final JSONArray priceJsonArray) {
        Request request = new Request();
        request.setUrl(API.API_SUNING_PRODUCT_PRICE);

        JSONObject data = new JSONObject();
        try {
            data.put("accessToken", SuningManager.getSignature().getToken());
            data.put("appKey", SuningManager.appKey);
            data.put("v", SuningManager.version);
            data.put("cityId", SuningManager.getSuningAreas().getCity().getCode());
            data.put("sku", priceJsonArray);
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
                                getSuningProductPrice(priceJsonArray);
                            }
                        }
                    };
                    getNewSignature.execute();
                    return;
                }
                if (!TextUtils.isEmpty(requestMessage.getResult())) {
                    try {
                        JSONObject object = new JSONObject(requestMessage.getResult());
                        if (object.optBoolean("isSuccess")) {
                            JSONArray array = object.optJSONArray("result");
                            if (array != null) {
                                for (int j = 0; j < array.length(); j++) {
                                    ModelProductPrice productPrice = new
                                            ModelProductPrice(array.optJSONObject(j));
                                    priceHashMap.put(productPrice.getSkuId(), productPrice);
                                }
                                productAdapter.notifyDataSetChanged();
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

    class ProductAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int i) {
            return products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.list_product_grid, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.list_product_image);
                viewHolder.priceTextView = (TextView) convertView
                        .findViewById(R.id.list_product_price);
                viewHolder.nameTextView = (TextView) convertView
                        .findViewById(R.id.list_product_name);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 256);
                viewHolder.imageView.setLayoutParams(layoutParams);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ModelProduct productDetail = products.get(i);
            ImageLoader.getInstance().displayImage(productDetail.getImage(), viewHolder.imageView);
            viewHolder.nameTextView.setText(productDetail.getName());
            if (priceHashMap.containsKey(productDetail.getSku())) {
                if (priceHashMap.get(productDetail.getSku()).getPrice() > 0) {
                    viewHolder.priceTextView.setText(Parameters.CONSTANT_RMB
                            + decimalFormat.format(priceHashMap.get(productDetail.getSku()).getPrice()));
                } else {
                    viewHolder.priceTextView.setHint("暂未设置价格");
                }
            } else {
                viewHolder.priceTextView.setHint("暂未设置价格");
            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView nameTextView;
            TextView priceTextView;
        }

    }

    private void showSuningAreas() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(SuningManager.getSuningAreas().getProvince().getName())) {
            stringBuilder.append(SuningManager.getSuningAreas().getProvince().getName());
            if (!TextUtils.isEmpty(SuningManager.getSuningAreas().getCity().getName())) {
                stringBuilder.append(">");
                stringBuilder.append(SuningManager.getSuningAreas().getCity().getName());
                if (!TextUtils.isEmpty(SuningManager.getSuningAreas().getDistrict().getName())) {
                    stringBuilder.append(">");
                    stringBuilder.append(SuningManager.getSuningAreas().getDistrict().getName());
                    if (!TextUtils.isEmpty(SuningManager.getSuningAreas().getTown().getName())) {
                        stringBuilder.append(">");
                        stringBuilder.append(SuningManager.getSuningAreas().getTown().getName());
                    }
                }
            }
        }
        productAreaTextView.setText(stringBuilder.toString());
    }

}

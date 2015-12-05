package yitgogo.smart.home.part;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartown.yitgogo.smart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import yitgogo.smart.BaseNormalFragment;
import yitgogo.smart.home.model.HomeData;
import yitgogo.smart.home.model.ModelKillPrice;
import yitgogo.smart.home.model.ModelSaleMiaosha;
import yitgogo.smart.product.ui.MiaoshaFragment;
import yitgogo.smart.tools.API;
import yitgogo.smart.tools.MissionController;
import yitgogo.smart.tools.NetworkContent;
import yitgogo.smart.tools.NetworkMissionMessage;
import yitgogo.smart.tools.OnNetworkListener;
import yitgogo.smart.tools.Parameters;
import yitgogo.smart.tools.QrCodeTool;
import yitgogo.smart.view.Notify;

public class PartMiaoshaFragment extends BaseNormalFragment {

    static PartMiaoshaFragment miaoshaFragment;
    GridView gridView;
    List<ModelSaleMiaosha> saleMiaoshas;
    HashMap<String, ModelKillPrice> killPriceHashMap;
    MiaoshaAdapter miaoshaAdapter;
    LinearLayout moreButton;

    public static PartMiaoshaFragment getMiaoshaFragment() {
        if (miaoshaFragment == null) {
            miaoshaFragment = new PartMiaoshaFragment();
        }
        return miaoshaFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        measureScreen();
        saleMiaoshas = new ArrayList<>();
        killPriceHashMap = new HashMap<>();
        miaoshaAdapter = new MiaoshaAdapter();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.part_home_sale_miaosha, null);
        findViews(view);
        return view;
    }

    @Override
    protected void findViews(View view) {
        gridView = (GridView) view.findViewById(R.id.miaosha_grid);
        moreButton = (LinearLayout) view.findViewById(R.id.miaosha_more);
        initViews();
    }

    @Override
    protected void initViews() {
        gridView.setAdapter(miaoshaAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Date currentTime = Calendar.getInstance().getTime();
                Date startTime = new Date(saleMiaoshas.get(arg2).getSeckillTime());
                if (startTime.before(currentTime)) {
                    if (saleMiaoshas.get(arg2).getSeckillNumber() > 0) {
                        showProductDetail(saleMiaoshas.get(arg2).getProdutId(), QrCodeTool.SALE_TYPE_MIAOSHA);
                    } else {
                        Notify.show("已售罄");
                    }
                } else {
                    Notify.show("未开始");
                }
            }
        });
        moreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openWindow(MiaoshaFragment.class.getName(), "秒杀");
            }
        });
    }

    public void refresh(String result) {
        saleMiaoshas.clear();
        miaoshaAdapter.notifyDataSetChanged();
        if (result.length() > 0) {
            JSONObject object;
            try {
                object = new JSONObject(result);
                if (object.optString("state").equalsIgnoreCase("SUCCESS")) {
                    JSONArray array = object.optJSONArray("dataList");
                    if (array != null) {
                        if (array.length() > 4) {
                            for (int i = 0; i < 4; i++) {
                                saleMiaoshas.add(new ModelSaleMiaosha(array
                                        .optJSONObject(i)));
                            }
                        } else {
                            for (int i = 0; i < array.length(); i++) {
                                saleMiaoshas.add(new ModelSaleMiaosha(array
                                        .optJSONObject(i)));
                            }
                        }
                        if (saleMiaoshas.size() > 0) {
                            getSalePrice();
                            int colums = saleMiaoshas.size();
                            gridView.setLayoutParams(new LinearLayout.LayoutParams(
                                    colums * HomeData.columWidth,
                                    LinearLayout.LayoutParams.MATCH_PARENT));
                            gridView.setColumnWidth(HomeData.columWidth);
                            gridView.setStretchMode(GridView.NO_STRETCH);
                            gridView.setNumColumns(colums);
                            miaoshaAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (saleMiaoshas.isEmpty()) {
            getView().setVisibility(View.GONE);
        } else {
            getView().setVisibility(View.VISIBLE);
        }
    }

    class MiaoshaAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return saleMiaoshas.size();
        }

        @Override
        public Object getItem(int position) {
            return saleMiaoshas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_sale_kill, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_kill_image);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.item_kill_name);
                viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.item_kill_price);
                viewHolder.salePriceTextView = (TextView) convertView.findViewById(R.id.item_kill_sale_price);
                viewHolder.stateTextView = (TextView) convertView.findViewById(R.id.item_kill_state);
                viewHolder.priceTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, HomeData.imageHeight);
                viewHolder.imageView.setLayoutParams(layoutParams);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            imageLoader.displayImage(getSmallImageUrl(saleMiaoshas
                    .get(position).getSeckillImg()), viewHolder.imageView);
            Date currentTime = Calendar.getInstance().getTime();
            Date startTime = new Date(saleMiaoshas.get(position).getSeckillTime());
            if (startTime.before(currentTime)) {
                if (saleMiaoshas.get(position).getSeckillNumber() > 0) {
                    viewHolder.stateTextView.setText("正在秒杀");
                    viewHolder.stateTextView.setBackgroundColor(Color.rgb(226, 59, 96));
                    viewHolder.stateTextView.setTextColor(Color.rgb(255, 241, 0));
                } else {
                    viewHolder.stateTextView.setText("已售罄");
                    viewHolder.stateTextView.setBackgroundColor(Color.rgb(189, 189, 189));
                    viewHolder.stateTextView.setTextColor(Color.rgb(255, 255, 255));
                }
            } else {
                viewHolder.stateTextView.setText("未开始");
                viewHolder.stateTextView.setBackgroundColor(Color.rgb(189, 189, 189));
                viewHolder.stateTextView.setTextColor(Color.rgb(255, 255, 255));
            }
            viewHolder.nameTextView.setText(saleMiaoshas.get(position)
                    .getProductName());
            if (killPriceHashMap.containsKey(saleMiaoshas.get(position).getProdutId())) {
                viewHolder.priceTextView.setText("原价:" + Parameters.CONSTANT_RMB + decimalFormat.format(killPriceHashMap.get(saleMiaoshas.get(position).getProdutId()).getOriginalPrice()));
                viewHolder.salePriceTextView.setText("秒杀价:" + Parameters.CONSTANT_RMB + decimalFormat.format(killPriceHashMap.get(saleMiaoshas.get(position).getProdutId()).getPrice()));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView nameTextView, priceTextView, salePriceTextView, stateTextView;
        }
    }

    /**
     * @author Tiger
     * @Url http://beta.yitos.net/api/product/promotionManage/promotion/
     * findPromotionPrice
     * @Parameters [productId=168633,54306,179859,54413, type=2]
     * @Result {"message":"ok","state":"SUCCESS","cacheKey":null,"dataList"
     * :[{"id":168633,"price":30.0},{"id":54306,"price":3980.0},
     * {"id":179859,"price":98.0},{"id":54413,"price":98.0}],
     * "totalCount":1,"dataMap":{},"object":null}
     */
    private void getSalePrice() {
        NetworkContent networkContent = new NetworkContent(API.API_SALE_PRICE);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < saleMiaoshas.size(); i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(saleMiaoshas.get(i).getProdutId());
        }
        networkContent.addParameters("productId", stringBuilder.toString()
                .trim());
        networkContent.addParameters("type", "2");
        MissionController.startNetworkMission(getActivity(), networkContent,
                new OnNetworkListener() {
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
                                            JSONObject jsonObject = array.optJSONObject(i);
                                            if (jsonObject != null) {
                                                killPriceHashMap.put(jsonObject.optString("id"), new ModelKillPrice(jsonObject));
                                            }
                                            miaoshaAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

}

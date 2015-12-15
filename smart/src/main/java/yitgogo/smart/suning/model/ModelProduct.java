package yitgogo.smart.suning.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiger on 2015-10-19.
 */
public class ModelProduct {
    String id = "", classId = "", sku = "", image = "", weight = "", brand = "", model = "", name = "", productArea = "", upc = "", saleUnit = "", category = "", introduction = "", param = "";
    int state = 0;
    List<ModelSuningImage> images = new ArrayList<>();
    JSONObject jsonObject = new JSONObject();

    public ModelProduct() {
    }

    public ModelProduct(JSONObject object) {
        if (object != null) {
            jsonObject = object;
            if (object.has("id")) {
                if (!object.optString("id").equalsIgnoreCase("null")) {
                    id = object.optString("id");
                }
            }
            if (object.has("classId")) {
                if (!object.optString("classId").equalsIgnoreCase("null")) {
                    classId = object.optString("classId");
                }
            }
            if (object.has("sku")) {
                if (!object.optString("sku").equalsIgnoreCase("null")) {
                    sku = object.optString("sku");
                }
            }
            if (object.has("image")) {
                if (!object.optString("image").equalsIgnoreCase("null")) {
                    image = object.optString("image");
                }
            }
            if (object.has("weight")) {
                if (!object.optString("weight").equalsIgnoreCase("null")) {
                    weight = object.optString("weight");
                }
            }
            if (object.has("brand")) {
                if (!object.optString("brand").equalsIgnoreCase("null")) {
                    brand = object.optString("brand");
                }
            }
            if (object.has("model")) {
                if (!object.optString("model").equalsIgnoreCase("null")) {
                    model = object.optString("model");
                }
            }
            if (object.has("name")) {
                if (!object.optString("name").equalsIgnoreCase("null")) {
                    name = object.optString("name");
                }
            }
            if (object.has("productArea")) {
                if (!object.optString("productArea").equalsIgnoreCase("null")) {
                    productArea = object.optString("productArea");
                }
            }
            if (object.has("upc")) {
                if (!object.optString("upc").equalsIgnoreCase("null")) {
                    upc = object.optString("upc");
                }
            }
            if (object.has("saleUnit")) {
                if (!object.optString("saleUnit").equalsIgnoreCase("null")) {
                    saleUnit = object.optString("saleUnit");
                }
            }
            if (object.has("category")) {
                if (!object.optString("category").equalsIgnoreCase("null")) {
                    category = object.optString("category");
                }
            }
            if (object.has("introduction")) {
                if (!object.optString("introduction").equalsIgnoreCase("null")) {
                    introduction = object.optString("introduction");
                }
            }
            if (object.has("param")) {
                if (!object.optString("param").equalsIgnoreCase("null")) {
                    param = object.optString("param");
                }
            }
            JSONArray imageArray = object.optJSONArray("cmimages");
            if (imageArray != null) {
                for (int i = 0; i < imageArray.length(); i++) {
                    images.add(new ModelSuningImage(imageArray.optJSONObject(i)));
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getClassId() {
        return classId;
    }

    public String getSku() {
        return sku;
    }

    public String getImage() {
        return image;
    }

    public String getWeight() {
        return weight;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getProductArea() {
        return productArea;
    }

    public String getUpc() {
        return upc;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public String getCategory() {
        return category;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getParam() {
        return param;
    }

    public int getState() {
        return state;
    }

    public List<ModelSuningImage> getImages() {
        return images;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public class ModelSuningImage {

        String id = "", img = "";

        public ModelSuningImage(JSONObject object) {
            if (object != null) {
                if (object.has("id")) {
                    if (!object.optString("id").equalsIgnoreCase("null")) {
                        id = object.optString("id");
                    }
                }
                if (object.has("img")) {
                    if (!object.optString("img").equalsIgnoreCase("null")) {
                        img = object.optString("img");
                    }
                }
            }
        }

        public String getId() {
            return id;
        }

        public String getImg() {
            return img;
        }
    }

}

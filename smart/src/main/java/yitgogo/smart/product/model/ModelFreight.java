package yitgogo.smart.product.model;

import org.json.JSONObject;

import java.util.Iterator;

public class ModelFreight {

    String agencyId = "", prompt = "";
    double fregith = 0;

    public ModelFreight(JSONObject object) {
        if (object != null) {
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.equals("prompt")) {
                    if (!object.optString(key).equalsIgnoreCase("null")) {
                        prompt = object.optString(key);
                    }
                } else {
                    agencyId = key;
                    fregith = object.optDouble(key);
                }
            }
        }
    }

    public String getAgencyId() {
        return agencyId;
    }

    public String getPrompt() {
        return prompt;
    }

    public double getFregith() {
        return fregith;
    }
}

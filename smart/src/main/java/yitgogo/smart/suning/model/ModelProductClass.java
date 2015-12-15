package yitgogo.smart.suning.model;

import org.json.JSONObject;

/**
 * Created by Tiger on 2015-10-21.
 */
public class ModelProductClass {

    String id = "", classNum = "", name = "";

    public ModelProductClass() {
    }

    public ModelProductClass(JSONObject object) {
        if (object != null) {
            if (object.has("name")) {
                if (!object.optString("name").equalsIgnoreCase("null")) {
                    name = object.optString("name");
                }
            }
            if (object.has("id")) {
                if (!object.optString("id").equalsIgnoreCase("null")) {
                    id = object.optString("id");
                }
            }
            if (object.has("classNum")) {
                if (!object.optString("classNum").equalsIgnoreCase("null")) {
                    classNum = object.optString("classNum");
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getClassNum() {
        return classNum;
    }

    public String getName() {
        return name;
    }
}
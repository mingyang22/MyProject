package baselibrary.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 做一下单例处理
 * Created by wz
 * on 2016/5/19.
 */
public class GsonUtil {
    protected static GsonUtil Instance;
    private Gson gson;

    protected GsonUtil() {
        gson = new Gson();
    }

    public static GsonUtil getInstance() {
        if (Instance == null) {
            createObj();
        }
        return Instance;
    }

    protected synchronized static void createObj() {
        if (Instance == null) {
            Instance = new GsonUtil();
        }
    }

    public <T> T jsonToObj(String json, Class<T> classObj) {
        T result = null;
        try {
            result = gson.fromJson(json, classObj);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(),"JsonToObj Error\n" + "Error Msg:" + e.getMessage() + "\nJson = " + json);
        }
        return result;
    }

    public <T> T jsonToObj(JsonObject json, Class<T> classObj) {
        T result = null;
        try {
            result = gson.fromJson(json, classObj);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(),"JsonToObj Error\n" + "Error Msg:" + e.getMessage() + "\nJson = " + json);
        }
        return result;
    }

    public <T> T jsonToObj(String json, Type type) {
        T result = null;
        try {
            result = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(),"JsonToObj Error\n" + "Error Msg:" + e.getMessage() + "\nJson = " + json);
        }
        return result;
    }

    public String ObjToJson(Object object, Class<?> classObj) {
        String result = "";
        try {
            result = gson.toJson(object, classObj);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(),"ObjToJson Error\n" + "Error Msg:" + e.getMessage() + "\nObjName=" + object.getClass().getSimpleName());
        }
        return result;
    }

    public String ObjToJson(Object object, Type type) {
        String result = "";
        try {
            result = gson.toJson(object, type);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(),"ObjToJson Erro\n" + "Error Msg:" + e.getMessage() + "\nObjName=" + object.getClass().getSimpleName());
        }
        return result;
    }

    public <T> List<T> jsonToList(String jsonString, Class<T> type) {
        List<T> list = new ArrayList<>();
        try {
            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(), "JsonToList Error\n" + "Error Msg:" + e.getMessage() + "\nJson = " + jsonString);
        }
        return list;
    }

}

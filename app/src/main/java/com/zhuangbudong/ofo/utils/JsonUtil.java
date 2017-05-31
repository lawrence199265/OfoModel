package com.zhuangbudong.ofo.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangxu on 17/5/31.
 */

public class JsonUtil<T> {

    public static List<Class> json2List(String jsonArrayString, Class clazz) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonArrayString);
        if (jsonArray.length() == 0) {
            throw new NullPointerException();
        }

        Field[] fields = clazz.getFields();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                if (consatin(fields, (String) iterator.next())) {
//                    clazz.getMethod()
                }
            }
        }

        return null;
    }

    private static boolean consatin(Field[] fields, String item) {
        boolean isContaint = false;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().toLowerCase().equals(item.toLowerCase())) {
                isContaint = true;
                break;
            }
        }
        return isContaint;
    }
}

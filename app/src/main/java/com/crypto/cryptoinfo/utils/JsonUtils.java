package com.crypto.cryptoinfo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtils {

    public static Map<String, Object> jsonToMap(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}

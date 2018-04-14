package com.fiuba.stories.stories.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ResponseObject {

    @SerializedName("a") String valueA;
    @SerializedName("b") String valueB;


    public static ResponseObject hydrate(JsonObject json) {
        return new Gson().fromJson(json, ResponseObject.class);
    }

    public String getValueA() {
        return valueA;
    }

    public String getValueB() {
        return valueB;
    }

}
package com.plt3ch.recipeviewer.InnerAppModels;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Plamen on 5/7/2017.
 */

public class WebServiceResponse implements Serializable {

    public static final String AUTH_TOKEN = "authToken";

    private Map<String, String> responseData;
    private RequestState state;

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public String getResponseValueForKey(String key) {
        if (responseData == null) {
            return null;
        }

        return responseData.get(key);
    }

    public void setResponseValueForKey(String key, String value) {
        if (responseData == null) {
            responseData = new HashMap<>();
        }

        responseData.put(key, value);
    }
}

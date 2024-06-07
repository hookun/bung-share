package com.example.bung_share;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Make_review_Request extends StringRequest {
    final static private String URL = "http://27.96.131.54:8080/2020081097/makeReview.jsp";
    private Map<String, String> parameters;
    public Make_review_Request(String userId, String storeId, String rating, String content,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Log.d("test", storeId+"/"+userId+"/"+rating+"/"+content);
        parameters = new HashMap<>();
        parameters.put("storeid", storeId);
        parameters.put("userid", userId);
        parameters.put("rating", rating);
        parameters.put("content", content);

    }
    public Map<String, String> getParams() {
        return parameters;
    }
}

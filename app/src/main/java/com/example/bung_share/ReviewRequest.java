package com.example.bung_share;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewRequest extends StringRequest {
    final static private String URL = "http://27.96.131.54:8080/2020081097/Requset_Review.jsp";
    private Map<String, String> parameters;
    public ReviewRequest(String storeID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("storeID", storeID);

    }
    public Map<String, String> getParams() {
        return parameters;
    }
}

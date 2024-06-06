package com.example.bung_share;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Add_dibs_Request extends StringRequest {
    final static private String URL = "http://27.96.131.54:8080/2020081097/Adddibs.jsp";
    private Map<String, String> parameters;
    public Add_dibs_Request(String isdibed,String storeid,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("storeid", storeid);
        parameters.put("isdibed", isdibed);
    }
    public Map<String, String> getParams() {
        return parameters;
    }
}
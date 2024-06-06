package com.example.bung_share;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddMapRequest extends StringRequest {
    final static private String URL = "http://27.96.131.54:8080/2020081097/Addmap.jsp";
    private Map<String, String> parameters;
    public AddMapRequest(String address, String category, String pay, String closeday, String operationtime,String menuinfo, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("address", address);
        parameters.put("category", category);
        parameters.put("pay", pay);
        parameters.put("closedDays", closeday);
        parameters.put("operationHours", operationtime);
        parameters.put("menu", menuinfo);
    }
    public Map<String, String> getParams() {
        return parameters;
    }
}
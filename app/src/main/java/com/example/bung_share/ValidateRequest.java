package com.example.bung_share;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private String URL = "http://27.96.131.54:8080/2020081097/UserValidate.jsp";
    private Map<String, String> parameters;
    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userID", userID);

    }
    public Map<String, String> getParams() {
        return parameters;
    }
}

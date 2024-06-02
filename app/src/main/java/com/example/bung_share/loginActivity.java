package com.example.bung_share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class loginActivity extends AppCompatActivity {
    private AlertDialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView tvRegister = findViewById(R.id.toRegister);
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, regiseterActivity.class);
            startActivity(intent);
        });
        final EditText etid = findViewById(R.id.loginid);
        final EditText etpwd = findViewById(R.id.loginpw);
        final Button btnLogin = findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = etid.getText().toString();
                final String pwd = etpwd.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity.this);
                                dialog = builder.setMessage("로그인 성공.").setPositiveButton("확인", null).create();
                                dialog.show();
                                Intent intent = new Intent( loginActivity.this, MainActivity.class );
                                intent.putExtra("id", id);
                                startActivity( intent );
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity.this);
                                dialog = builder.setMessage("로그인 실패.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(id,pwd,responseListener);
                RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
                queue.add(loginRequest);

            }
        });
        }
    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}

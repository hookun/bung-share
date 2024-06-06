package com.example.bung_share;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class regiseterActivity extends AppCompatActivity {
    //private ArrayAdapter adapter;
    //private String userID;
    //private String userPassword;
    //private String userEmail;
    private AlertDialog dialog;
    private boolean validate = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

       final EditText userID = (EditText) findViewById(R.id.editTextUsername);
       final EditText userPw = (EditText) findViewById(R.id.editTextPassword);
       final EditText userEmail = (EditText) findViewById(R.id.editTextEmail);
       final EditText userName = (EditText) findViewById(R.id.editTextName);
       final Button btnValidate = (Button) findViewById(R.id.validateButton);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userID.getText().toString();
                if(id.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    validate = false;
                    userID.setFocusable(true);
                    return;
                }
                if(validate)return;

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean newID = jsonResponse.getBoolean("newID");
                            if(newID){
                                AlertDialog.Builder builder = new AlertDialog.Builder(regiseterActivity.this);
                                dialog = builder.setMessage("사용 가능한 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                userID.setEnabled(false);
                                userID.setBackgroundColor(Color.GRAY);
                                btnValidate.setBackgroundColor(Color.GRAY);
                                validate = true;
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(regiseterActivity.this);
                                dialog = builder.setMessage("사용 불가한 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(regiseterActivity.this);
                queue.add(validateRequest);
            }
        });
        Button registerbtn = findViewById(R.id.registerButton);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userID.getText().toString();
                String pw = userPw.getText().toString();
                String email = userEmail.getText().toString();
                String name = userName.getText().toString();
                if(!validate){
                    Toast.makeText(getApplicationContext(), "아이디 중복을 체크해주세요", Toast.LENGTH_SHORT).show();
                    userID.setFocusable(true);
                    return;
                }
                if(id.trim().isEmpty() || pw.trim().isEmpty() || email.trim().isEmpty()||name.trim().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(regiseterActivity.this);
                    dialog = builder.setMessage("모든 항목을 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(regiseterActivity.this);
                                dialog = builder.setMessage("등록완료.").setPositiveButton("확인", null).create();
                                dialog.show();
                                finish();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(regiseterActivity.this);
                                dialog = builder.setMessage("등록실패.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(id, pw,email,name,responseListener);
                RequestQueue queue = Volley.newRequestQueue(regiseterActivity.this);
                queue.add(registerRequest);
            }
        });

    }
    protected void onstop(){
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}

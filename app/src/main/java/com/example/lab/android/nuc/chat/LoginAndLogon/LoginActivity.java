package com.example.lab.android.nuc.chat.LoginAndLogon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.lab.android.nuc.chat.MainActivity;
import com.example.lab.android.nuc.chat.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText uesr_name;
    EditText password;
    TextView change_password;
    TextView register;
    Button button;
    String n, p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button);
        change_password = findViewById(R.id.change_password);
        register = findViewById(R.id.register);
        uesr_name = findViewById(R.id.user_name_input);
        password = findViewById(R.id.password_input);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                n = uesr_name.getText().toString();
                p = password.getText().toString();
                OkGo.<String>post("http://47.95.7.169:8080/logon")
                        .tag(this)
                        .isMultipart(true)
                        .params("UserID", n)
                        .params("password", p)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.i("return ", response.body());
                                Map<String, Object> map = JSON.parseObject(response.body(), new TypeReference<Map<String, Object>>() {
                                });
                                if (map.containsKey("password")) {
                                    if (map.get("password").equals(p)) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("UserID", n);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                if (map.containsKey("status")) {
                                    if (map.get("status").equals("no"))
                                        Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

package com.example.collegeinfogap.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeinfogap.R;
import com.google.android.material.appbar.MaterialToolbar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);

        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> register());
        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void register() {

        String username =
                etUsername.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();

        String password2 =
                etPassword2.getText().toString().trim();

        if(username.isEmpty() || password.isEmpty()){

            Toast.makeText(
                    this,
                    "请输入完整信息",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }

        if(!password.equals(password2)){

            Toast.makeText(
                    this,
                    "两次密码不一致",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }

        registerBySocket(
                username,
                password);

    }
    private void registerBySocket(
            String username,
            String password){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.send(
                                "REGISTER",
                                username,
                                password);

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        Toast.makeText(
                                RegisterActivity.this,
                                "注册成功",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    }else{

                        Toast.makeText(
                                RegisterActivity.this,
                                "用户名已存在",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                RegisterActivity.this,
                                "连接服务器失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }

        }).start();

    }

}
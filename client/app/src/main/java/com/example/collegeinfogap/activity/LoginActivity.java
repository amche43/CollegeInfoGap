package com.example.collegeinfogap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeinfogap.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.collegeinfogap.database.DBHelper;
import com.example.collegeinfogap.utils.SPUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (SPUtil.isLogin(this)) {

            Intent intent =
                    new Intent(this,
                            HomeActivity.class);

            startActivity(intent);

            finish();
            return;

        }
        // 获取控件
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        cbRemember = findViewById(R.id.cbRemember);

        // 登录按钮
        btnLogin.setOnClickListener(v -> login());

        // 注册按钮
        btnRegister.setOnClickListener(v -> {

            Intent intent =
                    new Intent(LoginActivity.this,
                            RegisterActivity.class);

            startActivity(intent);

        });

    }

    private void login() {

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {

            Toast.makeText(this,
                    "请输入用户名和密码",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        loginBySocket(username,password);

    }
    private void loginBySocket(String username, String password) {

        new Thread(() -> {

            try {

                String result =
                        com.example.collegeinfogap.network.SocketClient.send(
                                "LOGIN",
                                username,
                                password
                        );

                runOnUiThread(() -> {

                    if ("SUCCESS".equals(result)) {

                        Toast.makeText(
                                LoginActivity.this,
                                "登录成功",
                                Toast.LENGTH_SHORT
                        ).show();

                        SPUtil.saveLogin(
                                LoginActivity.this,
                                username);

                        Intent intent =
                                new Intent(
                                        LoginActivity.this,
                                        HomeActivity.class);

                        intent.putExtra(
                                "username",
                                username);

                        startActivity(intent);

                        finish();

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                "用户名或密码错误",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                LoginActivity.this,
                                "连接服务器失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }

        }).start();

    }

}
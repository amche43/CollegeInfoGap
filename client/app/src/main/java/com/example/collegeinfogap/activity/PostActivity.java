package com.example.collegeinfogap.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.network.SocketClient;
import com.example.collegeinfogap.utils.SPUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;


public class PostActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private BottomNavigationView bottomNav;
    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setBackgroundColor(
                getColor(R.color.primaryBlue));

        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(R.id.nav_post);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {

                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;

            }

            if (id == R.id.nav_post) {

                return true;

            }

            if (id == R.id.nav_chat) {

                startActivity(new Intent(this, ChatActivity.class));
                finish();
                return true;

            }

            if (id == R.id.nav_my) {

                startActivity(new Intent(this, MyActivity.class));
                finish();
                return true;

            }

            return false;

        });

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        imageView = findViewById(R.id.imageView);

        Button btnSelect =
                findViewById(R.id.btnSelectImage);

        Button btnPublish =
                findViewById(R.id.btnPublish);
        btnSelect.setOnClickListener(v -> {

            launcher.launch("image/*");

        });
        btnPublish.setOnClickListener(v -> publish());
        Button btnAI = findViewById(R.id.btnAI);

        btnAI.setOnClickListener(v -> {

            String content = etContent.getText().toString().trim();

            if(content.isEmpty()){
                Toast.makeText(this, "请先输入内容", Toast.LENGTH_SHORT).show();
                return;
            }

            callAI("TITLE", content, result -> {
                etTitle.setText(result);
            });

            callAI("SUMMARY", content, result -> {
                Toast.makeText(this, "摘要：" + result, Toast.LENGTH_SHORT).show();
            });

            callAI("TAG", content, result -> {
                Toast.makeText(this, "标签：" + result, Toast.LENGTH_SHORT).show();
            });

        });

    }

    private void publish() {

        String title =
                etTitle.getText().toString().trim();

        String content =
                etContent.getText().toString().trim();

        if(title.isEmpty() || content.isEmpty()){

            Toast.makeText(
                    this,
                    "请输入完整内容",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }

        publishBySocket(
                title,
                SPUtil.getUsername(this),
                content,
                imageUri==null ? "" : imageUri.toString());

    }
    private void publishBySocket(
            String title,
            String author,
            String content,
            String image){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.send(
                                "POST",
                                title,
                                author,
                                content,
                                image);

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        Toast.makeText(
                                PostActivity.this,
                                "发布成功",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    }else{

                        Toast.makeText(
                                PostActivity.this,
                                "发布失败",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                PostActivity.this,
                                "连接服务器失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }

        }).start();

    }
    private final ActivityResultLauncher<String> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if(uri!=null){

                            imageUri = uri;

                            imageView.setImageURI(uri);

                        }

                    });
    private void callAI(String type, String content, AIResult callback) {

        new Thread(() -> {

            try {

                String result = SocketClient.send(
                        "AI",
                        type,
                        content
                );

                runOnUiThread(() -> {
                    callback.onResult(result);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
    interface AIResult {
        void onResult(String result);
    }
}
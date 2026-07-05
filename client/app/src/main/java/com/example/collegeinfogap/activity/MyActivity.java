package com.example.collegeinfogap.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.database.DBHelper;
import com.example.collegeinfogap.utils.SPUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyActivity extends AppCompatActivity {

    private TextView tvUsername;
    private TextView tvPostCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        tvUsername=findViewById(R.id.tvUsername);
        tvPostCount=findViewById(R.id.tvPostCount);

        Button btnLogout=findViewById(R.id.btnLogout);

        Button btnMyPost=findViewById(R.id.btnMyPost);

        Button btnFavorite =
                findViewById(R.id.btnFavorite);

        BottomNavigationView bottomNav=findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(R.id.nav_my);

        bottomNav.setOnItemSelectedListener(item->{

            int id=item.getItemId();

            if(id==R.id.nav_home){

                startActivity(new Intent(this,HomeActivity.class));
                finish();
                return true;

            }

            if(id==R.id.nav_post){

                startActivity(new Intent(this,PostActivity.class));
                finish();
                return true;

            }

            if(id==R.id.nav_chat){

                startActivity(new Intent(this,ChatActivity.class));
                finish();
                return true;

            }

            return true;

        });

        String username=SPUtil.getUsername(this);

        tvUsername.setText("用户名："+username);

        loadPostCount();

        btnLogout.setOnClickListener(v->{

            SPUtil.logout(this);

            startActivity(
                    new Intent(
                            this,
                            LoginActivity.class));

            finish();

        });

        btnMyPost.setOnClickListener(v->{

            startActivity(
                    new Intent(
                            this,
                            MyPostActivity.class));

        });

        btnFavorite.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            FavoriteActivity.class));

        });

    }
    private void loadPostCount(){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.sendMulti(
                                "GET_MY_POST",
                                SPUtil.getUsername(this)
                        );

                int count = 0;

                if(!result.trim().isEmpty()){

                    String[] rows =
                            result.split("\n");

                    count = rows.length;

                }

                int finalCount = count;

                runOnUiThread(() ->

                        tvPostCount.setText(
                                "我发布的帖子：" +
                                        finalCount +
                                        " 条")

                );

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }

}
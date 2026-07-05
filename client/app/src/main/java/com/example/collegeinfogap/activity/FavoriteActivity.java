package com.example.collegeinfogap.activity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.adapter.PostAdapter;
import com.example.collegeinfogap.bean.Post;
import com.example.collegeinfogap.utils.SPUtil;
import android.graphics.Color;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<Post> list;

    private PostAdapter adapter;

    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.getNavigationIcon().setTint(
                Color.WHITE);

        toolbar.setTitleTextColor(
                Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView =
                findViewById(R.id.recyclerView);

        tvEmpty =
                findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        list =
                new ArrayList<>();

        loadFavoriteFromServer();

    }

    private void loadFavoriteFromServer(){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.sendMulti(
                                "GET_FAVORITE",
                                SPUtil.getUsername(this)
                        );

                list.clear();

                String[] rows =
                        result.split("\n");

                for(String row : rows){

                    if(row.trim().isEmpty()){

                        continue;

                    }

                    String[] data =
                            row.split("\\|",-1);

                    if(data.length>=5){

                        list.add(
                                new Post(
                                        Integer.parseInt(data[0]),
                                        data[1],
                                        data[2],
                                        data[3],
                                        data[4]
                                )
                        );

                    }

                }

                runOnUiThread(() -> {

                    if(list.isEmpty()){

                        tvEmpty.setVisibility(View.VISIBLE);

                        recyclerView.setVisibility(View.GONE);

                    }else{

                        tvEmpty.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);

                    }

                    if(recyclerView.getAdapter()==null){

                        adapter =
                                new PostAdapter(
                                        FavoriteActivity.this,
                                        list);
                        recyclerView.setAdapter(adapter);

                        adapter.setOnPostClickListener(post -> {

                            Intent intent =
                                    new Intent(
                                            FavoriteActivity.this,
                                            DetailActivity.class);

                            intent.putExtra("id",
                                    post.getId());

                            intent.putExtra("title",
                                    post.getTitle());

                            intent.putExtra("author",
                                    post.getAuthor());

                            intent.putExtra("content",
                                    post.getContent());

                            intent.putExtra("image",
                                    post.getImage());

                            startActivity(intent);

                        });
                        adapter.setOnPostLongClickListener(post -> {

                            new androidx.appcompat.app.AlertDialog.Builder(
                                    FavoriteActivity.this)

                                    .setTitle("取消收藏")

                                    .setMessage("确定取消收藏这篇帖子吗？")

                                    .setPositiveButton("确定",
                                            (dialog, which) ->
                                                    unFavorite(post.getId()))

                                    .setNegativeButton("取消", null)

                                    .show();

                        });

                    }else{

                        adapter.notifyDataSetChanged();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void unFavorite(int postId){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.send(
                                "UNFAVORITE",
                                SPUtil.getUsername(this),
                                String.valueOf(postId));

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        android.widget.Toast.makeText(
                                FavoriteActivity.this,
                                "已取消收藏",
                                android.widget.Toast.LENGTH_SHORT
                        ).show();

                        loadFavoriteFromServer();

                    }else{

                        android.widget.Toast.makeText(
                                FavoriteActivity.this,
                                "取消失败",
                                android.widget.Toast.LENGTH_SHORT
                        ).show();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }

}
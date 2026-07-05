package com.example.collegeinfogap.activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.adapter.PostAdapter;
import com.example.collegeinfogap.bean.Post;
import com.example.collegeinfogap.utils.SPUtil;
import android.graphics.Color;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class MyPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<Post> list;

    private PostAdapter adapter;

    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_post);

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

        loadMyPostFromServer();

    }

    private void loadMyPostFromServer(){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.sendMulti(
                                "GET_MY_POST",
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
                                        MyPostActivity.this,
                                        list);

                        recyclerView.setAdapter(adapter);

                        adapter.setOnPostClickListener(post -> {

                            Intent intent =
                                    new Intent(
                                            MyPostActivity.this,
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
                                    MyPostActivity.this)

                                    .setTitle("删除帖子")

                                    .setMessage("确定删除这篇帖子吗？")

                                    .setPositiveButton("删除",
                                            (dialog, which) ->

                                                    deletePostBySocket(
                                                            post.getId()))

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
    private void deletePostBySocket(int id){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.send(
                                "DELETE_POST",
                                String.valueOf(id)
                        );

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        android.widget.Toast.makeText(
                                MyPostActivity.this,
                                "删除成功",
                                android.widget.Toast.LENGTH_SHORT
                        ).show();

                        loadMyPostFromServer();

                    }else{

                        android.widget.Toast.makeText(
                                MyPostActivity.this,
                                "删除失败",
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
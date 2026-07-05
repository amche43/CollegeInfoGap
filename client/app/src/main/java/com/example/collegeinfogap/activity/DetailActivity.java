package com.example.collegeinfogap.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.adapter.CommentAdapter;
import com.example.collegeinfogap.bean.Comment;
import com.example.collegeinfogap.network.SocketClient;
import com.example.collegeinfogap.utils.SPUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private String postTitle;
    private RecyclerView recyclerView;
    private ArrayList<Comment> commentList;
    private CommentAdapter adapter;
    private int postId;
    private Button btnLike;
    private Button btnFavorite;
    private boolean isLike = false;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.getNavigationIcon().setTint(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> finish());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvTitle=findViewById(R.id.tvTitle);
        TextView tvAuthor=findViewById(R.id.tvAuthor);
        TextView tvContent=findViewById(R.id.tvContent);

        ImageView imageView=findViewById(R.id.imageView);

        btnLike = findViewById(R.id.btnLike);
        btnFavorite = findViewById(R.id.btnFavorite);

        postTitle = getIntent().getStringExtra("title");

        String author=getIntent().getStringExtra("author");

        String content=getIntent().getStringExtra("content");

        String image=getIntent().getStringExtra("image");

        imageView.setOnClickListener(v -> {

            Intent intent = new Intent(this, ImageActivity.class);

            intent.putExtra("image", image);

            startActivity(intent);

        });
        btnLike.setOnClickListener(v -> {

            if(isLike){

                unLike();

            }else{

                like();

            }

        });
        btnFavorite.setOnClickListener(v -> {

            if(isFavorite){

                unFavorite();

            }else{

                favorite();

            }

        });

        EditText etComment=findViewById(R.id.etComment);

        Button btnSend=findViewById(R.id.btnSend);

        recyclerView=findViewById(R.id.recyclerComment);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        postId = getIntent().getIntExtra("id",-1);

        Toast.makeText(
                this,
                "postId = " + postId,
                Toast.LENGTH_SHORT
        ).show();

        tvTitle.setText(postTitle);

        tvAuthor.setText("作者："+author);

        tvContent.setText(content);

        if(image!=null && !image.isEmpty()){

            imageView.setImageURI(Uri.parse(image));

        }

        commentList=new ArrayList<>();

        adapter=new CommentAdapter(this,commentList);

        recyclerView.setAdapter(adapter);

        loadComment();

        loadLikeState();

        loadFavoriteState();

        btnSend.setOnClickListener(v->{

            String text=etComment.getText().toString().trim();

            if(text.isEmpty()){

                Toast.makeText(this,
                        "请输入评论",
                        Toast.LENGTH_SHORT).show();

                return;

            }

            publishCommentBySocket(
                    postId,
                    SPUtil.getUsername(this),
                    text
            );

        });

    }

    private void loadComment(){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.sendMulti(
                                "GET_COMMENT",
                                String.valueOf(postId)
                        );

                commentList.clear();

                String[] rows =
                        result.split("\n");

                for(String row : rows){

                    if(row.trim().isEmpty()){

                        continue;

                    }

                    String[] data =
                            row.split("\\|");

                    if(data.length>=2){

                        commentList.add(
                                new Comment(
                                        data[0],
                                        data[1]
                                )
                        );

                    }

                }

                runOnUiThread(() ->

                        adapter.notifyDataSetChanged()

                );

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void publishCommentBySocket(
            int postId,
            String author,
            String content){

        new Thread(() -> {

            try{

                String result =
                        com.example.collegeinfogap.network.SocketClient.send(
                                "COMMENT",
                                String.valueOf(postId),
                                author,
                                content
                        );

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        Toast.makeText(
                                DetailActivity.this,
                                "评论成功",
                                Toast.LENGTH_SHORT
                        ).show();

                        // 下一步这里会改成从服务器重新加载评论
                        loadComment();

                    }else{

                        Toast.makeText(
                                DetailActivity.this,
                                "评论失败",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                DetailActivity.this,
                                "连接服务器失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }

        }).start();

    }

    private void like(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "LIKE",

                                SPUtil.getUsername(this),

                                String.valueOf(postId)

                        );

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        isLike = true;

                        loadLikeCount();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                DetailActivity.this,
                                "网络连接失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }
        }).start();

    }
    private void unLike(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "UNLIKE",

                                SPUtil.getUsername(this),

                                String.valueOf(postId)

                        );

                runOnUiThread(() -> {

                    if("SUCCESS".equals(result)){

                        isLike = false;

                        loadLikeCount();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                DetailActivity.this,
                                "网络连接失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }
        }).start();

    }
    private void loadLikeState(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "IS_LIKE",

                                SPUtil.getUsername(this),

                                String.valueOf(postId)

                        );

                runOnUiThread(() -> {

                    if("YES".equals(result)){

                        isLike = true;

                        btnLike.setText("❤️ 已点赞");

                    }else{

                        isLike = false;

                        btnLike.setText("👍 点赞");

                    }
                    loadLikeCount();
                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                DetailActivity.this,
                                "网络连接失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }

        }).start();

    }
    private void loadLikeCount(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "GET_LIKE_COUNT",

                                String.valueOf(postId)

                        );

                int count =
                        Integer.parseInt(result);

                runOnUiThread(() -> {

                    if(isLike){

                        btnLike.setText("❤️ " + count);

                    }else{

                        btnLike.setText("👍 " + count);

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

                runOnUiThread(() ->

                        Toast.makeText(
                                DetailActivity.this,
                                "网络连接失败",
                                Toast.LENGTH_SHORT
                        ).show()

                );

            }

        }).start();

    }
    private void favorite(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "FAVORITE",

                                SPUtil.getUsername(this),

                                String.valueOf(postId)

                        );

                runOnUiThread(() -> {

                    if(result.trim().equals("SUCCESS")){

                        isFavorite = true;

                        loadFavoriteCount();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void unFavorite(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "UNFAVORITE",

                                SPUtil.getUsername(this),

                                String.valueOf(postId)

                        );

                runOnUiThread(() -> {

                    if(result.trim().equals("SUCCESS")){

                        isFavorite = false;

                        loadFavoriteCount();

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void loadFavoriteState(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "IS_FAVORITE",

                                SPUtil.getUsername(this),

                                String.valueOf(postId)

                        );

                runOnUiThread(() -> {

                    if(result.trim().equals("YES")){

                        isFavorite = true;

                    }else{

                        isFavorite = false;

                    }

                    loadFavoriteCount();

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void loadFavoriteCount(){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.send(

                                "GET_FAVORITE_COUNT",

                                String.valueOf(postId)

                        );

                int count =
                        Integer.parseInt(result.trim());

                runOnUiThread(() -> {

                    if(isFavorite){

                        btnFavorite.setText("⭐ " + count);

                    }else{

                        btnFavorite.setText("☆ " + count);

                    }

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;

    }

}
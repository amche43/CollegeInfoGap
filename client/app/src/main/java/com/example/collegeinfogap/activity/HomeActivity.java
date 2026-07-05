package com.example.collegeinfogap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.adapter.PostAdapter;
import com.example.collegeinfogap.bean.Post;
import com.example.collegeinfogap.network.SocketClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private android.widget.EditText etSearch;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNav;
    private ArrayList<Post> postList;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private String currentSort = "NEW";
    private TextView tvHomeTitle;
    private TextView tvSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                true
        );

        setContentView(R.layout.activity_home);

        etSearch = findViewById(R.id.etSearch);

        tvSort =
                findViewById(R.id.tvSort);

        recyclerView = findViewById(R.id.recyclerView);

        swipeRefresh =
                findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(
                R.color.primaryBlue
        );

        swipeRefresh.setColorSchemeResources(R.color.primaryBlue);
        swipeRefresh.setProgressBackgroundColorSchemeResource(
                android.R.color.white
        );

        fab = findViewById(R.id.fab);

        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if(id==R.id.nav_home){

                return true;

            }

            if(id==R.id.nav_post){

                startActivity(
                        new Intent(
                                this,
                                PostActivity.class));

                return true;

            }

            if(id==R.id.nav_chat){

                startActivity(
                        new Intent(
                                this,
                                ChatActivity.class));

                return true;

            }

            if(id==R.id.nav_my){

                startActivity(
                        new Intent(
                                this,
                                MyActivity.class));

                return true;

            }

            return false;

        });

        swipeRefresh.setOnRefreshListener(() -> {

            loadPostsFromServer();

        });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        postList = new ArrayList<>();

        loadPostsFromServer();

        fab.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            PostActivity.class);

            startActivity(intent);

        });
        tvSort.setOnClickListener(v -> {

            android.widget.PopupMenu menu =
                    new android.widget.PopupMenu(
                            HomeActivity.this,
                            tvSort
                    );

            menu.getMenu().add("最新发布");
            menu.getMenu().add("点赞最多");
            menu.getMenu().add("收藏最多");
            menu.getMenu().add("评论最多");

            menu.setOnMenuItemClickListener(item -> {

                String title = item.getTitle().toString();

                if(title.equals("最新发布")){

                    currentSort = "NEW";

                }else if(title.equals("点赞最多")){

                    currentSort = "LIKE";

                }else if(title.equals("收藏最多")){

                    currentSort = "FAVORITE";

                }else if(title.equals("评论最多")){

                    currentSort = "COMMENT";

                }

                updateSortText();

                loadPostsFromServer(); // ⭐刷新数据

                return true;
            });

            menu.show();

        });
        tvHomeTitle =
                findViewById(R.id.tvHomeTitle);
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                if (s.toString().trim().isEmpty()) {

                    loadPostsFromServer();

                } else {

                    searchPosts(s.toString());

                }

            }

            @Override
            public void afterTextChanged(android.text.Editable s) {

            }
        });

    }
    private void loadPostsFromServer() {

        new Thread(() -> {

            try {

                String result =
                        SocketClient.sendMulti(
                                "GET_POST",
                                currentSort
                        );
                postList.clear();

                String[] rows =
                        result.split("\n");

                for (String row : rows) {

                    if (row.trim().isEmpty()) {
                        continue;
                    }

                    String[] data =
                            row.split("\\|", -1);

                    if (data.length >= 5) {

                        Post post =
                                new Post(
                                        Integer.parseInt(data[0]),
                                        data[1],
                                        data[2],
                                        data[3],
                                        data[4]
                                );

                        if(data.length >= 8){

                            post.setLikeCount(
                                    Integer.parseInt(data[5]));

                            post.setFavoriteCount(
                                    Integer.parseInt(data[6]));

                            post.setCommentCount(
                                    Integer.parseInt(data[7]));

                        }

                        postList.add(post);

                    }

                }

                runOnUiThread(() -> {

                    if (adapter == null) {

                        adapter =
                                new PostAdapter(
                                        HomeActivity.this,
                                        postList);

                        recyclerView.setAdapter(adapter);

                        adapter.setOnPostClickListener(post -> {

                            Intent intent =
                                    new Intent(
                                            HomeActivity.this,
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

                    } else {

                        adapter.notifyDataSetChanged();

                    }
                    swipeRefresh.setRefreshing(false);

                });

            } catch (Exception e) {

                e.printStackTrace();

            }

        }).start();

    }

    private void searchPosts(String keyword){

        new Thread(() -> {

            try{

                String result =
                        SocketClient.sendMulti(
                                "SEARCH",
                                keyword);

                postList.clear();

                String[] rows =
                        result.split("\n");

                for(String row : rows){

                    if(row.trim().isEmpty()){

                        continue;

                    }

                    String[] data =
                            row.split("\\|",-1);

                    if(data.length>=5){

                        postList.add(

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

                runOnUiThread(() ->

                        adapter.notifyDataSetChanged()

                );

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void updateSortText(){

        if(currentSort.equals("NEW")){

            tvSort.setText("最新发布 ▼");

        }else if(currentSort.equals("LIKE")){

            tvSort.setText("点赞最多 ▼");

        }else if(currentSort.equals("FAVORITE")){

            tvSort.setText("收藏最多 ▼");

        }else if(currentSort.equals("COMMENT")){

            tvSort.setText("评论最多 ▼");

        }

    }

    private boolean firstLoad = true;

    @Override
    protected void onResume() {

        super.onResume();

        if(firstLoad){

            firstLoad = false;

            return;

        }

        loadPostsFromServer();

    }
}
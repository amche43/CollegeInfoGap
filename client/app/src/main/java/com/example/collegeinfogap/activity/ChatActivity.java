package com.example.collegeinfogap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.adapter.ChatAdapter;
import com.example.collegeinfogap.network.SocketClient;
import com.example.collegeinfogap.utils.SPUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.collegeinfogap.bean.Message;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<Message> messages;

    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        EditText etMessage =
                findViewById(R.id.etMessage);

        Button btnSend =
                findViewById(R.id.btnSend);

        recyclerView =
                findViewById(R.id.recyclerView);

        BottomNavigationView bottomNav =
                findViewById(R.id.bottomNav);

        messages = new ArrayList<>();

        adapter =
                new ChatAdapter(this, messages);

        recyclerView.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        loadMessages();

        btnSend.setOnClickListener(v -> {

            String msg =
                    etMessage.getText().toString().trim();

            if(msg.isEmpty()){

                return;

            }

            sendMessage(msg);

            etMessage.setText("");

        });

        bottomNav.setSelectedItemId(R.id.nav_chat);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

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

                return true;

            }

            if(id==R.id.nav_my){

                startActivity(new Intent(this,MyActivity.class));
                finish();
                return true;

            }

            return false;

        });

    }
    private void sendMessage(String content){

        new Thread(() ->{

            try{

                String result =
                        SocketClient.send(
                                "SEND_MESSAGE",
                                SPUtil.getUsername(this),
                                "ALL",
                                content
                        );

                if(result.equals("SUCCESS")){

                    runOnUiThread(() ->{
                        loadMessages();
                    });

                }

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void loadMessages(){

        new Thread(() ->{

            try{

                String result =
                        SocketClient.sendMulti("GET_MESSAGE");

                runOnUiThread(() ->{

                    parseMessages(result);

                });

            }catch(Exception e){

                e.printStackTrace();

            }

        }).start();

    }
    private void parseMessages(String data){

        messages.clear();

        if(data == null || data.isEmpty()){

            adapter.notifyDataSetChanged();

            return;

        }

        String[] rows = data.split("\n");

        for(String row : rows){

            if(row.trim().isEmpty()) continue;

            String[] arr = row.split("\\|");

            if(arr.length < 5) continue;

            Message message = new Message();

            message.setId(
                    Integer.parseInt(arr[0]));

            message.setSender(arr[1]);

            message.setReceiver(arr[2]);

            message.setContent(arr[3]);

            message.setSendTime(arr[4]);

            messages.add(message);

        }

        adapter.notifyDataSetChanged();

        if(messages.size()>0){

            recyclerView.scrollToPosition(
                    messages.size()-1);

        }

    }

}
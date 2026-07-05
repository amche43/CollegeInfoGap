package com.example.collegeinfogap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.bean.Message;

import java.util.ArrayList;

public class ChatAdapter
        extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Message> list;

    public ChatAdapter(Context context,
                       ArrayList<Message> list) {

        this.context = context;
        this.list = list;

    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvSender;

        TextView tvContent;

        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvSender =
                    itemView.findViewById(R.id.tvSender);

            tvContent =
                    itemView.findViewById(R.id.tvContent);

            tvTime =
                    itemView.findViewById(R.id.tvTime);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_message,
                                parent,
                                false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Message message =
                list.get(position);

        holder.tvSender.setText(
                message.getSender());

        holder.tvContent.setText(
                message.getContent());

        holder.tvTime.setText(
                message.getSendTime());

    }

    @Override
    public int getItemCount() {

        return list.size();

    }

}
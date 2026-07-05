package com.example.collegeinfogap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.bean.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context context;

    private ArrayList<Comment> list;

    public CommentAdapter(Context context,
                          ArrayList<Comment> list){

        this.context = context;

        this.list = list;

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor;

        TextView tvContent;

        public ViewHolder(@NonNull View itemView){

            super(itemView);

            tvAuthor =
                    itemView.findViewById(R.id.tvAuthor);

            tvContent =
                    itemView.findViewById(R.id.tvContent);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType){

        View view =
                LayoutInflater.from(context)
                        .inflate(R.layout.item_comment,
                                parent,
                                false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position){

        Comment comment =
                list.get(position);

        holder.tvAuthor.setText(
                comment.getAuthor());

        holder.tvContent.setText(
                comment.getContent());

    }

    @Override
    public int getItemCount(){

        return list.size();

    }

}
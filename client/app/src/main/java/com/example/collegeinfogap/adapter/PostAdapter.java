package com.example.collegeinfogap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinfogap.R;
import com.example.collegeinfogap.bean.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Post> list;

    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnPostClickListener{

        void onClick(Post post);

    }

    public interface OnPostLongClickListener{

        void onLongClick(Post post);

    }

    private OnPostClickListener listener;

    private OnPostLongClickListener longClickListener;

    public void setOnPostClickListener(OnPostClickListener listener){

        this.listener = listener;

    }

    public void setOnPostLongClickListener(
            OnPostLongClickListener listener){

        this.longClickListener = listener;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAuthor;
        TextView tvContent;

        TextView tvLike;
        TextView tvFavorite;
        TextView tvComment;

        ImageView ivPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent =
                    itemView.findViewById(R.id.tvContent);

            tvLike =
                    itemView.findViewById(R.id.tvLike);

            tvFavorite =
                    itemView.findViewById(R.id.tvFavorite);

            tvComment =
                    itemView.findViewById(R.id.tvComment);

            ivPost =
                    itemView.findViewById(R.id.ivPost);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        Post post = list.get(position);

        holder.tvTitle.setText(post.getTitle());

        holder.tvAuthor.setText(
                "作者：" + post.getAuthor());

        holder.tvContent.setText(
                post.getContent());

        holder.tvLike.setText(
                "👍 " + post.getLikeCount());

        holder.tvFavorite.setText(
                "⭐ " + post.getFavoriteCount());

        holder.tvComment.setText(
                "💬 " + post.getCommentCount());

        holder.itemView.setOnClickListener(v -> {

            if(listener!=null){

                listener.onClick(post);

            }

        });

        holder.itemView.setOnLongClickListener(v -> {

            if(longClickListener!=null){

                longClickListener.onLongClick(post);

            }

            return true;

        });

        if(post.getImage()!=null &&
                !post.getImage().isEmpty()){

            holder.ivPost.setVisibility(View.VISIBLE);

            holder.ivPost.setImageURI(
                    Uri.parse(post.getImage()));

        }else{

            holder.ivPost.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }

}

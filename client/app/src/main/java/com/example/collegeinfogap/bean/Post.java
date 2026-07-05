package com.example.collegeinfogap.bean;

public class Post {

    private int id;
    private String title;
    private String author;
    private String content;
    private String image;

    private int likeCount;
    private int favoriteCount;
    private int commentCount;

    public Post(
            int id,
            String title,
            String author,
            String content,
            String image){

        this.id=id;

        this.title=title;

        this.author=author;

        this.content=content;

        this.image=image;

    }

    public int getId(){

        return id;

    }

    public String getTitle(){

        return title;

    }

    public String getAuthor(){

        return author;

    }

    public String getContent(){

        return content;

    }

    public String getImage(){

        return image;

    }
    public int getLikeCount(){

        return likeCount;

    }

    public int getFavoriteCount(){

        return favoriteCount;

    }

    public int getCommentCount(){

        return commentCount;

    }
    public void setLikeCount(int likeCount){

        this.likeCount = likeCount;

    }

    public void setFavoriteCount(int favoriteCount){

        this.favoriteCount = favoriteCount;

    }

    public void setCommentCount(int commentCount){

        this.commentCount = commentCount;

    }

}
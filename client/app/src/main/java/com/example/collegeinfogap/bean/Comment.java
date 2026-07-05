package com.example.collegeinfogap.bean;

public class Comment {

    private String author;

    private String content;

    public Comment(String author,
                   String content){

        this.author = author;

        this.content = content;

    }

    public String getAuthor(){

        return author;

    }

    public String getContent(){

        return content;

    }

}
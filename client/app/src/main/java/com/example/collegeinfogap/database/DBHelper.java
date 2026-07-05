package com.example.collegeinfogap.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "college.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_USER = "user";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String userSql =
                "CREATE TABLE user(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT UNIQUE," +
                        "password TEXT)";

        db.execSQL(userSql);

        String postSql =
                "CREATE TABLE post(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT," +
                        "author TEXT," +
                        "content TEXT," +
                        "image TEXT)";

        db.execSQL(postSql);

        String commentSql =
                "CREATE TABLE comment(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "postTitle TEXT," +
                        "author TEXT," +
                        "content TEXT)";

        db.execSQL(commentSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS post");

        onCreate(db);

    }

}
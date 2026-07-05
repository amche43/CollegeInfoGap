package server;

import server.database.DBUtil;

import java.sql.Connection;

public class TestDB {

    public static void main(String[] args) {

        Connection connection =
                DBUtil.getConnection();

        if(connection!=null){

            System.out.println("数据库连接成功！");

        }else{

            System.out.println("数据库连接失败！");

        }

    }

}
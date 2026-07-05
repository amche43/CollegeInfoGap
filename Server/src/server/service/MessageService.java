package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MessageService {

    //发送消息
    public static String sendMessage(String sender,
                                     String receiver,
                                     String content){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "insert into message(sender,receiver,content) values(?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,sender);
            ps.setString(2,receiver);
            ps.setString(3,content);

            ps.executeUpdate();

            ps.close();
            conn.close();

            return "SUCCESS";

        }catch(Exception e){

            e.printStackTrace();

            return "FAIL";

        }

    }

    //获取聊天记录
    public static String getMessages(){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from message order by send_time asc";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            StringBuilder builder =
                    new StringBuilder();

            while(rs.next()){

                builder.append(rs.getInt("id"))
                        .append("|");

                builder.append(rs.getString("sender"))
                        .append("|");

                builder.append(rs.getString("receiver"))
                        .append("|");

                builder.append(rs.getString("content"))
                        .append("|");

                builder.append(
                                rs.getTimestamp("send_time").toString())
                        .append("\n");

            }

            rs.close();
            ps.close();
            conn.close();

            return builder.toString();

        }catch(Exception e){

            e.printStackTrace();

            return "";

        }

    }

}

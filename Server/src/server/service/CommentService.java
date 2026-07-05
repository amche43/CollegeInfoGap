package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommentService {

    public static boolean publish(
            int postId,
            String author,
            String content){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "insert into comment(postId,author,content) values(?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1,postId);

            ps.setString(2,author);

            ps.setString(3,content);

            int rows =
                    ps.executeUpdate();

            ps.close();

            conn.close();

            return rows>0;

        }catch(Exception e){

            e.printStackTrace();

            return false;

        }

    }
    public static String getComments(int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from comment where postId=? order by id asc";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, postId);

            ResultSet rs =
                    ps.executeQuery();

            StringBuilder builder =
                    new StringBuilder();

            while(rs.next()){

                builder.append(
                                rs.getString("author"))
                        .append("|");

                builder.append(
                                rs.getString("content"))
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
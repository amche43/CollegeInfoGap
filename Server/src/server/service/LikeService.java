package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LikeService {

    public static boolean like(
            String username,
            int postId){

        if(isLike(username,postId)){

            return true;

        }

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "insert into likes(username,postId) values(?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,username);

            ps.setInt(2,postId);

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
    public static boolean unLike(
            String username,
            int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "delete from likes where username=? and postId=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,username);

            ps.setInt(2,postId);

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
    public static boolean isLike(
            String username,
            int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from likes where username=? and postId=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,username);

            ps.setInt(2,postId);

            ResultSet rs =
                    ps.executeQuery();

            boolean exist =
                    rs.next();

            rs.close();

            ps.close();

            conn.close();

            return exist;

        }catch(Exception e){

            e.printStackTrace();

            return false;

        }

    }
    public static int getLikeCount(int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select count(*) from likes where postId=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1,postId);

            ResultSet rs =
                    ps.executeQuery();

            int count = 0;

            if(rs.next()){

                count = rs.getInt(1);

            }

            rs.close();

            ps.close();

            conn.close();

            return count;

        }catch(Exception e){

            e.printStackTrace();

            return 0;

        }

    }

}
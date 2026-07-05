package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FavoriteService {

    public static boolean favorite(
            String username,
            int postId){

        if(isFavorite(username,postId)){

            return true;

        }

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "insert into favorite(username,postId) values(?,?)";

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
    public static boolean unFavorite(
            String username,
            int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "delete from favorite where username=? and postId=?";

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
    public static boolean isFavorite(
            String username,
            int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from favorite where username=? and postId=?";

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
    public static String getFavoritePosts(
            String username){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select post.* " +
                            "from post,favorite " +
                            "where post.id=favorite.postId " +
                            "and favorite.username=? " +
                            "order by favorite.id desc";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,username);

            ResultSet rs =
                    ps.executeQuery();

            StringBuilder builder =
                    new StringBuilder();

            while(rs.next()){

                builder.append(
                                rs.getInt("id"))
                        .append("|");

                builder.append(
                                rs.getString("title"))
                        .append("|");

                builder.append(
                                rs.getString("author"))
                        .append("|");

                builder.append(
                                rs.getString("content"))
                        .append("|");

                builder.append(
                                rs.getString("image"))
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
    public static int getFavoriteCount(int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select count(*) from favorite where postId=?";

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
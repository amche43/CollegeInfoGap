package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class PostService {

    public static boolean publish(
            String title,
            String author,
            String content,
            String image){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "insert into post(title,author,content,image) values(?,?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,title);

            ps.setString(2,author);

            ps.setString(3,content);

            ps.setString(4,image);

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
    public static String getPosts(){
        return getPosts("NEW");
    }
    public static String getPosts(String type){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql;

            if(type.equals("NEW")){

                sql =
                        "select * from post order by id desc";

            }else if(type.equals("LIKE")){

                sql =
                        "select post.* " +
                                "from post " +
                                "left join likes " +
                                "on post.id=likes.postId " +
                                "group by post.id " +
                                "order by count(likes.id) desc, post.id desc";

            }else if(type.equals("FAVORITE")){

                sql =
                        "select post.* " +
                                "from post " +
                                "left join favorite " +
                                "on post.id=favorite.postId " +
                                "group by post.id " +
                                "order by count(favorite.id) desc, post.id desc";

            }else{

                sql =
                        "select post.* " +
                                "from post " +
                                "left join comment " +
                                "on post.id=comment.postId " +
                                "group by post.id " +
                                "order by count(comment.id) desc, post.id desc";

            }

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            StringBuilder builder =
                    new StringBuilder();

            while(rs.next()){

                int postId = rs.getInt("id");

                int likeCount = getLikeCount(postId);

                int favoriteCount = getFavoriteCount(postId);

                int commentCount = getCommentCount(postId);

                builder.append(rs.getInt("id"))
                        .append("|");

                builder.append(rs.getString("title"))
                        .append("|");

                builder.append(rs.getString("author"))
                        .append("|");

                builder.append(rs.getString("content"))
                        .append("|");

                builder.append(rs.getString("image"))
                        .append("|");

                builder.append(likeCount)
                        .append("|");

                builder.append(favoriteCount)
                        .append("|");

                builder.append(commentCount)
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
    public static String getMyPosts(String author){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from post where author=? order by id desc";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, author);

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
    public static boolean delete(int id){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "delete from post where id=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1,id);

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

    public static String search(String keyword){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from post " +
                            "where title like ? " +
                            "or content like ? " +
                            "order by id desc";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            String like =
                    "%" + keyword + "%";

            ps.setString(1, like);

            ps.setString(2, like);

            ResultSet rs =
                    ps.executeQuery();

            StringBuilder builder =
                    new StringBuilder();

            while(rs.next()){

                builder.append(rs.getInt("id"))
                        .append("|");

                builder.append(rs.getString("title"))
                        .append("|");

                builder.append(rs.getString("author"))
                        .append("|");

                builder.append(rs.getString("content"))
                        .append("|");

                builder.append(rs.getString("image"))
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
    private static int getLikeCount(int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select count(*) from likes where postId=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, postId);

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
    private static int getFavoriteCount(int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select count(*) from favorite where postId=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, postId);

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
    private static int getCommentCount(int postId){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select count(*) from comment where postId=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, postId);

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
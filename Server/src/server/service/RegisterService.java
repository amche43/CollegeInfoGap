package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterService {

    public static boolean register(String username,
                                   String password){

        try{

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "insert into user(username,password) values(?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,username);

            ps.setString(2,password);

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

}
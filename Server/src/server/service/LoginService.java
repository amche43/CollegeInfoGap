package server.service;

import server.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginService {

    public static boolean login(String username,
                                String password) {

        try {

            Connection conn =
                    DBUtil.getConnection();

            String sql =
                    "select * from user where username=? and password=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, username);

            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            boolean success =
                    rs.next();

            rs.close();

            ps.close();

            conn.close();

            return success;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

}
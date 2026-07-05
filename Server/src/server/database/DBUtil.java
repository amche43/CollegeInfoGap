package server.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    // 数据库地址
    private static final String URL =
            "jdbc:mysql://localhost:3306/college_info_gap?useSSL=false&serverTimezone=Asia/Shanghai";

    // 用户名
    private static final String USER = "root";

    // 你的MySQL密码
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }

}
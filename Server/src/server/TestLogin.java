package server;

import server.service.LoginService;

public class TestLogin {

    public static void main(String[] args) {

        boolean result =
                LoginService.login(
                        "admin",
                        "123456");

        System.out.println(result);

    }

}
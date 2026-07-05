package server;

import server.service.RegisterService;

public class TestRegister {

    public static void main(String[] args) {

        boolean result =
                RegisterService.register(
                        "test001",
                        "123456");

        System.out.println(result);

    }

}
package server;

import server.service.PostService;

public class TestPost {

    public static void main(String[] args) {

        boolean result =
                PostService.publish(

                        "408经验",

                        "admin",

                        "一定多刷真题！",

                        ""

                );

        System.out.println(result);

    }

}
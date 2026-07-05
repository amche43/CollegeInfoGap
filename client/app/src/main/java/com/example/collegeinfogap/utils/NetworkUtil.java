package com.example.collegeinfogap.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class NetworkUtil {

    // 电脑热点IP
    private static final String HOST = "172.20.10.3";

    private static final int PORT = 8888;

    public static String send(String msg){

        try{

            Socket socket =
                    new Socket(HOST,PORT);

            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            writer.write(msg);

            writer.newLine();

            writer.flush();

            String result =
                    reader.readLine();

            socket.close();

            return result;

        }catch (Exception e){

            e.printStackTrace();

            return "error";

        }

    }

}
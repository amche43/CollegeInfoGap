package com.example.collegeinfogap.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {

    public static String send(String... msgs){

        try{

            Socket socket =
                    new Socket(
                            "10.0.2.2",
                            8888);

            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            for(String msg:msgs){

                writer.write(msg);

                writer.newLine();

            }

            writer.flush();

            String result =
                    reader.readLine();

            socket.close();

            return result;

        }catch(Exception e){

            e.printStackTrace();

            return "ERROR";

        }

    }
    public static String sendMulti(String... msgs){

        try{

            Socket socket =
                    new Socket(
                            "10.0.2.2",
                            8888);

            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            for(String msg:msgs){

                writer.write(msg);

                writer.newLine();

            }

            writer.flush();

            StringBuilder builder =
                    new StringBuilder();

            String line;

            while((line=reader.readLine())!=null){

                builder.append(line);

                builder.append("\n");

            }

            socket.close();

            return builder.toString();

        }catch(Exception e){

            e.printStackTrace();

            return "";

        }

    }

}
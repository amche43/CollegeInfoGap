package server;

import java.io.*;
import java.net.Socket;

public class TestSocketGetMyPost {

    public static void main(String[] args)
            throws Exception {

        Socket socket =
                new Socket("127.0.0.1",8888);

        BufferedWriter writer =
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

        writer.write("GET_MY_POST");
        writer.newLine();

        writer.write("admin");
        writer.newLine();

        writer.flush();

        String line;

        while((line=reader.readLine())!=null){

            System.out.println(line);

        }

        socket.close();

    }

}
package server;

import java.io.*;
import java.net.Socket;

public class TestSocketIsFavorite {

    public static void main(String[] args)throws Exception{

        Socket socket =
                new Socket(
                        "127.0.0.1",
                        8888);

        BufferedWriter writer =
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

        writer.write("IS_FAVORITE");
        writer.newLine();

        writer.write("admin");
        writer.newLine();

        writer.write("1");
        writer.newLine();

        writer.flush();

        System.out.println(
                reader.readLine());

        socket.close();

    }

}
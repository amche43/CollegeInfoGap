package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TestSocketRegister {

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

        writer.write("REGISTER");
        writer.newLine();

        writer.write("socket001");
        writer.newLine();

        writer.write("123456");
        writer.newLine();

        writer.flush();

        System.out.println(reader.readLine());

        socket.close();

    }

}
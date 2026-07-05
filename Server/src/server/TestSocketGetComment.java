package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TestSocketGetComment {

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

        writer.write("GET_COMMENT");
        writer.newLine();

        // 测试帖子ID
        writer.write("1");
        writer.newLine();

        writer.flush();

        String line;

        while((line = reader.readLine()) != null){

            System.out.println(line);

        }

        socket.close();

    }

}
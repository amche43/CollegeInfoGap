package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import server.service.LoginService;
import server.service.RegisterService;
import server.service.PostService;
import server.service.AIService;
import server.service.MessageService;

public class Server {

    private static ArrayList<PrintWriter> clients =
            new ArrayList<>();

    public static void main(String[] args)
            throws Exception {

        ServerSocket server =
                new ServerSocket(8888);

        System.out.println("服务器启动...");

        while(true){

            Socket socket =
                    server.accept();

            System.out.println(
                    "客户端连接：" +
                            socket.getInetAddress());

            new ClientThread(socket).start();

        }

    }

    static class ClientThread extends Thread{

        Socket socket;

        public ClientThread(Socket socket){

            this.socket=socket;

        }

        @Override
        public void run(){

            try{

                BufferedReader reader=
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                PrintWriter writer=
                        new PrintWriter(
                                socket.getOutputStream(),
                                true);

                clients.add(writer);

                String cmd;

                while ((cmd = reader.readLine()) != null) {

                    System.out.println("命令：" + cmd);

                    if (cmd.equals("LOGIN")) {

                        String username = reader.readLine();

                        String password = reader.readLine();

                        System.out.println("用户名：" + username);

                        System.out.println("密码：" + password);

                        boolean success =
                                LoginService.login(
                                        username,
                                        password);

                        if(success){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if (cmd.equals("REGISTER")) {

                        String username = reader.readLine();

                        String password = reader.readLine();

                        System.out.println("注册用户：" + username);

                        boolean success =
                                RegisterService.register(
                                        username,
                                        password);

                        if(success){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("POST")){

                        String title =
                                reader.readLine();

                        String author =
                                reader.readLine();

                        String content =
                                reader.readLine();

                        String image =
                                reader.readLine();

                        System.out.println("发布帖子");

                        System.out.println(title);

                        boolean success =
                                PostService.publish(
                                        title,
                                        author,
                                        content,
                                        image);

                        if(success){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("GET_POST")){

                        String type =
                                reader.readLine();

                        String result =
                                PostService.getPosts(type);

                        writer.println(result);

                        writer.flush();

                        socket.close();

                        break;

                    }
                    if(cmd.equals("SEARCH")){

                        String keyword =
                                reader.readLine();

                        String result =
                                PostService.search(keyword);

                        writer.print(result);

                        writer.flush();

                        socket.close();

                        break;

                    }
                    if(cmd.equals("COMMENT")){

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        String author =
                                reader.readLine();

                        String content =
                                reader.readLine();

                        boolean ok =
                                server.service.CommentService.publish(
                                        postId,
                                        author,
                                        content);

                        if(ok){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("GET_COMMENT")){

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        String result =
                                server.service.CommentService.getComments(postId);

                        writer.print(result);

                        writer.flush();

                        socket.close();

                        break;

                    }
                    if(cmd.equals("GET_MY_POST")){

                        String author =
                                reader.readLine();

                        String result =
                                PostService.getMyPosts(author);

                        writer.print(result);

                        writer.flush();

                        socket.close();

                        break;

                    }
                    if(cmd.equals("DELETE_POST")){

                        int id =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean success =
                                PostService.delete(id);

                        if(success){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("FAVORITE")){

                        String username =
                                reader.readLine();

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean ok =
                                server.service.FavoriteService.favorite(
                                        username,
                                        postId);

                        if(ok){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("LIKE")){

                        String username =
                                reader.readLine();

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean ok =
                                server.service.LikeService.like(
                                        username,
                                        postId);

                        if(ok){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("UNFAVORITE")){

                        String username =
                                reader.readLine();

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean ok =
                                server.service.FavoriteService.unFavorite(
                                        username,
                                        postId);

                        if(ok){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("UNLIKE")){

                        String username =
                                reader.readLine();

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean ok =
                                server.service.LikeService.unLike(
                                        username,
                                        postId);

                        if(ok){

                            writer.println("SUCCESS");

                        }else{

                            writer.println("FAIL");

                        }

                    }
                    if(cmd.equals("IS_FAVORITE")){

                        String username =
                                reader.readLine();

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean ok =
                                server.service.FavoriteService
                                        .isFavorite(
                                                username,
                                                postId);

                        if(ok){

                            writer.println("YES");

                        }else{

                            writer.println("NO");

                        }

                    }
                    if(cmd.equals("IS_LIKE")){

                        String username =
                                reader.readLine();

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        boolean ok =
                                server.service.LikeService.isLike(
                                        username,
                                        postId);

                        if(ok){

                            writer.println("YES");

                        }else{

                            writer.println("NO");

                        }

                    }
                    if(cmd.equals("GET_LIKE_COUNT")){

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        int count =
                                server.service.LikeService
                                        .getLikeCount(postId);

                        writer.println(count);

                    }
                    if(cmd.equals("GET_FAVORITE")){

                        String username =
                                reader.readLine();

                        String result =
                                server.service.FavoriteService
                                        .getFavoritePosts(
                                                username);

                        writer.print(result);

                        writer.flush();

                        socket.close();

                        break;

                    }
                    if(cmd.equals("GET_FAVORITE_COUNT")){

                        int postId =
                                Integer.parseInt(
                                        reader.readLine());

                        int count =
                                server.service.FavoriteService
                                        .getFavoriteCount(postId);

                        writer.println(count);

                    }
                    if(cmd.equals("AI")) {

                        String type = reader.readLine();
                        String content = reader.readLine();

                        String prompt = "";

                        if(type.equals("TITLE")) {
                            prompt = "请生成标题：" + content;
                        }

                        if(type.equals("SUMMARY")) {
                            prompt = "请总结内容：" + content;
                        }

                        if(type.equals("TAG")) {
                            prompt = "请生成标签：" + content;
                        }

                        String result = AIService.askAI(prompt);

                        writer.println(result);
                    }
                    if(cmd.equals("SEND_MESSAGE")){

                        String sender = reader.readLine();

                        String receiver = reader.readLine();

                        String content = reader.readLine();

                        String result =
                                MessageService.sendMessage(
                                        sender,
                                        receiver,
                                        content);

                        writer.println(result);

                    }
                    else if(cmd.equals("GET_MESSAGE")){

                        writer.print(
                                MessageService.getMessages());

                        writer.flush();

                        socket.close();

                        break;

                    }

                }

            }catch(Exception e){

                e.printStackTrace();

            }

        }

    }

}
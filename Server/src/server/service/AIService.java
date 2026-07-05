package server.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIService {

    public static String askAI(String prompt) {

        try {

            URL url = new URL("http://localhost:11434/api/generate");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{"
                    + "\"model\":\"llama3\","
                    + "\"prompt\":\"" + prompt + "\","
                    + "\"stream\":false"
                    + "}";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            String result = sb.toString();

            int start = result.indexOf("\"response\":\"") + 12;
            int end = result.indexOf("\"", start);

            return result.substring(start, end);

        } catch (Exception e) {
            e.printStackTrace();
            return "AI请求失败";
        }
    }
}
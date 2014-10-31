package edu.sjsu.techknowgeek.fingerninja;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkManager {
    public static final int PORT = 7890;
    public static final String[] SCORE_CATEGORIES = {
            "User highscore:",
            "All highscore:",
            "All Avg:",
            "User Avg:",
            "User Past Hr Avg:",
            "User Past Wk Avg:",
            "User Past Mn Avg:",
    };
    private static Socket socket;
    private static String IP_ADDRESS;
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static String currentUser;

    public static void setUser(String aUserName) {
        currentUser = aUserName;
    }

    public static boolean registerUser() {
        String response = messageServer("register:"+ currentUser);

        if(response.equals("Okay\n")){
            return true;
        } else if (response.equals("Sorry\n")){
            return false;
        } else {
            System.err.print("Something funny with the server\n");
            return false;
        }
    }

    public static boolean sendGameStats(String aGameName, int score){
        String response = messageServer(
                String.format("%s\t%s\t%s\t%d","results:", currentUser, aGameName, score));

        if(response.equals("Okay\n")){
            return true;
        } else if (response.equals("Sorry\n")){
            return false;
        } else {
            System.err.print("Something funny with the server\n");
            return false;
        }
    }

    public static String[] getGameStats(){
        //String response = messageServer("statistics:" + currentUser);
        String response = "game1\t2\t1100\t2\t4\t10\t7\ngame2\t4\t2200\t4\t8\t20\t14\ngame3\t4\t2200\t4\t8\t20\t14\n.";
        if(!response.matches("([A-z]*(\t\\d*){6}\n)*[.]") ){
            System.err.print("Something funny with parsing stats\n");
        }

        String[] games = response.split("\n");

        ArrayList<String> parsedResp = new ArrayList<String>();
        for(int i = 0; i < games.length-1; i++) {
            String[] scores = games[i].split("\t");
            for(int j=1; j < SCORE_CATEGORIES.length; j++) {
                parsedResp.add(scores[j]);
            }
        }

        return parsedResp.toArray(new String[parsedResp.size()]);
    }

    public static void setIP(String anIP){
        IP_ADDRESS = anIP;
    }

    private static String messageServer(String input){
        String output = "";
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            if (socket != null) {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
                writer.write(input);
                writer.flush();
                output = reader.readLine();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
}

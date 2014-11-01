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

    public static String getUser() {
        return currentUser;
    }

    public static String getIP_ADDRESS() { return IP_ADDRESS;}

    public static boolean registerUser() {
        String response = messageServer("register:"+ currentUser);

        if(response.equals("Okay")){
            return true;
        } else if (response.equals("Sorry")){
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
        String response = messageServer("statistics:" + currentUser);
        ArrayList<String> parsedResp = new ArrayList<String>();

        if(!response.matches("([A-z]*(\t\\d*){6}\n)*[.]") ){
            System.err.print("Something funny with parsing stats\n");
        }
        if(response.equals(".")) {
            parsedResp.add(response);
            return parsedResp.toArray(new String[1]);
        }

        String[] games = response.split("\n");

        for(int i = 0; i < games.length-1; i++) {
            String[] scores = games[i].split("\t");
            for(int j=1; j < scores.length; j++) {
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
                writer.write(input+"\n");
                writer.flush();
                String currLine = "";
                while(!currLine.equals(".")) {
                    currLine = reader.readLine();
                    output += currLine + "\n";
                }

                socket.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return output;
    }
}

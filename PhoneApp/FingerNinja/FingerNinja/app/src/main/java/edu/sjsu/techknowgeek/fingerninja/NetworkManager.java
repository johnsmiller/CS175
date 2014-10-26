package edu.sjsu.techknowgeek.fingerninja;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class NetworkManager extends HashMap<NetworkManager.ScoreCats, Integer> {
    public enum ScoreCats {
        USER,
        OVERALL,
        AVG_OVERALL,
        AVG_USER_HR,
        AVG_USER_WK,
        AVG_USER_M
    }

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private void createSocket(String ip, String port, String socketData) {

        try {
            socket = new Socket(ip, Integer.parseInt(port));
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String aUserName) {
        String response = messageServer("register:"+aUserName);

        if(response.equals("Okay\n")){
            return true;
        } else if (response.equals("Sorry\n")){
            return false;
        } else {
            System.err.print("Something funny with the server\n");
            return false;
        }
    }

    public boolean sendGameStats(String aUserName, String aGameName, int score){
        String response = messageServer(
                String.format("%s\t%s\t%s\t%d","results:", aUserName, aGameName, score));

        if(response.equals("Okay\n")){
            return true;
        } else if (response.equals("Sorry\n")){
            return false;
        } else {
            System.err.print("Something funny with the server\n");
            return false;
        }
    }

    public void getGameStats(String aUserName){
        String response = messageServer("statistics:" + aUserName);

        if(!response.matches("[A-z]*(\t\\d*){6}") ){
            System.err.print("Something funny with parsing stats\n");
        }

        int i = 1;
        String[] parts = response.split("\t");
        while(i < parts.length && i <ScoreCats.values().length){
            this.put(ScoreCats.values()[i], Integer.parseInt(parts[i]));
        }
    }

    private String messageServer(String input){
        String output = "";
        try {
            writer.write(input);
            writer.flush();
            output = reader.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return output;
    }
}

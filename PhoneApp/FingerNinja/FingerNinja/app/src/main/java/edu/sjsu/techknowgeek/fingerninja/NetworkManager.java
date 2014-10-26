package edu.sjsu.techknowgeek.fingerninja;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class NetworkManager {

    private static Socket socket;
    private static BufferedWriter writer;
    private static BufferedReader reader;

    public static void createSocket(String ip, int port) {

        try {
            socket = new Socket(ip, port);
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String aUserName) {
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

    public static boolean sendGameStats(String aUserName, String aGameName, int score){
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

    public static String[] getGameStats(String aUserName){
        String response = messageServer("statistics:" + aUserName);

        if(!response.matches("[A-z]*(\t\\d*){6}") ){
            System.err.print("Something funny with parsing stats\n");
        }
        return response.split("\t");
    }

    private static String messageServer(String input){
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

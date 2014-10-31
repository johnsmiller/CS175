/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerciseserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */

/*
 On connection, send: Fingercise Server
 three kinds of messages: (1) a register name message, (2) send game result, (3) get statistics.

 "register:Bobby Smith"
 return: "OK" || "Sorry"

 "result:player_name\tgame_name\tscore"
 return: "OK" || "Sorry"

 "statistics:player_name"
 return:
 game1_name\tuser_high_score\tover_all_high_score\tover_all_avg_score\tuser_avg_score_last_hour\tuser_avg_score_last_week\tuser_avg_score_last_month
 game2_name\tuser_high_score\tover_all_high_score\tover_all_avg_score\tuser_avg_score_last_hour\tuser_avg_score_last_week\tuser_avg_score_last_month
 game3_name\tuser_high_score\tover_all_high_score\tover_all_avg_score\tuser_avg_score_last_hour\tuser_avg_score_last_week\tuser_avg_score_last_month
 */
public class FingerciseServer extends Thread {

    private static final int PORT = 7890;
    private static final int NUM_CONNECT = 1;
    private static final String WELCOME_MESSAGE = "Fingercise Server";
    private static final String SUCCESS_MESSAGE = "Okay\n";
    private static final String FAILURE_MESSAGE = "Sorry\n";
    private String message;

    private final HashMap<String, Game> games;
    private ArrayList<String> names;

    private final int MAX_GAMES = 3; //Must be greater than -1

    private FingerciseServer() {
        games = new HashMap<>(MAX_GAMES);
        names = new ArrayList<>();
    }

    public static void main(String args[]) {
        FingerciseServer myServer = new FingerciseServer();
        if (myServer != null) {
            myServer.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            ServerSocket server = null;
            try {
                server = new ServerSocket(PORT, NUM_CONNECT);
                Socket client = server.accept();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream()));

                writer.write(WELCOME_MESSAGE);

                message = reader.readLine();

                if (message.contains("register")) {
                    writer.write(register());
                } else if (message.contains("result:")) {
                    writer.write(result());
                } else if (message.contains("statistics:")) {
                    writer.write(statistics());
                } else {
                    writer.write("Sorry\n");
                }

            } catch (IOException ie) {
                ie.printStackTrace();
            } finally {
                if (server != null) {
                    try {
                        server.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FingerciseServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private String register() {
        String userName = (message.substring(message.indexOf(":") + 1)).trim();

        for (Game g : games.values()) {
            if (!g.addUser(userName)) {
                return FAILURE_MESSAGE;
            }
        }

        if (games.size() < MAX_GAMES) {
            names.add(userName);
        }

        return SUCCESS_MESSAGE;
    }

    private String result() {
        String result = (message.substring(message.indexOf(":") + 1)).trim();

        String[] split = result.split("\t"); //Assume 3 variables: User Name, Game name, and score

        if (!games.containsKey(split[1])) //If game has not yet been made, initialize
        {
            Game game = new Game(split[1]);
            for (String s : names) {
                game.addUser(s);
            }
        }

        for (Game g : games.values()) {
            if (!g.addScore(split[0], Integer.parseInt(split[2]))) {
                return FAILURE_MESSAGE; //User has not been previously created
            }
        }

        return SUCCESS_MESSAGE;
    }

    private String statistics() {
        String userName = (message.substring(message.indexOf(":") + 1)).trim();
        String ret = "";

        for (Game g : games.values()) {
            for (String s : g.getStats(userName)) {
                ret += s + "\t";
            }
            ret += "\n";
        }

        ret += ".";

        return ret;
    }

    public class Game {

        private final String NAME;

        private int overallHighScore;
        private int allScores; //All scores added to this int
        private int numOfScores; //number of scores added

        HashMap<String, User> users;

        public Game(String name) {
            NAME = name;
            overallHighScore = 0;
            allScores = 0;
            numOfScores = 0;

            users = new HashMap<>();
        }

        public boolean addScore(String userName, int score) {
            if (!users.containsKey(userName)) {
                return false;
            } else {
                if (score > overallHighScore) {
                    overallHighScore = score;
                }
                allScores += score;
                numOfScores++;

                return users.get(userName).addScore(score);
            }
        }

        public boolean addUser(String userName) {
            if (!users.containsKey(userName)) {
                users.put(userName, new User(userName));
                return true;
            } else {
                return false;
            }
        }

        /**
         * String[0] = game name String[1] = user high score String[2] = over
         * all high score String[3] = overall average score String[4] = user
         * average score last hour String[5] = user average score last week
         * String[6] = user average score last month
         *
         * @param userName
         * @return String[] of size 7 or null if user does not exist
         */
        public String[] getStats(String userName) {
            if (!users.containsKey(userName)) {
                return null;
            }

            User user = users.get(userName);
            String[] ret = new String[7];

            String overallAvg = ((double) allScores / numOfScores) + "";
            int decIndex = (overallAvg.contains(".")) ? overallAvg.indexOf(".") : overallAvg.length();
            Double[] avgs = user.getUserAverages();

            ret[0] = NAME;
            ret[1] = user.getUserHighScore() + "";
            ret[2] = overallHighScore + "";
            //concat average score to 2 decimal places
            ret[3] = (decIndex < overallAvg.length() - 3)
                    ? overallAvg.substring(0, overallAvg.length() - 2) : overallAvg;
            ret[4] = avgs[0] + "";
            ret[5] = avgs[1] + "";
            ret[6] = avgs[2] + "";

            return ret;
        }

    }

    public class User {

        private final String NAME;
        private int highScore;
        private final TreeMap<GregorianCalendar, Integer> SCORES;

        private final static long hourMili = 3600000; //number of miliseconds in an hour
        private final static long weekMili = 604800000; //number of miliseconds in a week
        private final static long monthMili = (262800000 * 10); //number of miliseconds in a month

        public User(String name) {
            NAME = name;
            highScore = 0;
            SCORES = new TreeMap<>();
        }

        public boolean addScore(int score) {
            if (score > highScore) {
                highScore = score;
            }
            SCORES.put(new GregorianCalendar(), score);
            return true;
        }

        @Override
        public String toString() {
            return NAME;
        }

        public int getUserHighScore() {
            return highScore;
        }

        /**
         * Double[0] = User average score in last hour Double[1] = User average
         * score in last week Double[2] = User average score in last month
         *
         * @return Double[] of size 3
         */
        public Double[] getUserAverages() {
            Long currentTime = new GregorianCalendar().getTimeInMillis();
            GregorianCalendar temp = new GregorianCalendar();
            TreeMap<GregorianCalendar, Integer> mapValues = SCORES;
            Double[] ret = new Double[3];
            Integer[] intValues;

            //Get Scores from last month, store in valuesIn, and store map in values
            temp.setTimeInMillis(currentTime - monthMili);
            mapValues = (TreeMap<GregorianCalendar, Integer>) mapValues.tailMap(temp, true);
            intValues = mapValues.values().toArray(new Integer[0]);
            ret[2] = getAverage(intValues);

            //Get Scores from last week, store in valuesIn, and store map in values
            temp.setTimeInMillis(currentTime - weekMili);
            mapValues = (TreeMap<GregorianCalendar, Integer>) mapValues.tailMap(temp, true);
            intValues = mapValues.values().toArray(new Integer[0]);
            ret[1] = getAverage(intValues);

            //Get Scores from last hour, store in valuesIn, and store map in values
            temp.setTimeInMillis(currentTime - hourMili);
            mapValues = (TreeMap<GregorianCalendar, Integer>) mapValues.tailMap(temp, true);
            intValues = mapValues.values().toArray(new Integer[0]);
            ret[0] = getAverage(intValues);

            return ret;
        }

        private double getAverage(Integer[] intValues) {
            double score = 0.0;

            for (Integer i : intValues) {
                score += i;
            }

            return score / intValues.length;
        }
    }

}

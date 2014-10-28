import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class MyServer extends Thread
{
	private static final int PORT=7890;
	private static final int NUM_CONNECT=1;
	private string message;
	private MyServer() {}
	private HashMap<K, V> members;

	//statistics values
	int userHighScore=0; //high score for that user
	int overallHighScore=0; //high score for all users
	int overallAverageScore=0; //average score for all users
	int userAverageScoreHour; //average for all users over last hour
	int userAverageScoreWeek; //average for all users over last week
	int userAverageScoreMonth; //average for all users over month
	

	public static void main(String args[])
	{
		MyServer myServer = new MyServer();
		if(myServer !=null) {myServer.start();}
	}


	public class GameData{
		public string gameName;
		public int gameScore;
		public int gameTime;
	}



	public void run()
	{
		try
		{
			ServerSocket server = new ServerSocket(PORT, NUM_CONNECT);
			Socket client = server.accept();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(client.getInputStream()));

			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(client.getOutputStream()));

			//Code added by Nabil
			writer.write("Fingercise Server");
			// do some reading and writing
			message = reader.readLine();
			String delims = ":\\t";
			String[] entry = message.split(delims);
			ArrayList<GameData> blah = members.get(entry[1]);
			
			if(entry[0]="register")
			{
				//code to Register a new user
				members.put(entry[1], new ArrayList<GameData>());
				System.out.println("Okay");
			}
			else if(entry[0]="result")
			{
				//result from a single game recorded to database
				GameData e = new GameData();	 
				blah.add(e);
				e.gameName = entry[2];
				e.gameScore = entry[3];
			}
			else if(entry[0]="statistics")
			{
				GameData temp = new GameData;
				for(int x=0;x<blah.size();x++)
				{
					temp = blah.get(x);
					if(temp.gameScore>overallHighScore)
					{
						overallHighScore = temp;
					}
				}
				//returns statistics from database for a single user
				
			}
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
}
package Pong;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Exceptions.UserCanceledException;
import other.Score;

public class ServerStuff {
	
	private String IP;
	private int port;// = 25568;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private Thread keepWriting;
	private Thread keepReading;
	private int gameUpdateSleep;
	private Player playerHost, playerClient;
	private boolean isHost;
	private Ball ball;
	private boolean connected = false;
	private Score score;
	private boolean canceled = false;
	private MultiplayerPong parent;
	
	public ServerStuff(int gameUpdateSleep, Player playerHost, Player playerClient, Ball ball, Score score, boolean isHost, MultiplayerPong thisGame){
		this.gameUpdateSleep = gameUpdateSleep;
		this.playerHost = playerHost;
		this.playerClient = playerClient;
		this.isHost = isHost;
		this.ball = ball;
		this.score = score;
		this.parent = thisGame;
	}
	
	public boolean checkIfPortOpen(int portNumber){
		
		try {
			ServerSocket temp = new ServerSocket(portNumber, 1);
			temp.close();
			say("Port " + portNumber + " is available");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			say("Port " + portNumber + " is unavailable");
			return false;
		}
	}

	public void setupHost(int daPort) throws UserCanceledException{
		//this.port = Integer.parseInt(port);//JOptionPane.showInputDialog(portMessage, "25568"));
		port = daPort;
		
		
			
			try {
				say("setting up server");
				server = new ServerSocket(port, 1);
			} catch (IOException e) {
				say("Error- Port " + port + " is already being used");
				e.printStackTrace();
			}
			
			try {
				waitForConnectionHost();
				setupNewStreams();
				connected = true;
			} catch (Exception e) {
				if (canceled){
					canceled = false;
					throw new UserCanceledException();
				}
				else
					say("Connection could not be established");
			}
		
		if (connected){
			keepReading = new Thread(new Runnable() {
				public void run() {
					while (true){
						try {
							read();
							sleep(gameUpdateSleep/2);
						} catch (IOException e) {
							say("Lost Connection to Client");
							closeServer();
							break;
						}
					}
				}
			});
			keepReading.start();
			
			keepWriting = new Thread(new Runnable() {
				public void run() {
					say("at run");
					while (true){
						try {
							write();
							sleep(gameUpdateSleep);
						} catch (IOException e) {
							say("Lost Connection to Client");
							closeServer();
							break;
						}
					}
				}
			});
			keepWriting.start();
		}
	}

	public void waitForConnectionHost() throws IOException {
		say("Waiting to Connect");
		// Will Stay here until connection is made
		connection = server.accept();
		say("Now Connected to " + connection.getInetAddress().getHostName()
				+ " @ " + connection.getInetAddress().getHostAddress());
	}
	
	public void setupNewStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
	
		input = new ObjectInputStream(connection.getInputStream());
		say("Streams setup");
	}

	public void write() throws IOException {
		if (isHost){
			//say("writing as host");
			//TODO combine all this into a string then parse on client side
			int[] y = new int[5];
			y[0] = playerHost.getY();
			y[1] = ball.getX();
			y[2] = ball.getY();
			y[3] = score.getLeftScore();
			y[4] = score.getRightScore();
			output.writeObject(y);
			output.flush();
		}
		else
		{
			//say("writing as client");
			output.writeInt(playerClient.getY());
			output.flush();
		}
	}

	public void setupClient(String daIP, int daPort) throws UserCanceledException {
		
//		IP = JOptionPane.showInputDialog("Enter IP Address \n0 for LAN",  "0");
//		port = Integer.parseInt(JOptionPane.showInputDialog("Enter # for the port", "25568"));
		
		IP = daIP;
		port = daPort;
		
		try {
			waitForConnectionClient();
			setupNewStreams();
			connected = true;
		} catch (Exception e) {
			
			if (canceled){
				canceled = false;
				throw new UserCanceledException();
			}
				
			else
				say("Connection could not be esablished 3");
		}
		
		if (connected){
			keepWriting = new Thread(new Runnable() {
				public void run() {
					while (true){
						try {
							write();
							sleep(gameUpdateSleep);
						} catch (IOException e) {
							say("Lost Connection to the Host");
							closeServer();
							break;
						}
					}
				}
			});
			keepWriting.start();
			
			keepReading = new Thread(new Runnable() {
				public void run() {
					while (true){
						try {
							read();
							sleep(gameUpdateSleep/2);
						} catch (IOException e) {
							say("Lost Connection to the Host");
							closeServer();
							break;
						}
					}
				}
			});
			keepReading.start();
		}
	}

	public void waitForConnectionClient() throws IOException {
		while (connection == null && !canceled){
			say("Trying to connect");
			try{
				connection = new Socket(IP, port);
			}catch(IOException e){}
		}
		say("Now connected to " + connection.getInetAddress());
	}

	public void read() throws IOException {
		if (isHost){
			playerClient.setY(input.readInt());
		}
		else{
			try{
				int[] temp = (int[]) input.readObject();
				//say(temp[0] + " " + temp[1] + " "+ temp[2]);
				playerHost.setY(temp[0]);
				ball.setX(temp[1]);
				ball.setY(temp[2]);
				score.setLeftScore(temp[3]);
				score.setRightScore(temp[4]);
			}catch(Exception e){}
		}
	}
	
	public void cancelWait(){
		canceled = true;
		
		if (isHost){
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeServer(){
		
		try{
			parent.reset();
		}catch(Exception e){}
		
		say("Closing Server");
		try {
			output.close();
		} catch (Exception e) {
		}
		try {
			input.close();
		} catch (Exception e) {
		}
		try {
			server.close();
		} catch (Exception e) {
		}
		try {
			connection.close();
		} catch (Exception e) {
		}
	}
	
	public void sleep(int i){
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void say(String s) {
		if (isHost) System.out.println(this.getClass().getName() + ": " + "Server: " + s);
		else System.out.println(this.getClass().getName() + ": " + "Client: " + s);
	}
}
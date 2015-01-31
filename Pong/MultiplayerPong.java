package Pong;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import other.Modes;
import other.Score;
import other.UpdatesAndFrames;
import other.WaitingAnimated;
import Themes.ThemeSelection;
import Themes.Themes;
import Buttons.InputBox;
import Buttons.SimpleButton;
import Buttons.ThemeButton;
import Buttons.TriangleButton;
import Exceptions.UserCanceledException;

public class MultiplayerPong extends JFrame implements Runnable, KeyListener, MouseMotionListener, MouseListener{

	//2500 Lines on 10/25/2014
	
	private static final long serialVersionUID = 1L;
	//private String mode = "menu", menu = "menu", local = "local", online = "online", hostOrJoin = "Host?";
	private String errorMessage = null, mouseXY = "";
	private Modes mode = Modes.MAIN_MENU;
	private final int updateSleep = 40, updateTarget = 1000/updateSleep, width = 800, height = 600;
	private int errorMessageX = 0, errorMessageY = 0;
	private boolean isHost, f1 = false, canceled = false;
	private SimpleButton localButton,onlineButton, hostButton, joinButton, backButton, addButton, removeButton, apply, cancel;
	private TriangleButton leftArrow, rightArrow;
	private ThemeButton themeButton;
	private Image dbi;
	private Graphics dbg;
	private ArrayList<MenuBall> menuBall = new ArrayList<MenuBall>();
	private Player playerHost, playerClient;
	private UpdatesAndFrames uaf;
	private Ball ball;
	private ServerStuff server;
	private Score score;
	private ThemeSelection themeSelector;
	private Themes theme;
	private Thread serverWait;
	private MultiplayerPong ThisGame;
	private WaitingAnimated waitingText;
	private InputBox hostPort, clientIP, clientPort;
	
	public MultiplayerPong() {
		super("Online Pong");
		ThisGame = this;
		themeSelector = new ThemeSelection();
		theme = new Themes(themeSelector.getCurrentTheme());
		setBackground(theme.getBackgroundColor());
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setVisible(true);
		setResizable(false);
		
		final int buttonWidth = 300, buttonHeight = 70, arcs = 30;
		localButton = new SimpleButton("Local", 50, 270, buttonWidth, buttonHeight, arcs, arcs, theme);
		onlineButton = new SimpleButton("Online", 50, 360, buttonWidth, buttonHeight, arcs, arcs, theme);
		themeButton = new ThemeButton("Current Theme:", theme.getFont(), 430, 270, 310, 520-270-40, theme.getButtonColor(), arcs, arcs, theme);
		final int yoff = 220;
		hostButton = new SimpleButton("Host a Game", 30, 100+yoff, buttonWidth, buttonHeight, arcs, arcs, theme);
		joinButton = new SimpleButton("Join a Game", 30, 190+yoff, buttonWidth, buttonHeight, arcs, arcs, theme);
		backButton = new SimpleButton("Back", 30, 280+yoff, buttonWidth, buttonHeight, arcs, arcs, theme);
		addButton = new SimpleButton("+", 20, 530, 50, 50, arcs, arcs, theme);
		removeButton = new SimpleButton("-", 80, 530, 50, 50, arcs, arcs, theme);
		uaf = new UpdatesAndFrames();
		leftArrow = new TriangleButton("left", 435+10, 410-30-20+2, 50+10, 50+20+10, theme);
		rightArrow = new TriangleButton("right", 730-10, 410-30-20+2, 50+10, 50+20+10, theme);
		
		final int balls = 5;
		for (int i =0; i < balls;i++)
			menuBall.add(new MenuBall(20, width, height, theme, localButton, onlineButton, addButton, removeButton, themeButton));
		
	}


	@Override
	public void run() {

		Thread paintIt = new Thread(new Runnable() {
			public void run() {
				while (true){
					repaint();
					uaf.addFrameUpdate(); //1.5 Million FPS
					sleep(updateSleep/2);
				}
			}
		});

		Thread gameLoop = new Thread(new Runnable() {
			public void run() {
				gameTimer();
			}
		});

		paintIt.start();
		gameLoop.start();
	}
	
	public void gameTimer(){
		  final double GAME_HERTZ = updateTarget;
	      final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
	      double lastUpdateTime = System.nanoTime();
	      
	      while (true)
	      {
	         double now = System.nanoTime();
	            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
	            {
	            	gameUpdate();
	               lastUpdateTime += TIME_BETWEEN_UPDATES;
	               uaf.addGameUpdate();
	               uaf.update();
	               sleep(updateSleep/2);
	            }
	            if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES){
	               lastUpdateTime = now - TIME_BETWEEN_UPDATES;
	            }
	      }
	}
	
	private void gameUpdate(){
		if (mode == Modes.MAIN_MENU)
			menuUpdate();
		else if (mode == Modes.ONLINE_MENU)
			hostOrJoinUpdate();
		else if (mode == Modes.LOCAL)
			localUpdate();
		else if (mode == Modes.ONLINE)
			if (isHost)
				hostGameUpdate();
			else //Client
				clientGameUpdate();
	}
	
	
	private void setupClient() {
		
		if (serverWait == null){
			serverWait = new Thread(new Runnable() {
				public void run() {
					server = new ServerStuff(updateSleep, playerHost, playerClient, ball, score, isHost, ThisGame);
					int portNumber = Integer.parseInt(clientPort.getText());
					String ip = clientIP.getText();
					
					if (portNumber <= 65535){
						try{
							waitingText = new WaitingAnimated(80,200, "Trying to Connect to the Host", .5, theme);
							apply = null;
							cancel = new SimpleButton("Cancel", 500, 500, 100, 40, 20, 20, theme);
							cancel.setHovered(true);
							server.setupClient(ip, portNumber);
							mode = Modes.ONLINE;
							errorMessage = null;
						}catch(UserCanceledException e){
							say("User Canceled Wait");
							waitingText = null;
						}
					}
					else{
						errorMessage = "Port must be less than 65536";
						errorMessageX = 420;
						errorMessageY = 390;
					}
					
					serverWait = null;
				}
			});
			serverWait.start();
		}
	}
	
	public void reset(){
		mode = Modes.MAIN_MENU;
		say("resetting Buttons");
		cancel = null;
		joinButton.reset();
		backButton.reset();
		hostButton.reset();
		playerHost = playerClient = null;
		ball = null;
		server = null;
		score = null;
		waitingText = null;
		errorMessage = null;
		hostPort = clientIP = clientPort = null;
		apply = null;
	}
	
//	public void resetServer(){
//		try{
//			server.closeServer();
//		}catch(Exception e){};
//	}
	
	
	private void setupHost() {
		if (serverWait == null){
			serverWait = new Thread(new Runnable(){
				public void run() {
					server = new ServerStuff(updateSleep, playerHost, playerClient, ball, score, isHost, ThisGame);
					int portNumber = Integer.parseInt(hostPort.getText());
										
					if (portNumber <= 65535 && server.checkIfPortOpen(portNumber)){
						try{
							waitingText = new WaitingAnimated(80,200, "Waiting for a Client", .5, theme);
							apply = null;
							cancel = new SimpleButton("Cancel", 500, 400, 100, 40, 20, 20, theme);
							cancel.setHovered(true);
							server.setupHost(portNumber);
							mode = Modes.ONLINE;
							errorMessage = null;
						}catch(UserCanceledException e){
							waitingText = null;
							say("User Canceled Wait");
						}
					}
					else{
						if (portNumber >= 65535)
							errorMessage = "Port must be less than 65536";
						else
							errorMessage = "Sorry, Port: " + portNumber + " is Unavailable";
						
						errorMessageX = 420;
						errorMessageY = 390;
					}
					serverWait = null;
				}
			});
			serverWait.start();
		}
	}
	
	private void hostGameUpdate(){
		playerHost.move();
		ball.move();
		score.checkIfScored();
	}
	
	private void clientGameUpdate() {
		playerClient.move();
	}

	private void localUpdate() {
		playerHost.move();
		playerClient.move();
		ball.move();
		score.checkIfScored();
	}

	private void menuUpdate() {
		
		for (int i =0; i < menuBall.size();i++)
			menuBall.get(i).move();
		
		if (localButton.getPressed()){
			mode = Modes.LOCAL;
			PlayerSetup();
			localButton.reset();
		}
		else if (onlineButton.getPressed()){
			mode = Modes.ONLINE_MENU;
			PlayerSetup();
			onlineButton.reset();
		}
		else if(leftArrow.getPressed()){
			themeSelector.previousTheme();
			theme.setTheme(themeSelector.getCurrentTheme());
			leftArrow.reset();
		}
		else if(rightArrow.getPressed()){
			themeSelector.nextTheme();
			theme.setTheme(themeSelector.getCurrentTheme());
			rightArrow.reset();
		}
		else if (addButton.getPressed()){
			menuBall.add(new MenuBall(20, width, height, theme, localButton, onlineButton, addButton, removeButton, themeButton));
			addButton.resetPressed();
		}
		else if (removeButton.getPressed()){
			if (menuBall.size() >= 1)
				menuBall.remove(menuBall.size()-1);
			removeButton.resetPressed();
		}
	}
	
	private void PlayerSetup() {
		final int playerHeight = 120, playerWidth = 20;
		playerHost = new Player("left", 30,100,playerWidth,playerHeight, 20, width, height, theme);
		playerClient = new Player("right", 750,100,playerWidth,playerHeight, 20, width, height, theme);
		ball = new Ball(200,200,8,3,20, playerHost, playerClient, width, height, theme);
		score = new Score(ball, width,theme);
	}


	private void hostOrJoinUpdate() {
		if (hostButton.getPressed()){
			if (hostPort == null){
				joinButton.reset();
				isHost = true;
				say("you are the host");
				clientIP = null;
				clientPort = null;
				hostButton.forceHilight(true);
				joinButton.forceHilight(false);
				hostPort = new InputBox(400, 300, 300, 50, "Input Port # To Use", theme, inputBoxFontMetrics);
				//hostPort.setMaxChars(5);
				hostPort.setNumsOnly(true);
				apply = new SimpleButton("Go!", 500, 400, 100, 40, 20, 20, theme);
				errorMessage = null;
			}
			else if (canceled){
				apply = new SimpleButton("Go!", 500, 400, 100, 40, 20, 20, theme);
				apply.setHovered(true);
				canceled = false;
			}
		}
		if (joinButton.getPressed()){
			if (clientIP == null){
				hostButton.reset();
				isHost = false;
				say("you are the client");
				hostPort = null;
				hostButton.forceHilight(false);
				joinButton.forceHilight(true);
				clientIP = new InputBox(400, 300, 300, 50, "Input Host's IP", theme, inputBoxFontMetrics);
				clientPort = new InputBox(400, 400, 300, 50, "Input Host's Port #", theme, inputBoxFontMetrics);
				clientPort.setMaxChars(5);
				clientPort.setNumsOnly(true);
				apply = new SimpleButton("Go!", 500, 500, 100, 40, 20, 20, theme);
				errorMessage = null;
			}
			else if (canceled){
				apply = new SimpleButton("Go!", 500, 500, 100, 40, 20, 20, theme);
				apply.setHovered(true);
				canceled = false;
			}
		}
		if (backButton.getPressed()){
			try{
			if (server != null) server.cancelWait();
			}catch(Exception e){};
			hostButton.forceHilight(false);
			joinButton.forceHilight(false);
			hostButton.reset();
			joinButton.reset();
			hostPort = null;
			clientIP = null;
			clientPort = null;
			apply = null;
			cancel = null;
			errorMessage = null;
			mode = Modes.MAIN_MENU;
			backButton.reset();
		}
		
		if (apply != null && apply.getPressed()){
			if (isHost)
				setupHost();
			else
				setupClient();
			
			apply.reset();
		}
		
		else if (cancel != null && cancel.getPressed()){
			server.cancelWait();
			say("canceled");
			canceled = true;
			cancel = null;
			waitingText = null;
		}
		
	}
	
	public void paint(Graphics g)
	{
        dbi = createImage(getWidth(), getHeight());
        dbg = dbi.getGraphics();
        try{
        paintComponet(dbg);
        }catch (NullPointerException e1){};
        g.drawImage(dbi, 0, 0, this);
    }
	
	private FontMetrics inputBoxFontMetrics;
	
	public void paintComponet(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		setBackground(theme.getBackgroundColor());
		
		if (theme.getThemeName().equals("seizure")){
			g.setColor(theme.getBackgroundColor());
			g.fillRect(0, 0, 800, 600);
		}

		if (mode == Modes.MAIN_MENU) {
			//if (!theme.getThemeName().equals("seizure"))
			if (theme.getMenuBackgroundImage() != null) g.drawImage(theme.getMenuBackgroundImage(), 0, 0, null);
			for (int i =0; i < menuBall.size();i++)
				menuBall.get(i).draw(g);
			g.setColor(theme.getMenuFontColor());
			g.setFont(new Font(theme.getFont().getFontName(), Font.ITALIC, theme.getFont().getSize() + 60));
			g.drawString("Online Pong", 120, 170);
			g.setFont(new Font(theme.getFont().getFontName(), theme.getFont().getStyle(), theme.getFont().getSize() + 10));
			g.drawString("Created By: Cameron O'Neil", 220, 580);
			localButton.draw(g);
			onlineButton.draw(g);
			themeButton.draw(g);
			leftArrow.draw(g);
			rightArrow.draw(g);
			addButton.draw(g);
			removeButton.draw(g);
			g.setFont(new Font("Lucida Console", Font.PLAIN, 14));
		} else if (mode == Modes.ONLINE_MENU) {
			hostButton.draw(g);
			joinButton.draw(g);
			backButton.draw(g);
			inputBoxFontMetrics = g.getFontMetrics();
			if (hostPort != null){
				hostPort.draw(g);
				g.setFont(new Font("Lucida Console", Font.PLAIN, 12));
				g.drawString("Warning: If not on the same network, You must Port-Forward",350, 370);
			}
			if (clientIP != null) clientIP.draw(g);
			if (clientPort != null) clientPort.draw(g);
			if (apply != null) apply.draw(g);
			if (cancel != null) cancel.draw(g);
			g.setFont(new Font("Lucida Console", Font.PLAIN, 14));
			
			if (hostButton.getPressed()){
				g.setFont(new Font(theme.getFont().getFontName(), theme.getFont().getStyle(), theme.getFont().getSize() + 10));
				g.setColor(theme.getFontColor());
				g.drawString("Host Settings", 430, 120);
			}
			else if (joinButton.getPressed()){
				g.setFont(new Font(theme.getFont().getFontName(), theme.getFont().getStyle(), theme.getFont().getSize() + 10));
				g.setColor(theme.getFontColor());
				g.drawString("Client Settings", 430, 120);
			}
			
			if (waitingText != null) waitingText.draw(g);
		}
		else if (mode == Modes.ONLINE || mode == Modes.LOCAL) {
//			if (isHost)
//				g.drawString("You are Host", 200, 50);
//			else
//				g.drawString("You are Client", 200, 50);
			
			g.drawString("ESC to Quit", 730, 40);

			score.draw(g);
			playerHost.draw(g);
			playerClient.draw(g);
			ball.draw(g);
		}
		
		g.setColor(theme.getMenuFontColor());
		g.setFont(new Font("Lucida Console", Font.PLAIN, 14));
		if (f1){
			if (!(uaf.getUPS() > updateTarget - 2 && uaf.getUPS() < updateTarget + 2))
				g.setColor(Color.red);
			
			g.drawString("FPS: " + uaf.getFPS(), 30, 50);
			g.drawString("UPS: " + uaf.getUPS(), 30, 70);
			g.drawString("Time Elapsed: " + uaf.getTimeElapsed(), 30, 90);
			g.drawString("Mouse at: " + mouseXY, 610, 50);
		}
		else{
			g.setColor(theme.getMenuFontColor());
			g.drawString("[Press F1 for Game Details]", 10, 45);
		}
		
		if (errorMessage != null){
			g.setColor(Color.red);
			g.drawString(errorMessage, errorMessageX, errorMessageY);
		}

		// transparency
		// g.setColor(new Color(1f,0f,0f,.5f ));
		// g.fillRect(0, 0, 300, 300);
	}
	
	public void mouseMoved(MouseEvent e) {
		
		mouseXY = e.getPoint().toString().substring(14);
		
		if (mode == Modes.MAIN_MENU) {
			try{
				localButton.checkIfHovering(e);
				onlineButton.checkIfHovering(e);
				leftArrow.checkIfHovering(e);
				rightArrow.checkIfHovering(e);
				addButton.checkIfHovering(e);
				removeButton.checkIfHovering(e);
			}catch (NullPointerException e1){};
		}
		else if (mode == Modes.ONLINE_MENU){
			hostButton.checkIfHovering(e);
			joinButton.checkIfHovering(e);
			backButton.checkIfHovering(e);
			if (apply != null) apply.checkIfHovering(e);
			if (cancel != null) cancel.checkIfHovering(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (mode == Modes.MAIN_MENU) {
			localButton.checkIfPressed(e);
			onlineButton.checkIfPressed(e);
			leftArrow.checkIfPressed(e);
			rightArrow.checkIfPressed(e);
			addButton.checkIfPressed(e);
			removeButton.checkIfPressed(e);
		}
		else if (mode == Modes.ONLINE_MENU){
			hostButton.checkIfPressed(e);
			joinButton.checkIfPressed(e);
			backButton.checkIfPressed(e);
			if (hostPort != null) hostPort.checkIfPressed(e);
			if (clientIP != null) clientIP.checkIfPressed(e);
			if (clientPort != null) clientPort.checkIfPressed(e);
			if (apply != null) apply.checkIfPressed(e);
			if (cancel != null) cancel.checkIfPressed(e);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent k) {
		
		if (hostPort != null) hostPort.keyTyped(k);
		if (clientIP != null) clientIP.keyTyped(k);
		if (clientPort != null) clientPort.keyTyped(k);
		
		int keyTyped = k.getKeyCode();
		
		if (keyTyped == KeyEvent.VK_W){
			playerHost.upHeld(true);
		}
		else if (keyTyped == KeyEvent.VK_S){
			playerHost.downHeld(true);
		}
		else if (keyTyped == KeyEvent.VK_UP){
			playerClient.upHeld(true);
		}
		else if (keyTyped == KeyEvent.VK_DOWN){
			playerClient.downHeld(true);
		}
		else if (keyTyped == KeyEvent.VK_F1)
			f1 = !f1;
		else if (keyTyped == KeyEvent.VK_ADD){
			for (int i =0; i < 100;i++)
				menuBall.add(new MenuBall(20, width, height, theme, localButton, onlineButton, addButton, removeButton, themeButton));
		}
		else if (keyTyped == KeyEvent.VK_SUBTRACT){
			for (int i =0; i < 100;i++)
				if (menuBall.size() >= 1)
					menuBall.remove(menuBall.size()-1);
		}
		else if(keyTyped == KeyEvent.VK_ESCAPE){
			reset();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent k) {
		int keyTyped = k.getKeyCode();
		
		if (keyTyped == KeyEvent.VK_W){
			playerHost.upHeld(false);
		}
		else if (keyTyped == KeyEvent.VK_S){
			playerHost.downHeld(false);
		}
		else if (keyTyped == KeyEvent.VK_UP){
			playerClient.upHeld(false);
		}
		else if (keyTyped == KeyEvent.VK_DOWN){
			playerClient.downHeld(false);
		}
	}
	
	private void sleep(int i){
		try {
			Thread.sleep(i);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}

	public static void main(String[] args) {
		MultiplayerPong game = new MultiplayerPong();
		game.run();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void mouseDragged(MouseEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent k) {}
}

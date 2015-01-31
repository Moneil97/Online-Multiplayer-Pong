package Pong;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import Themes.Themes;

public class Player {

	@SuppressWarnings("unused")
	private int x, y, width, height, speed, gameWidth, gameHeight;
	private boolean goUp, goDown;
	private Themes theme;
	private String id;

	public Player(String id, int x, int y, int width, int height, int speed, int gameWidth, int gameHeight, Themes theme){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.gameHeight = gameHeight;
		this.gameWidth = gameWidth;
		this.theme = theme;
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void upHeld(boolean b) {
		goUp = b;
	}
	
	public void downHeld(boolean b) {
		goDown = b;
	}
	
	public void move(){
		if (goUp && y>30){
			y -= speed;
		}
		else if (goDown && y+height<gameHeight){
			y+=speed;
		}
	}
	
	public Rectangle2D getUpperFifth(){
		return new Rectangle2D.Double(x,y, width, height/5); //Upper
	}
	public Rectangle2D getUpperMiddleFifth(){
		return new Rectangle2D.Double(x,y+height/5, width, height/5); //UpperMid
	}
	public Rectangle2D getMiddleFifth(){
		return new Rectangle2D.Double(x,y+height/5*2, width, height/5); //Middle
	}
	public Rectangle2D getLowerMiddleFifth(){
		return new Rectangle2D.Double(x,y+height/5*3, width, height/5); //Lower Mid
	}
	public Rectangle2D getLowerFifth(){
		return new Rectangle2D.Double(x,y+height/5*4, width, height/5); //Lower
	}
	
	public Rectangle2D getBounds(){
		return new Rectangle2D.Double(x,y, width, height);
	}
	
	public void draw(Graphics2D g)
	{
		//Player one
		if (id.equals("left")){
			if (theme.getPlayerOneImage() != null){
				g.drawImage(theme.getPlayerOneImage(), x, y, width, height, null);
			}
			else{
				g.setColor(theme.getPlayerOneColor());
				g.fillRect(x, y, width, height);
			}
		}
		//Player two
		else if (id.equals("right")){
			if (theme.getPlayerTwoImage() != null){
				g.drawImage(theme.getPlayerTwoImage(), x, y, width, height, null);
			}
			else{
				g.setColor(theme.getPlayerTwoColor());
				g.fillRect(x, y, width, height);
			}
		}
	}
	
	public void say(Object s) {
		System.out.println(this.getClass().getName()+ ": " + s);
	}

}

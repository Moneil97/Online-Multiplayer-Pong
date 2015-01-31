package Pong;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import Themes.Themes;

public class Ball {

	private int x, y, size, gameHeight;//, gameWidth;
	private double xSpeed, ySpeed;
	
	private Player left, right;
	private Themes theme;

	public Ball(int x, int y, int xSpeed, int ySpeed, int size, Player host,
			Player client, int gameWidth, int gameHeight, Themes theme) {
		this.x = x;
		this.y = y;
		this.xSpeed = xSpeed;		
		this.ySpeed = ySpeed;
		this.size = size;
		this.left = host;
		this.right = client;
		//this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		this.theme = theme;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void move() {
		x += xSpeed;
		y += ySpeed;

		checkCollisionAdvanced();
	}

	private void checkCollisionAdvanced() {

		// Y
		if (y < 30)
			ySpeed = Math.abs(ySpeed);
		else if (y + size > gameHeight - 10)
			ySpeed = -Math.abs(ySpeed);

		
		if (getBounds().intersects(left.getBounds()))
			xSpeed = Math.abs(xSpeed);
		else if (getBounds().intersects(right.getBounds()))
			xSpeed = -Math.abs(xSpeed);
		else 
			return; //If not touching anything will leave method now
		
		
		double num = .2;
		
		if (getBounds().intersects(left.getLowerFifth()) || getBounds().intersects(right.getLowerFifth()))
			ySpeed+= (ySpeed>0 ? num*2 : -num*2);
		else if (getBounds().intersects(left.getLowerMiddleFifth()) || getBounds().intersects(right.getLowerMiddleFifth())){
			ySpeed+= (ySpeed>0 ? num : -num);
			xSpeed+= (xSpeed>0 ? num : -num);
		}
		else if (getBounds().intersects(left.getMiddleFifth()) || getBounds().intersects(right.getMiddleFifth()))
			xSpeed+= (xSpeed>0 ? num*2 : -num*2);
		else if (getBounds().intersects(left.getUpperMiddleFifth()) || getBounds().intersects(right.getUpperMiddleFifth())){
			ySpeed+= (ySpeed>0 ? num : -num);
			xSpeed+= (xSpeed>0 ? num : -num);
		}
		else if (getBounds().intersects(left.getUpperFifth()) || getBounds().intersects(right.getUpperFifth()))
			ySpeed+= (ySpeed>0 ? num*2 : -num*2);
		
		//say("xSpeed: " + xSpeed + "     ySpeed: " + ySpeed);
	}

	public Ellipse2D getBounds() {
		return new Ellipse2D.Float(x, y, size, size);
	}

	public int getSize() {
		return size;
	}

	public void draw(Graphics2D g) {
		
		if (theme.getBallImage() != null){
			g.drawImage(theme.getBallImage(), x, y, size, size, null);
		}
		else{
			g.setColor(theme.getBallColor());
			g.fillOval(x, y, size, size);
		}
	}

	public void say(Object s) {
		System.out.println(this.getClass().getName()+ ": " + s);
	}

	public void reset() {
		x = rand(300, 500);
		y = rand(100, 500);
		xSpeed = either(rand(4, 8), rand(-8, -4));// 8;
		ySpeed = either(rand(4, 8), rand(-8, -4));// 3;
	}

	public int either(int one, int two) {
		if (Math.random() > .5)
			return one;
		else
			return two;
	}

	public static int rand(int min, int max) {
		// [min, max]
		return min + (int) (Math.random() * ((max - min) + 1));
	}
}

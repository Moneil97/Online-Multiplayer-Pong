package Pong;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import Buttons.SimpleButton;
import Buttons.ThemeButton;
import Themes.Themes;

public class MenuBall {

	private int x, y, xSpeed, ySpeed, size, gameHeight, gameWidth;
	private Themes theme;
	private SimpleButton[] butts;
	private ThemeButton themeButton;

	public MenuBall(int size, int gameWidth, int gameHeight, Themes theme, SimpleButton localButton, SimpleButton onlineButton, SimpleButton addButton, SimpleButton removeButton, ThemeButton themeButton) {
		this.size = size;
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		this.theme = theme;
		this.themeButton = themeButton;
		butts = new SimpleButton[] {localButton, onlineButton, addButton, removeButton};
		spawn();
	}
	
	public void spawn(){
		
		do{
			x = rand(40, gameWidth-40);
			y = rand(40, gameHeight-40);
		}while(buttonIntersection());
		xSpeed = either(rand(4, 8), rand(-8, -4));// 8;
		ySpeed = either(rand(4, 8), rand(-8, -4));// 3;
	}
	
	private boolean buttonIntersection(){
		
		for (SimpleButton butt : butts){
			if (getBounds().intersects(butt.getBounds()))
				return true;
		}
		
		if (getBounds().intersects(themeButton.getBounds())) return true;
		
		return false;
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
		x = x + xSpeed;
		y = y + ySpeed;

		checkCollision();
	}


	public Ellipse2D getBounds() {
		return new Ellipse2D.Float(x, y, size, size);
	}
	
	

	private void checkCollision() {
		// X
		if (x>gameWidth-size)
			xSpeed  *= -1;
		else if (x<5)
			xSpeed  *= -1;
		// Y
		if (y < 30)
			ySpeed *= -1;
		else if (y + size > gameHeight - 5)
			ySpeed *= -1;
		
		//Collision
		for (SimpleButton butt : butts){
			if (getBounds().intersects(butt.getUpperBounds()))
				ySpeed = -Math.abs(ySpeed);
			if (getBounds().intersects(butt.getLowerBounds()))
				ySpeed = Math.abs(ySpeed);
			if (getBounds().intersects(butt.getRightBounds()))
				xSpeed = Math.abs(xSpeed);
			if (getBounds().intersects(butt.getLeftBounds()))
				xSpeed = -Math.abs(xSpeed);
		}
		
		
//		if (getBounds().intersects(localButton.getUpperBounds()) || getBounds().intersects(onlineButton.getUpperBounds()))
//			ySpeed = -Math.abs(ySpeed);
//		if (getBounds().intersects(localButton.getLowerBounds()) || getBounds().intersects(onlineButton.getLowerBounds()))
//			ySpeed = Math.abs(ySpeed);
//		if (getBounds().intersects(localButton.getRightBounds()) || getBounds().intersects(onlineButton.getRightBounds()))
//			xSpeed = Math.abs(xSpeed);
//		if (getBounds().intersects(localButton.getLeftBounds()) || getBounds().intersects(onlineButton.getLeftBounds()))
//			xSpeed = -Math.abs(xSpeed);
		
//		if ()
//			ySpeed *= -1;
//		if ()
//			ySpeed *= -1;
//		if ()
//			xSpeed *= -1;
//		if ()
//			xSpeed *= -1;
		
		//Theme Button
		if (getBounds().intersects(themeButton.getUpperBounds()))
			ySpeed *= -1;
		if (getBounds().intersects(themeButton.getLowerBounds()))
			ySpeed *= -1;
		if (getBounds().intersects(themeButton.getRightBounds()))
			xSpeed *= -1;
		if (getBounds().intersects(themeButton.getLeftBounds()))
			xSpeed *= -1;
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

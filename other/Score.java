package other;
import java.awt.Font;
import java.awt.Graphics2D;

import Pong.Ball;
import Themes.Themes;


public class Score {

	private int leftScore = 0, rightScore = 0;
	private Ball ball;
	private int gameWidth;
	private Themes theme;
	
	public Score (Ball ball, int gameWidth, Themes theme){
		this.ball = ball;
		this.gameWidth = gameWidth;
		this.theme = theme;
	}

	public int getLeftScore() {
		return leftScore;
	}

	public void incLeftScore() {
		leftScore++;
	}

	public int getRightScore() {
		return rightScore;
	}

	public void incRightScore() {
		rightScore++;
	}
	
	public void setLeftScore(int ls){
		leftScore = ls;
	}
	
	public void setRightScore(int rs){
		rightScore = rs;
	}
	
	public void draw(Graphics2D g){
		g.setFont(new Font("Lucida Console", Font.BOLD, 40));
		g.setColor(theme.getScoreFontColor());
		//g.drawLine(gameWidth/2, 0, gameWidth/2, 600);
		if (leftScore < 10)
			g.drawString(leftScore + " - " + rightScore, 335, 70);
		else
			g.drawString(leftScore + " - " + rightScore, 308, 70);
		
		//g.drawString(rightScore + "", 420, 70);
	}

	public void checkIfScored() {
		if (ball.getX()<0)
			incRightScore();
		else if (ball.getX() > gameWidth)
			incLeftScore();
		else return;
		
		ball.reset();
	}
}

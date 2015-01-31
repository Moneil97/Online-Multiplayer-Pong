package other;

import java.awt.Font;
import java.awt.Graphics2D;

import Themes.Themes;

public class WaitingAnimated {
	
	private String message;
	private long start;
	private int x,y;
	private double nanos;
	private Themes theme;
	private byte dots = 0;
	
	public WaitingAnimated(int x, int y, String message, double seconds, Themes theme){
		start = System.nanoTime();
		this.message = message;
		this.x = x;
		this.y = y;
		this.nanos = seconds * 1000000000;
		this.theme = theme;
	}
	
	public void setMessage(String s){
		message = s;
	}
	
	public void draw(Graphics2D g){
		g.setFont(new Font(theme.getFont().getFontName(), Font.PLAIN, theme.getFont().getSize() + 10));
		g.setColor(theme.getFontColor());
		g.drawString(message + getDots(), x, y);
	}
	
	private String getDots(){
		
		long now = System.nanoTime();
		
		if (now - start > nanos){
			start = now;
			dots++;
			if (dots > 3) dots = 0;
		}
		
		String dotString = "";
		
		for (int i = 0; i < dots; i++){
			dotString += ".";
		}
		
		return dotString;
	}
	
	public void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}
}

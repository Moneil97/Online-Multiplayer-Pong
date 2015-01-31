package Buttons;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import Themes.Themes;

public class ThemeButton {

	private int x, y, width, height;
	private String text;
	private int arcWidth = 20, arcHeight = 20;
	private Themes theme;
	
	public ThemeButton(String text, Font font, int x, int y, int width, int height, Color color, int arcWidth, int arcHeight, Themes theme) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		this.theme = theme;
	}

	public void draw(Graphics2D g2) {
		g2.setFont(theme.getFont());
		
//		g2.setColor(Color.RED);
//		g2.fill(getUpperBounds());
//		g2.fill(getLowerBounds());
//		g2.fill(getRightBounds());
//		g2.fill(getLeftBounds());
		
		g2.setColor(theme.getButtonColor());
		g2.fillRoundRect(x, y, width, height-50, arcWidth, arcHeight);
		
		if (theme.getButtonOutlineColor() != null){
			g2.setColor(theme.getButtonOutlineColor());
			g2.drawRoundRect(x, y, width, height-50, arcWidth, arcHeight);
		}
		
		g2.setColor(theme.getFontColor());
		g2.drawString(text, (x + width / 2) - g2.getFontMetrics().stringWidth(text) / 2, y + g2.getFontMetrics().getHeight()+10);
		g2.drawString(theme.getThemeName(), (x + width / 2) - g2.getFontMetrics().stringWidth(theme.getThemeName()) / 2, y + g2.getFontMetrics().getHeight()*3);
		//g2.drawImage(theme.getPreviewImage(), x + width/4-10, y+height/4+20-15, width/2+20, height/2+20, null);
		if (theme.getThemeCreator().equals("Sam Ebe"))
			g2.drawString("-" + theme.getThemeCreator(), (x + width / 2) - g2.getFontMetrics().stringWidth("-" + theme.getThemeCreator()) / 2, y + height-97 /*+ g2.getFontMetrics().getHeight()*/);
		else{
			g2.drawString("-" + theme.getThemeCreatorFirst(), (x + width / 2) - g2.getFontMetrics().stringWidth("-" + theme.getThemeCreatorFirst()) / 2, y + height-97);
			//g2.drawString(theme.getThemeCreatorLast(), (x + width / 2) - g2.getFontMetrics().stringWidth(theme.getThemeCreatorLast()) / 2, y + height-87);
		}
	}	
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}
	
	public Rectangle2D getUpperBounds(){
		return new Rectangle2D.Double(x+5, y+1, width-5-5, 1);
	}
	
	public Rectangle2D getLowerBounds(){
		return new Rectangle2D.Double(x+5, y+height-50-1, width-5-5, 1);
	}
	
	public Rectangle2D getRightBounds(){
		return new Rectangle2D.Double(x+width-1, y+5, 1, height-5-5-50);
	}
	
	public Rectangle2D getLeftBounds(){
		return new Rectangle2D.Double(x-1, y+5, 1, height-5-5-50);
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x, y, width, height);
	}
}

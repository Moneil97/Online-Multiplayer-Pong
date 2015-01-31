package Buttons;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import Themes.Themes;

public class SimpleButton {

	private int x, y, width, height;
	private String text;

	private boolean hovering, pressed;

	private Rectangle rekt;
	private int arcWidth = 0, arcHeight = 0;
	private Themes theme;

	private boolean forceHilight = false;

	public SimpleButton(String text, int x, int y, int width,
			int height, int arcWidth, int arcHeight, Themes theme) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		this.theme = theme;

		rekt = new Rectangle(x, y, width, height);
	}

	public void draw(Graphics2D g2) {
		g2.setFont(theme.getFont());
		
//		g2.setColor(Color.red);
//		g2.fill(getUpperBounds());
//		g2.fill(getLowerBounds());
//		g2.fill(getRightBounds());
//		g2.fill(getLeftBounds());

		if (hovering || forceHilight) {
			if (theme.getButtonHoverImage() != null) {
				g2.drawImage(theme.getButtonHoverImage(), x, y, width, height,
						null);
			} else {
				g2.setColor(theme.getButtonHoverColor());
				g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
			}
		} else {
			if (theme.getButtonImage() != null) {
				g2.drawImage(theme.getButtonImage(), x, y, width, height, null);
			} else {
				g2.setColor(theme.getButtonColor());
				g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
			}
		}
		if (theme.getButtonOutlineColor() != null){
			g2.setColor(theme.getButtonOutlineColor());
			g2.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		}
		
		if (hovering || forceHilight)
			g2.setColor(theme.getFontHoverColor());
		else
			g2.setColor(theme.getFontColor());

		g2.drawString(text,
				(x + width / 2) - g2.getFontMetrics().stringWidth(text) / 2,
				(y + height / 2) + g2.getFontMetrics().getHeight() / 4);
	}

	public void checkIfHovering(MouseEvent e) {
		if (rekt.contains(e.getX(), e.getY())) {
			if (!hovering) {
				hovering = true;
			}
		} else {
			hovering = false;
		}
	}

	public void checkIfPressed(MouseEvent e) {
		if (hovering){
			pressed = true;
			say("pressed");
		}
	}

	public boolean getPressed() {
		return pressed;
	}

	public void setX(int x) {
		this.x = x;
		rekt = new Rectangle(x, y, width, height);
	}

	public void setY(int y) {
		this.y = y;
		rekt = new Rectangle(x, y, width, height);
	}

	public void reset() {
		pressed = false;
		hovering = false;
		forceHilight = false;
	}
	
	public void resetPressed() {
		pressed = false;
	}

	public void setPressed(boolean b) {
		pressed = b;
	}

	public void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Rectangle2D getUpperBounds(){
		return new Rectangle2D.Double(x+5, y+1, width-5-5, 1);
	}
	
	public Rectangle2D getLowerBounds(){
		return new Rectangle2D.Double(x+5, y+height-1, width-5-5, 1);
	}
	
	public Rectangle2D getRightBounds(){
		return new Rectangle2D.Double(x+width-1, y+5, 1, height-5-5);
	}
	
	public Rectangle2D getLeftBounds(){
		return new Rectangle2D.Double(x-1, y+5, 1, height-5-5);
	}

	public Rectangle2D getBounds() {
		
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	public void forceHilight(boolean b) {
		forceHilight = b;
	}

	public void setHovered(boolean b) {
		hovering = b;
		
	}
}

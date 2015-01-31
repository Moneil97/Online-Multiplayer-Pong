package Buttons;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import other.Triangle;
import Themes.Themes;

public class TriangleButton {

	private boolean hovering, pressed;
	private Themes theme;
	private Triangle triangle;

	public TriangleButton(String direction, int x, int y, int width,
			int height, Themes theme) {

		this.theme = theme;
		triangle = new Triangle(direction, x, y, width, height);
	}

	public void draw(Graphics2D g2) {
		g2.setFont(theme.getFont());

		if (hovering) {
			g2.setColor(theme.getTrianlgeButtonHoverColor());
			g2.fillPolygon(triangle.getBounds());
			if (theme.getTrianlgeButtonHoverOutlineColor() != null) {
				g2.setColor(theme.getTrianlgeButtonHoverOutlineColor());
				g2.drawPolygon(triangle.getBounds());
			}
		} else {
			g2.setColor(theme.getTrianlgeButtonColor());
			g2.fillPolygon(triangle.getBounds());
			if (theme.getTrianlgeButtonOutlineColor() != null) {
				g2.setColor(theme.getTrianlgeButtonOutlineColor());
				g2.drawPolygon(triangle.getBounds());
			}
		}
	}

	public void checkIfHovering(MouseEvent e) {
		if (triangle.getBounds().contains(e.getX(), e.getY())) {
			if (!hovering) {
				hovering = true;
			}
		} else {
			hovering = false;
		}
	}

	public void checkIfPressed(MouseEvent e) {
		if (hovering)
			pressed = true;
	}

	public boolean getPressed() {
		return pressed;
	}

	public void setLocation(String direction, int x, int y, int width,
			int height) {
		triangle.setLocation(direction, x, y, width, height);
	}

	public void reset() {
		pressed = false;
		//hovering = false;
	}

	public void setPressed(boolean b) {
		pressed = b;
	}

	public void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}
}

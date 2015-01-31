package other;
import java.awt.Polygon;


public class Triangle {

	private int xCoords[], yCoords[];
	private Polygon bounds;
	
	public Triangle(String direction, int x, int y, int width, int height) {
		setLocation(direction, x, y, width, height);
	}
	
	public void setLocation(String direction, int x, int y, int width, int height){
		if (direction.equals("left")) {
			int xCoords[] = { x, x + width, x + width };
			int yCoords[] = { y, y - height / 2, y + height / 2 };
			this.xCoords = xCoords;
			this.yCoords = yCoords;
		}else if (direction.equals("right")){
			int xCoords[] = { x, x - width, x - width };
			int yCoords[] = { y, y - height / 2, y + height / 2 };
			this.xCoords = xCoords;
			this.yCoords = yCoords;
		}else if (direction.equals("down")){
			int xCoords[] = { x, x - width/2, x + width/2 };
			int yCoords[] = { y, y + height, y + height};
			this.xCoords = xCoords;
			this.yCoords = yCoords;
		}else{
			int xCoords[] = { x, x - width/2, x + width/2 };
			int yCoords[] = { y, y - height, y - height};
			this.xCoords = xCoords;
			this.yCoords = yCoords;
		}
		bounds = new Polygon(xCoords, yCoords, xCoords.length);
	}
	
	public Polygon getBounds(){
		return bounds;
	}

}

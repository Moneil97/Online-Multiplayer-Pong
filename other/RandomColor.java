package other;
import java.awt.Color;


public class RandomColor {

	public Color getRandomColor(){
		return new Color(rand(0,255), rand(0,255), rand(0,255));
	}
	
	private int rand(int min, int max) {
		// [min, max]
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
}

package Themes;

public class ThemeSelection {

	String[] themes = {"Classic", "Seizure", "Retro"};
	int x = 0;
	
	//No Constructor used
	
	public String getCurrentTheme(){
		return themes[x];
	}
	
	public void nextTheme(){
		x++;
		
		if (x > themes.length-1)
			x = 0;
		
		say(getCurrentTheme());
	}
	
	public void previousTheme(){
		x--;
		
		if (x < 0)
			x = themes.length-1;
		
		say(getCurrentTheme());
	}
	
	public void say(Object s) {
		System.out.println(this.getClass().getName()+ ": " + s);
	}
}
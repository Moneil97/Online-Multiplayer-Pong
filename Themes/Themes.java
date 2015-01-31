package Themes;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import other.RandomColor;

public class Themes {

	private String classic = "Classic", seizure = "Seizure", retro = "Retro";
	private String cameron = "Cameron O'Neil", sam = "Sam Ebe";
	private String themeName, themeCreator;
	private Color buttonColor, buttonHoverColor, backgroundColor,
			buttonTextColor, playerOneColor, playerTwoColor, ballColor,
			fontColor, hoverFontColor, buttonOutlineColor,
			/*playerOneOutlineColor, playerTwoOutlineColor, ballOutlineColor,*/
			menuFontColor, trianlgeButtonColor, trianlgeButtonOutlineColor,
			trianlgeButtonHoverColor, trianlgeButtonHoverOutlineColor, scoreFontColor,
			inputBoxBackgroundColor, inputBoxBorderColor, inputBoxSelectedBorderColor;
	private BufferedImage menuBackgroundImage, gameBackgroundImage,
			buttonImage, buttonHoverImage, playerOneImage, playerTwoImage,
			ballImage, previewImage;
	private Font font;
	private RandomColor r = new RandomColor();
	private boolean seizureMode = false;

	public Themes(String theme) {
		setTheme(theme);
	}

	public void setTheme(String theme) {
		if (theme.equals(classic)) {
			themeName = classic;
			themeCreator = sam;
			seizureMode = false;
			font = new Font("Lucida Console", Font.BOLD, 24);
			fontColor = hoverFontColor = Color.black;
			menuFontColor = Color.black;

			// Colors
			scoreFontColor = buttonColor = new Color(219, 141, 81);
			trianlgeButtonHoverColor = trianlgeButtonColor = buttonHoverColor = new Color(
					122, 20, 0);
			backgroundColor = new Color(196, 196, 196);
			buttonTextColor = Color.black;
			playerOneColor = Color.cyan;
			playerTwoColor = Color.green;
			ballColor = Color.magenta;
			
			//InputBoxColors
			inputBoxBackgroundColor = Color.white;
			inputBoxBorderColor = Color.black;
			inputBoxSelectedBorderColor = Color.blue;

			// Outline colors
			buttonOutlineColor = /*playerOneOutlineColor = playerTwoOutlineColor = ballOutlineColor = */trianlgeButtonOutlineColor = trianlgeButtonHoverOutlineColor = null;

			// Images
			gameBackgroundImage = buttonImage = buttonHoverImage = playerOneImage = playerTwoImage = ballImage = null;

			try {
				previewImage = ImageIO.read(this.getClass()
						.getResourceAsStream("/Media/ClassicThemePreview.PNG"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (theme.equals(seizure)) {
			themeName = seizure;
			themeCreator = cameron;
			seizureMode = true;
		} else if (theme.equals(retro)) {
			themeName = retro;
			themeCreator = cameron;
			seizureMode = false;
			font = new Font("Lucida Console", Font.BOLD, 24);
			fontColor = Color.green;
			hoverFontColor = Color.black;
			menuFontColor = Color.green;

			// Colors
			buttonColor = Color.black;
			buttonHoverColor = Color.green;
			backgroundColor = Color.black;
			scoreFontColor = buttonTextColor = Color.green;
			playerOneColor = playerTwoColor = ballColor = Color.green;

			trianlgeButtonColor = Color.black;
			trianlgeButtonOutlineColor = trianlgeButtonHoverColor = trianlgeButtonHoverOutlineColor = Color.green;

			//InputBoxColors
			inputBoxBackgroundColor = Color.black;
			inputBoxBorderColor = Color.green;
			inputBoxSelectedBorderColor = Color.blue;
			
			// Outline colors
			buttonOutlineColor = /*playerOneOutlineColor = playerTwoOutlineColor = ballOutlineColor = */Color.green;

			// Images
			gameBackgroundImage = buttonImage = buttonHoverImage = playerOneImage = playerTwoImage = ballImage = null;

			try {
				previewImage = ImageIO.read(this.getClass()
						.getResourceAsStream("/Media/RetroThemePreview.PNG"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getThemeName() {
		return themeName;
	}

	public String getThemeCreator() {
		return themeCreator;
	}

	public String getThemeCreatorFirst() {
		return themeCreator.substring(0, themeCreator.indexOf(" "));
	}

	public String getThemeCreatorLast() {
		return themeCreator.substring(themeCreator.indexOf(" "));
	}

	public Font getFont() {
		return font;
	}

	public Color getFontColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return fontColor;
	}

	public Color getButtonColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return buttonColor;
	}

	public Color getButtonHoverColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return buttonHoverColor;
	}

	public Color getBackgroundColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return backgroundColor;
	}

	public Color getButtonTextColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return buttonTextColor;
	}

	public Color getPlayerOneColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return playerOneColor;
	}

	public Color getPlayerTwoColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return playerTwoColor;
	}

	public Color getBallColor() {
		if (seizureMode)
			return r.getRandomColor();
		else
			return ballColor;
	}

	public BufferedImage getMenuBackgroundImage() {
		return menuBackgroundImage;
	}

	public BufferedImage getGameBackgroundImage() {
		return gameBackgroundImage;
	}

	public BufferedImage getButtonImage() {
		return buttonImage;
	}

	public BufferedImage getButtonHoverImage() {
		return buttonHoverImage;
	}

	public BufferedImage getPlayerOneImage() {
		return playerOneImage;
	}

	public BufferedImage getPlayerTwoImage() {
		return playerTwoImage;
	}

	public BufferedImage getBallImage() {
		return ballImage;
	}

	public Color getFontHoverColor() {
		if (seizureMode)
			return r.getRandomColor();
		return hoverFontColor;
	}

	public Color getMenuFontColor() {
		if (seizureMode)
			return r.getRandomColor();
		return menuFontColor;
	}

	public Color getButtonOutlineColor() {
		if (seizureMode)
			return r.getRandomColor();
		return buttonOutlineColor;
	}

	public Color getTrianlgeButtonColor() {
		if (seizureMode)
			return r.getRandomColor();
		return trianlgeButtonColor;
	}

	public Color getTrianlgeButtonHoverColor() {
		if (seizureMode)
			return r.getRandomColor();
		return trianlgeButtonHoverColor;
	}

	public Color getTrianlgeButtonOutlineColor() {
		if (seizureMode)
			return r.getRandomColor();
		return trianlgeButtonOutlineColor;
	}

	public Color getTrianlgeButtonHoverOutlineColor() {
		if (seizureMode)
			return r.getRandomColor();
		return trianlgeButtonHoverOutlineColor;
	}

	public BufferedImage getPreviewImage() {
		return previewImage;
	}

	public Color getScoreFontColor() {
		if (seizureMode)
			return r.getRandomColor();
		return scoreFontColor;
	}

	public Color getInputBoxBackgroundColor() {
		return inputBoxBackgroundColor;
	}

	public Color getInputBoxBorderColor() {
		return inputBoxBorderColor;
	}

	public Color getInputBoxSelectedBorderColor() {
		return inputBoxSelectedBorderColor;
	}

}

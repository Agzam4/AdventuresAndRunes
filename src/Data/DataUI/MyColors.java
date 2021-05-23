package Data.DataUI;

import java.awt.Color;
import java.awt.image.BufferedImage;

public enum MyColors {
	BLACK,
	DARK_GREY,
	GREY,
	LIGHT_GREY,
	DARK_WHITE,
	WHITE;

	private Color c;
	private static final BufferedImage palette = UIData.loadImage("UI\\default\\palette.png");

	private static Color getPixelColor(int x, int y) {
		return new Color(palette.getRGB(x, y));
	}
	private static Color getPixelColor(int id) {
		return getPixelColor(id%palette.getWidth(), (int) (Math.floor(id)/palette.getWidth()));
	}

	MyColors() {
		c = getPixelColor(this.ordinal());
	}
	public Color getColor() {
		return c;
	}
}

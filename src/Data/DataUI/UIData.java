package Data.DataUI;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIData {
	
	private static final String path = System.getProperty("user.dir") + "\\data\\";

	protected static BufferedImage loadImage(String data) {
		System.out.println("loadImage: " + data);
		try {
			return ImageIO.read(new File(path + data));
		} catch (IOException e) {
			try {
				return ImageIO.read(UIData.class.getResourceAsStream(data));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return new BufferedImage(99, 99, BufferedImage.TYPE_INT_RGB);
	}
	
	protected static void loadData(String data) {
		
	}
}

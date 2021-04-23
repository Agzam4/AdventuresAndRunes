package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private int type;
	
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int FINISH = 20;
	public static final int SWAMP = 21;
	
//	private static ArrayList<Integer> getNORMALS() {
//		return 
//			new ArrayList<Integer>(Arrays.asList(new Integer[] {
//				0,26
//			}));
//	}
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }
	
}

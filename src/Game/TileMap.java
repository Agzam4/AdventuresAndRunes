package Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.STRING;

import Main.GameJPanel;
import TileMap.Tile;

public class TileMap {

	private double x;
	private double y;

	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	
	private double tween;
	
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	private BufferedImage tileSet;

	private Tile[][] tiles;
	private int numTiilesAcross;

	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {

		this.tileSize = tileSize;
		numRowsToDraw = GameJPanel.HEIGHT / tileSize + 2;
		numColsToDraw = GameJPanel.WIDTH / tileSize + 2;
		
		tween = 0.07;
	}

	public void loadTiles(String s) throws IOException {
		tileSet = ImageIO.read(getClass().getResourceAsStream(s));
		numTiilesAcross = tileSet.getWidth() / tileSize;
		tiles = new Tile[2][numTiilesAcross];
		BufferedImage subimage;
		for(int col = 0; col < numTiilesAcross; col++) {
			subimage = tileSet.getSubimage(col * tileSize,0,tileSize,tileSize);
			tiles[0][col] = new Tile(subimage, Tile.NORMAL);
			subimage = tileSet.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
			tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
		}
	}
	public void loadMap(String s) throws NumberFormatException, IOException {
		InputStream in = getClass().getResourceAsStream(s);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		numCols = Integer.parseInt(br.readLine());
		numRows = Integer.parseInt(br.readLine());
		map = new int[numRows][numCols];
		width = numCols * tileSize;
		height = numRows * tileSize;
		
		String delims = "\\s+";
		for (int row = 0; row < numRows; row++) {
			String line = br.readLine();
			String[] tokens = line.split(delims);
			for (int col = 0; col < numCols; col++) {
				map[row][col] = Integer.parseInt(tokens[col]);
			}
		}
	}
	
	
	public int getTileSize() {
		return tileSize;
	}
	public int getX() {
		return (int)x;
	}
	public int getY() {
		return (int)y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public int getType(int row, int col) { // TODO
		int rc = map[row][col];
		int r = rc / numTiilesAcross;
		int c = rc % numTiilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setPosition(double x, double y) {
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();

		colOffset = (int) (-this.x / tileSize);
		rowOffset = (int) (-this.y / tileSize);
		
	}

	private void fixBounds() {
		if(x < minX)
			x = minX;
		if(y < minY)
			y = minY;
		if(x > maxX)
			x = maxX;
		if(y > maxY)
			y = maxY;
	}
	
	public void draw(Graphics2D g) {
		for (int row = rowOffset; row < rowOffset + numColsToDraw; row++) {
			if(row >= numRows)
				break;
			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) 
					break;
				if(map[row][col] == 0)
					continue;
				int rc = map[row][col];
				int r = rc / numTiilesAcross;
				int c = rc % numTiilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
			}
		}
	}
}

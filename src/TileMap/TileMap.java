package TileMap;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import Data.UserData;
import Main.GamePanel;

public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s) {
		
		try {

			tileset = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[3][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				// 0
				subimage = tileset.getSubimage(
						col * tileSize,
						0,
						tileSize,
						tileSize
					);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				// 1
				subimage = tileset.getSubimage(
						col * tileSize,
						tileSize,
						tileSize,
						tileSize
					);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
				// 2
				subimage = tileset.getSubimage(
						col * tileSize,
						tileSize*2,
						tileSize,
						tileSize
					);
				tiles[2][col] = new Tile(subimage, 20+col);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int[][] getMap() {
		return map;
	}
	
	public String getString(String data, String name) throws StringIndexOutOfBoundsException {
		return data.substring(data.indexOf("<" + name + ">") + name.length()+2, data.indexOf("</" + name + ">"));
	}
	public String[] getStringArr(String data, String name) throws StringIndexOutOfBoundsException{
		return getString(data, name).split(",");
	}
	
	public String formData = "";
public void loadMap(String s) {
		
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(s)));
			String line = "";
			String data = "";
			while((line = br.readLine()) != null) {
				data += line;
			}
					
			//= new String(Files.readAllBytes(Paths.get(getClass().getResource(s).toString())));
			String formatedData = data.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "")
					.replaceAll("\\n", "\n").replaceAll("\\s", " ");
			System.err.println(formatedData);
			formData = formatedData;
			String dataArr[] = getStringArr(formatedData, "size");
			numCols = Integer.parseInt(dataArr[0]);
			numRows = Integer.parseInt(dataArr[1]);
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			System.err.println(numCols + "x" + numRows);
			
			dataArr = getStringArr(formatedData, "map");
			
			for(int row = 0; row < numRows; row++) {
				for(int col = 0; col < numCols; col++) {
					try {
						map[row][col] =
								Integer.parseInt(dataArr[col + row*numCols]);
						
					} catch (Exception e) {
					}
				}
			}
//			for (int x = 0; x < numRows; x++) {
//				for (int y = 1; y < numCols; y++) {
////					try {
//						map[x][y-1] = Integer.parseInt(dataArr[id].replaceAll(" ", ""));
////					} catch (ArrayIndexOutOfBoundsException e) {
//////						map[x][y-1] = 1;
////					}
//					id++;
//				}
////				id++;
//			}
			
//			numCols = Integer.parseInt(dataArr[0]);
//			numRows = Integer.parseInt(dataArr[1]);
			
			
		} catch (IOException | NullPointerException e ) {
			UserData.writeData("level", "0");
			e.printStackTrace();
			System.exit(0);
		}
		
		
//		for (int i = 0; i < data.toCharArray(); i++) {
//			if()
//		}
		
		
//		try {
//			
//			InputStream in = g
//			BufferedReader br = new BufferedReader(
//						new InputStreamReader(in)
//					);
//			
//			map = new int[numRows][numCols];
//			width = numCols * tileSize;
//			height = numRows * tileSize;
//			
//			xmin = GamePanel.WIDTH - width;
//			xmax = 0;
//			ymin = GamePanel.HEIGHT - height;
//			ymax = 0;
//			
//			String delims = "\\s+";
//			for(int row = 0; row < numRows; row++) {
//				String line = br.readLine();
//				if(line.equals("#Objects")) {
//					
//				}else {
//					String[] tokens = line.split(delims);
//					for(int col = 0; col < numCols; col++) {
//						map[row][col] = Integer.parseInt(tokens[col], 16);
//					}
//				}
//			}
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		
	}
public void loadMap2(String s) {
	try {
		String data = "";
				
		data = new String(Files.readAllBytes(Paths.get(s)));
		String formatedData = data.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "")
				.replaceAll("\\n", "\n").replaceAll("\\s", " ");
		System.err.println(formatedData);
		formData = formatedData;
		String dataArr[] = getStringArr(formatedData, "size");
		numCols = Integer.parseInt(dataArr[0]);
		numRows = Integer.parseInt(dataArr[1]);
		map = new int[numRows][numCols];
		width = numCols * tileSize;
		height = numRows * tileSize;
		xmin = GamePanel.WIDTH - width;
		xmax = 0;
		ymin = GamePanel.HEIGHT - height;
		ymax = 0;
		System.err.println(numCols + "x" + numRows);
		
		dataArr = getStringArr(formatedData, "map");
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				try {
					map[row][col] =
							Integer.parseInt(dataArr[col + row*numCols]);
					
				} catch (Exception e) {
				}
			}
		}
	} catch (IOException | NullPointerException e ) {
		UserData.writeData("level", "0");
		e.printStackTrace();
		System.exit(0);
	}
}
	
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public int getType2(int r, int c) {
		return tiles[r][c].getType();
	}
	
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setTween(double d) { tween = d; }
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g) {
		
		for(
			int row = rowOffset;
			row < rowOffset + numRowsToDraw;
			row++) {
			
			if(row >= numRows) break;
			
			for(
				int col = colOffset;
				col < colOffset + numColsToDraw;
				col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
				
			}
			
		}
		
	}

	public void loadYouMap(String youLevelURL) {
			String data;
			try {
				data = new String(Files.readAllBytes(Paths.get(youLevelURL)));
			} catch (IOException e1) {
				System.exit(0);
				return;
			}
			String formatedData = data.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "")
					.replaceAll("\\n", "\n").replaceAll("\\s", " ");
			System.err.println(formatedData);
			formData = formatedData;
			String dataArr[] = getStringArr(formatedData, "size");
			numCols = Integer.parseInt(dataArr[0]);
			numRows = Integer.parseInt(dataArr[1]);
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			dataArr = getStringArr(formatedData, "map");
			for(int row = 0; row < numRows; row++) {
				for(int col = 0; col < numCols; col++) {
					try {
						map[row][col] =
								Integer.parseInt(dataArr[col + row*numCols]);
						
					} catch (Exception e) {
					}
				}
			}
	}
	
}




















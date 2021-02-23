package Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserData {

	private static final Path DATA_PATH =  Paths.get(System.getProperty("user.dir") + "\\data\\data.png");
	private static final String DEFAULT_DATA = "‰PNG, <level>0</level><gold>0</gold>";
	private static String MAIN_DATA = loadData(DEFAULT_DATA);
	
//	public int level = Integer.parseInt(getData("level", DEFAULT_DATA));
//	
//	
//	public static void main(String[] args) {
//		System.out.println(getData("level"));
//		setData("level", "12");
//	}
	public static String getData(String id) {
		return 
			MAIN_DATA.substring(
				MAIN_DATA.indexOf("<" + id + ">") + id.length() + 2,
				MAIN_DATA.indexOf("</" + id + ">")
			);
	}
	
	public static void writeData(String id, String value) {
		setData(id, value);
		updateData();
	}
	
	public static void setData(String id, String value) {
		System.out.println(MAIN_DATA);
		MAIN_DATA = 
				MAIN_DATA.substring(0,MAIN_DATA.indexOf("<" + id + ">")+id.length()+2) 
				+ value + 
				MAIN_DATA.substring(MAIN_DATA.indexOf("</" + id + ">"), MAIN_DATA.length());
		System.out.println(MAIN_DATA);
	}
	
	public static void updateData() {
		try {
			Files.write(DATA_PATH, MAIN_DATA.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String loadData(String $default) {
		try {
			return new String(Files.readAllBytes(DATA_PATH));
		} catch (IOException e) {
			try {
				Files.write(DATA_PATH, $default.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return $default;
	}
}

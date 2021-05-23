package Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserData {

	// Data
	private static final Path DATA_PATH =  Paths.get(System.getProperty("user.dir") + "/data.png");
	private static final String DEFAULT_DATA = "‰PNG, <level>0</level><gold>0</gold>";
	private static String MAIN_DATA = loadData(DEFAULT_DATA);
	
	// Date
	private static String SYSTEM_DATE = getDate();
	private static int DAY_OF_YEAR = getDayOfYear();

	// Args data
	private static String[] args;
	private static ArrayList<String> argsType = new ArrayList<String>();
	private static ArrayList<String> argsValue = new ArrayList<String>();
	
	public static boolean isAprilFoolsDay() {
		return SYSTEM_DATE.equals("01-04");
	}
	public static boolean isWebDay() {
		return SYSTEM_DATE.equals("04-04");
	}
	public static boolean isProgrammerDay() {
		return DAY_OF_YEAR == 256;
	}
	private static int getDayOfYear() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	private static String getDate() {
		SimpleDateFormat formatter= new SimpleDateFormat("dd-MM");
		Date date = new Date(System.currentTimeMillis());
		System.out.println(formatter.format(date));
		return formatter.format(date);
	}
	
	public static void setArgs(String[] args) {
		UserData.args = args;
		for (String string : args) {
			String[] s = string.toUpperCase().split("\\$");
			argsType.add(s[0]);
			argsValue.add(s.length > 1 ? s[1] : "");
		}
	}
	public static boolean hasType(String type) {
		return argsType.indexOf(type) != -1;
	}
	public static boolean hasValue(String type) {
		int index = argsType.indexOf(type);
		if(index == -1) return false;
		return !argsValue.get(index).equals("");
	}
	public static String getValue(String type) {
		int index = argsType.indexOf(type);
		if(index == -1) return null;
		return argsValue.get(index);
	}
	
	
	public static String[] getArgs() {
		return args;
	}
	
//	public static void main(String[] args) {
//		System.out.println(isAprilFoolsDay());
//		System.out.println(isWebDay());
//		System.out.println(isProgrammerDay());
//	}
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

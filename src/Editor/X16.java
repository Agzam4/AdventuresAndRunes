package Editor;

public class X16 {

	public static void main(String[] args) {
		String input = "22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22\r\n" + 
				"22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 18 22 22 22 22 22 22 22 22 22 22 22 22 22 22 22 19 17 17 17 17 17 17 17 17 17 18 22 22 22 22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 18 22 22 22 22 22 19 17 17 17 17 17 17 17 18 22\r\n" + 
				"22 4 0 0 0 0 0 0 0 0 0 0 13 0 0 0 0 0 14 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 0 0 0 0 17 17 17 18 22 22 22 22 22 22 22 22 22 22 22 4 0 0 0 0 0 0 0 0 0 0 17 18 22 22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 18 22 22 22 22 4 0 0 0 0 0 0 0 3 22\r\n" + 
				"22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 11 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 11 0 0 0 0 23 24 25 0 0 0 0 0 0 0 17 17 18 22 22 22 22 22 22 22 19 0 0 10 0 11 0 0 0 0 0 0 0 0 17 17 0 0 0 0 9 0 0 0 0 1 21 2 0 0 0 0 0 0 18 22 22 22 4 0 0 0 0 0 0 0 3 22\r\n" + 
				"22 4 0 0 0 12 0 0 0 0 0 0 11 1 21 21 2 0 0 0 0 0 0 0 0 0 0 15 0 0 0 0 0 23 25 0 0 0 0 0 0 0 0 0 0 9 11 0 0 0 0 0 17 17 17 17 17 17 17 0 0 1 21 21 21 2 0 0 0 0 0 0 0 0 0 0 0 0 0 23 24 25 0 0 3 22 4 0 0 0 0 0 0 0 18 22 22 4 0 0 0 0 0 0 0 3 22\r\n" + 
				"22 4 0 0 0 0 0 0 0 0 1 21 21 21 6 22 4 0 0 0 0 0 10 9 0 0 0 0 0 0 10 0 16 0 0 0 0 11 0 0 0 0 0 1 21 21 21 21 2 0 0 0 9 0 0 0 0 9 0 0 0 3 22 22 22 4 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3 22 4 0 0 0 0 0 0 0 0 17 17 0 0 0 0 0 0 0 0 3 22\r\n" + 
				"22 4 9 0 10 0 0 0 0 11 3 22 22 22 22 22 4 0 0 0 1 21 21 21 21 2 0 0 9 1 21 21 2 0 9 1 21 21 2 0 0 0 0 3 22 22 22 22 4 10 0 1 21 2 0 0 1 21 2 11 1 21 6 22 22 4 0 0 0 0 0 0 0 9 0 0 10 9 0 0 11 0 0 1 21 5 21 2 0 0 0 0 0 0 0 0 0 0 0 10 11 0 0 9 0 3 22\r\n" + 
				"5 21 21 21 21 21 21 21 21 21 21 6 22 22 22 22 4 0 0 0 3 22 22 22 22 21 21 21 21 21 6 5 21 21 21 21 6 22 4 0 0 0 0 3 22 22 22 5 21 21 21 21 21 21 21 21 21 21 21 21 21 6 22 22 22 4 0 0 0 0 0 1 21 21 21 21 21 21 21 21 21 21 21 21 6 22 5 21 21 21 21 21 21 21 21 21 21 21 21 21 21 21 21 21 21 21 6";

		String output = input;
		for (int i = 0; i < 50; i++) {
			output = output.replaceAll("" + i, "" + Integer.toHexString(i));
			System.err.println(i + " => " + Integer.toHexString(i));
		}
		System.out.println(output);
//		
//		String output = "";
//		String lines[] = input.replaceAll("\r", "").split("\n");
//		for (String line : lines) {
//			String[] systems = line.split(" ");
//			for (String string : systems) {
////				System.out.println(Integer.toHexString(Integer.valueOf(string)));
//				output += Integer.toHexString(Integer.valueOf(string)) + " ";
//			}
//			output += "\n";
//		}
		
	}

}
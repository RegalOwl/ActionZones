package regalowl.actionzones;

import java.util.ArrayList;

public class SerializeArrayList {
	
	public ArrayList<String> stringToArray(String commalist) {
		try {
			ArrayList<String> array = new ArrayList<String>();
			if (commalist.indexOf(",") == 0) {
				commalist = commalist.substring(1, commalist.length());
			}
			
			if (!commalist.substring(commalist.length() - 1, commalist.length()).equalsIgnoreCase(",")) {
				commalist = commalist + ",";
			}
			
			while (commalist.contains(",")) {
				array.add(commalist.substring(0, commalist.indexOf(",")));
				if (commalist.indexOf(",") == commalist.lastIndexOf(",")) {
					break;
				}
				commalist = commalist.substring(commalist.indexOf(",") + 1, commalist.length());
			}
			return array;
		} catch (Exception e) {
			ArrayList<String> array = new ArrayList<String>();
			array.add("Bad Data!");
			return array;
		}
		
		
	}
	
	
	public String arrayToString(ArrayList<String> array) {
		String string = "";
		
		int c = 0;
		while (c < array.size()) {
			string = string + array.get(c) + ",";
			c++;
		}
		return string;
		
	}
	
	

}

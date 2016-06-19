package eu.epicpvp.kcore.Util;

public class UtilInteger {

	public static int isNumber(String n){
		int number = 0;
		try{
			number = Integer.parseInt(n);
			return number;
		}catch(NumberFormatException e){					
			return -1;
		}
	}
	
}

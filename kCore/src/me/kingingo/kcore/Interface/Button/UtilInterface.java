package me.kingingo.kcore.Interface.Button;

import me.kingingo.kcore.Interface.DeathGamesInterface;

public class UtilInterface {

	public static String DGtoString(DeathGamesInterface dg){
		return dg.getChest_anzahl()+"?/?"+dg.getOn_Or_Off();
	}
	
	public static DeathGamesInterface StringToDG(String dg){
		return new DeathGamesInterface(Integer.valueOf(dg.split("?/?")[0]), Boolean.valueOf(dg.split("?/?")[1]));
	}
	
}

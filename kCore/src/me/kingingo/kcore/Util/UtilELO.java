package me.kingingo.kcore.Util;

import org.bukkit.entity.Player;

public class UtilELO {

	public static double START_WERT=200.0;
	
	public static double Erwartungswert(double winner,double loser){
		return 1 / (1+(Math.pow(10,((loser-winner)/400))));
	}
	
	public static double eloBerechnen(double winner,double loser){
		return winner+(20*( 1 - Erwartungswert(winner, loser)));
	}
}

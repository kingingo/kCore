package me.kingingo.kcore.Util;

public class UtilELO {

	public static double START_WERT=0.0;
	
	public static double Erwartungswert(double winner,double loser){
		return 1+(Math.pow(10,((loser-winner)/400))) /1;
	}
	
	public static double eloBerechnen(double winner,double loser){
		return winner+(20*( 1 - Erwartungswert(winner, loser)));
	}
	
}

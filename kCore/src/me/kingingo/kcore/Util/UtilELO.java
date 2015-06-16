package me.kingingo.kcore.Util;

public class UtilELO {

	public static double Erwartungswert(int winner,int loser){
		return 1+(Math.pow(10,((loser-winner)/400))) /1;
	}
	
	public static double eloBerechnen(int winner,int loser){
		return winner+(20*( 1 - Erwartungswert(winner, loser)));
	}
	
}

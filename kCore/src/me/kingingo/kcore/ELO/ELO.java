package me.kingingo.kcore.ELO;

import me.kingingo.kcore.ELO.Events.PlayerEloEvent;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ELO {
public static double START_WERT=200.0;

	public static double eloBerechnenProzent(double winner,double loser, double prozent){
		return winner+( (loser/100) * prozent);
	}

	public static double Erwartungswert(double winner,double loser){
		return 1 / (1+(Math.pow(10,((loser-winner)/400))));
	}
	
	public static double eloBerechnen(double winner,double loser){
		return winner+(20*( 1 - Erwartungswert(winner, loser)));
	}
	
	public static void eloCHANGEProzent(Player killer,Player lose,int prozent,StatsManager stats){
		double killer_elo = stats.getDouble(Stats.ELO, killer);
		double lose_elo = stats.getDouble(Stats.ELO, lose);
		
		double killer_new = eloBerechnenProzent(killer_elo, lose_elo,prozent);
		
		Bukkit.getPluginManager().callEvent(new PlayerEloEvent(killer,killer_elo,killer_new));
		Bukkit.getPluginManager().callEvent(new PlayerEloEvent(lose,lose_elo,START_WERT));
		
		stats.setDouble(killer, killer_new, Stats.ELO);
		stats.setDouble(lose,lose_elo, Stats.TIME_ELO);
		stats.setString(lose, ""+System.currentTimeMillis(), Stats.TIME);
		stats.setDouble(lose, START_WERT, Stats.ELO);
	}
	
	public static void eloCHANGE(Player lose,StatsManager stats){
		double lose_elo = stats.getDouble(Stats.ELO, lose);
		Bukkit.getPluginManager().callEvent(new PlayerEloEvent(lose,lose_elo,START_WERT));
		stats.setDouble(lose,lose_elo, Stats.TIME_ELO);
		stats.setString(lose, ""+System.currentTimeMillis(), Stats.TIME);
		stats.setDouble(lose, START_WERT, Stats.ELO);
	}
	
	public static void eloCHANGE(Player killer,Player lose,StatsManager stats){
		double killer_elo = stats.getDouble(Stats.ELO, killer);
		double lose_elo = stats.getDouble(Stats.ELO, lose);
		
		double killer_new = eloBerechnen(killer_elo, lose_elo);
		
		Bukkit.getPluginManager().callEvent(new PlayerEloEvent(killer,killer_elo,killer_new));
		Bukkit.getPluginManager().callEvent(new PlayerEloEvent(lose,lose_elo,START_WERT));
		
		stats.setDouble(killer, killer_new, Stats.ELO);
		stats.setDouble(lose,lose_elo, Stats.TIME_ELO);
		stats.setString(lose, ""+System.currentTimeMillis(), Stats.TIME);
		stats.setDouble(lose, START_WERT, Stats.ELO);
	}
}

package me.kingingo.kcore.PrivatServer.Interface.Button;

import org.bukkit.entity.Player;

import me.kingingo.kcore.PrivatServer.Interface.DeathGamesInterface;

public class UtilInterface {

	public static String DGtoString(DeathGamesInterface dg,Player player){
		return (dg.getChest_anzahl().containsKey(player) ? dg.getChest_anzahl().get(player) : 120)+"?/?"+ (dg.getKits().containsKey(player) ? dg.getKits().get(player) : true);
	}
	
	public static int DG_Chest(String dg){
		return Integer.valueOf(dg.split("?/?")[0]);
	}
	
	public static boolean DG_Kits(String dg){
		return Boolean.valueOf(dg.split("?/?")[1]);
	}
	
}

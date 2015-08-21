package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;

public class Money {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==3){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			String g = manager.getPlayerGilde(p);
			
			if(args[1].equalsIgnoreCase("abheben")||args[1].equalsIgnoreCase("take")){
				UUID owner=manager.getOwner(g);
				if(!owner.equals(UtilPlayer.getRealUUID(p))){
					p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_OWNER_NOT"));
					return;
				}
				double money = UtilNumber.toDouble(args[2]);
				if(money==0){
					p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "NO_INTEGER"));
				}else{
					if(money<0)return;
					
					if(manager.getDouble(Stats.MONEY, g) < money){
						p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_NOT_ENOUGH_MONEY"));
					}else{
						System.err.println("[GildenManager] Gilde:"+g);
						System.err.println("[GildenManager] Kontostand-Gilde: "+manager.getDouble(Stats.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player: "+manager.getStatsManager().getDouble(Stats.MONEY, p));
						System.err.println("[GildenManager] Einzahlen: "+money);
						manager.setDouble(g, manager.getDouble(Stats.MONEY, g)-money, Stats.MONEY);
						manager.getStatsManager().setDouble(p, manager.getStatsManager().getDouble(Stats.MONEY, p)+money, Stats.MONEY);
						manager.sendGildenChat(g, "GILDE_MONEY_LIFTED",new String[]{p.getName(),String.valueOf(money)});
						System.err.println("[GildenManager] Kontostand-Gilde danach: "+manager.getDouble(Stats.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player danach: "+manager.getStatsManager().getDouble(Stats.MONEY, p));
					}
				}
			}else if(args[1].equalsIgnoreCase("einzahlen")||args[1].equalsIgnoreCase("pay")){
				double money = UtilNumber.toDouble(args[2]);
				if(money==0){
					p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "NO_INTEGER"));
				}else{
					if(money<0)return;
					
					if(manager.getStatsManager().getDouble(Stats.MONEY, p) < money){
						p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_NOT_ENOUGH_MONEY"));
					}else{
						System.err.println("[GildenManager] Gilde:"+g);
						System.err.println("[GildenManager] Kontostand-Gilde: "+manager.getDouble(Stats.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player: "+manager.getStatsManager().getDouble(Stats.MONEY, p));
						System.err.println("[GildenManager] Einzahlen: "+money);
						manager.setDouble(g, manager.getDouble(Stats.MONEY, g)+money, Stats.MONEY);
						manager.getStatsManager().setDouble(p, manager.getStatsManager().getDouble(Stats.MONEY, p)-money, Stats.MONEY);
						System.err.println("[GildenManager] Kontostand-Gilde danach: "+manager.getDouble(Stats.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player danach: "+manager.getStatsManager().getDouble(Stats.MONEY, p));
						manager.sendGildenChat(g, "GILDE_MONEY_DEPOSIT",new String[]{p.getName(),String.valueOf(money)});
					}
				}	
			}else{
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde money abheben [Money]");
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde money einzahlen [Money]");
			}
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde money abheben [Money]");
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde money einzahlen [Money]");
		}
	}
	
}

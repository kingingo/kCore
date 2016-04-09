package eu.epicpvp.kcore.Gilden.Commands;

import java.util.UUID;

import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Money {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==3){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			String g = manager.getPlayerGilde(p);
			
			if(args[1].equalsIgnoreCase("abheben")||args[1].equalsIgnoreCase("take")||args[1].equalsIgnoreCase("withdraws")){
				UUID owner=manager.getOwner(g);
				if(!owner.equals(UtilPlayer.getRealUUID(p))){
					p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_OWNER_NOT"));
					return;
				}
				double money = UtilNumber.toDouble(args[2]);
				if(money==0){
					p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "NO_INTEGER"));
				}else{
					if(money<0)return;
					
					if(manager.getDouble(StatsKey.MONEY, g) < money){
						p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_NOT_ENOUGH_MONEY"));
					}else{
						System.err.println("[GildenManager] Gilde:"+g);
						System.err.println("[GildenManager] Kontostand-Gilde: "+manager.getDouble(StatsKey.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player: "+manager.getStatsManager().getDouble(p, StatsKey.MONEY));
						System.err.println("[GildenManager] Einzahlen: "+money);
						manager.setDouble(g, manager.getDouble(StatsKey.MONEY, g)-money, StatsKey.MONEY);
						manager.getStatsManager().add(p, StatsKey.MONEY,money);
						manager.sendGildenChat(g, "GILDE_MONEY_LIFTED",new String[]{p.getName(),String.valueOf(money)});
						System.err.println("[GildenManager] Kontostand-Gilde danach: "+manager.getDouble(StatsKey.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player danach: "+manager.getStatsManager().getDouble(p, StatsKey.MONEY));
					}
				}
			}else if(args[1].equalsIgnoreCase("einzahlen")||args[1].equalsIgnoreCase("pay")||args[1].equalsIgnoreCase("deposite")){
				double money = UtilNumber.toDouble(args[2]);
				if(money==0){
					p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "NO_INTEGER"));
				}else{
					if(money<0)return;
					
					if(manager.getStatsManager().getDouble(p, StatsKey.MONEY) < money){
						p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_NOT_ENOUGH_MONEY"));
					}else{
						System.err.println("[GildenManager] Gilde:"+g);
						System.err.println("[GildenManager] Kontostand-Gilde: "+manager.getDouble(StatsKey.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player: "+manager.getStatsManager().getDouble(p, StatsKey.MONEY));
						System.err.println("[GildenManager] Einzahlen: "+money);
						manager.setDouble(g, manager.getDouble(StatsKey.MONEY, g)+money, StatsKey.MONEY);
						manager.getStatsManager().add(p, StatsKey.MONEY,-money);
						System.err.println("[GildenManager] Kontostand-Gilde danach: "+manager.getDouble(StatsKey.MONEY, g));
						System.err.println("[GildenManager] Kontostand-Player danach: "+manager.getStatsManager().getDouble(p, StatsKey.MONEY));
						manager.sendGildenChat(g, "GILDE_MONEY_DEPOSIT",new String[]{p.getName(),String.valueOf(money)});
					}
				}	
			}else{
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+" /gilde money abheben [Money]");
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+" /gilde money einzahlen [Money]");
			}
		}else{
			p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+" /gilde money abheben [Money]");
			p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+" /gilde money einzahlen [Money]");
		}
	}
	
}

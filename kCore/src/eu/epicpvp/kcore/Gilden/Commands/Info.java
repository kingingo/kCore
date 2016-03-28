package eu.epicpvp.kcore.Gilden.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.wolveringer.dataclient.gamestats.StatsKey;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Gilden.GildenManager;

public class Info {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			p.sendMessage(Language.getText(p, "GILDE_STATS_PREFIX"));
			String g = manager.getPlayerGilde(p);
			for(StatsKey s : manager.getTyp().getStats()){
				if(s==StatsKey.LOC_X||s==StatsKey.LOC_Y||s==StatsKey.LOC_Z||s==StatsKey.WORLD)continue;
				
				if(s==StatsKey.ELO){
					p.sendMessage("\u00A756FAME: \u00A75b"+manager.getDouble(s, g, manager.getTyp()));
				}else if(s.getType() == int.class){
					p.sendMessage("\u00A756"+s.getMySQLName()+": \u00A75b"+manager.getInt(s, g, manager.getTyp()));
				}else if(s.getType() == double.class){
					p.sendMessage("\u00A756"+s.getMySQLName()+": \u00A75b"+manager.getDouble(s, g, manager.getTyp()));
				}
			}
			manager.getMember(g);
			p.sendMessage("\u00A756Anzahl: \u00A75b"+manager.getAnzahl(g)+"/10");
			String[] players = manager.getGildenPlayersName(g);
			String l = "\u00A756List: ";
			for(String player : players){
				if(UtilPlayer.isOnline(player)){
					l=l+" \u00A75a"+Bukkit.getPlayer(player).getName()+",";
				}else{
					l=l+" \u00A75c"+player+",";
				}
			}
			l=l.substring(0, l.length()-1);
			p.sendMessage(l);
		}else if(args.length==2){
			String g = args[1];
			if(!manager.ExistGilde(g)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_EXIST_NOT"));
				return;
			}
			p.sendMessage(Language.getText(p, "GILDE_STATS_PREFIXBY",g));
			for(StatsKey s : manager.getTyp().getStats()){
				if(s==StatsKey.LOC_X||s==StatsKey.LOC_Y||s==StatsKey.LOC_Z||s==StatsKey.WORLD)continue;
				
				if(s==StatsKey.ELO){
					p.sendMessage("\u00A756FAME: \u00A75b"+manager.getDouble(s, g, manager.getTyp()));
				}else if(s.getType() == int.class){
					p.sendMessage("\u00A756"+s.getMySQLName()+": \u00A75b"+manager.getInt(s, g, manager.getTyp()));
				}else if(s.getType() == double.class){
					p.sendMessage("\u00A756"+s.getMySQLName()+": \u00A75b"+manager.getDouble(s, g, manager.getTyp()));
				}
			}
			manager.getMember(g);
			String[] players = manager.getGildenPlayersName(g);
			String l = "\u00A756List: ";
			for(String player : players){
				if(UtilPlayer.isOnline(player)){
					l=l+" \u00A75a"+Bukkit.getPlayer(player).getName()+",";
				}else{
					l=l+" \u00A75c"+player+",";
				}
			}
			l=l.substring(0, l.length()-1);
			p.sendMessage(l);
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde info oder /gilde info [Gilde]");
		}
	}
	
}

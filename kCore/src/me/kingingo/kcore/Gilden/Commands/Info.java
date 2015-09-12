package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Info {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			p.sendMessage(Language.getText(p, "GILDE_STATS_PREFIX"));
			String g = manager.getPlayerGilde(p);
			for(Stats s : manager.getTyp().getStats()){
				if(s==Stats.LOC_X||s==Stats.LOC_Y||s==Stats.LOC_Z||s==Stats.WORLD)continue;
				
				if(s==Stats.ELO){
					p.sendMessage("�6FAME: �b"+manager.getDouble(s, g, manager.getTyp()));
				}else if(s.getCREATE().contains("int")){
					p.sendMessage("�6"+s.getK�RZEL()+": �b"+manager.getInt(s, g, manager.getTyp()));
				}else if(s.getCREATE().contains("double")){
					p.sendMessage("�6"+s.getK�RZEL()+": �b"+manager.getDouble(s, g, manager.getTyp()));
				}
			}
			manager.getMember(g);
			p.sendMessage("�6Anzahl: �b"+manager.getAnzahl(g)+"/10");
			String[] players = manager.getGildenPlayersName(g);
			String l = "�6List: ";
			for(String player : players){
				if(UtilPlayer.isOnline(player)){
					l=l+" �a"+Bukkit.getPlayer(player).getName()+",";
				}else{
					l=l+" �c"+player+",";
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
			for(Stats s : manager.getTyp().getStats()){
				if(s==Stats.LOC_X||s==Stats.LOC_Y||s==Stats.LOC_Z||s==Stats.WORLD)continue;
				
				if(s==Stats.ELO){
					p.sendMessage("�6FAME: �b"+manager.getDouble(s, g, manager.getTyp()));
				}else if(s.getCREATE().contains("int")){
					p.sendMessage("�6"+s.getK�RZEL()+": �b"+manager.getInt(s, g, manager.getTyp()));
				}else if(s.getCREATE().contains("double")){
					p.sendMessage("�6"+s.getK�RZEL()+": �b"+manager.getDouble(s, g, manager.getTyp()));
				}
			}
			manager.getMember(g);
			String[] players = manager.getGildenPlayersName(g);
			String l = "�6List: ";
			for(String player : players){
				if(UtilPlayer.isOnline(player)){
					l=l+" �a"+Bukkit.getPlayer(player).getName()+",";
				}else{
					l=l+" �c"+player+",";
				}
			}
			l=l.substring(0, l.length()-1);
			p.sendMessage(l);
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde info oder /gilde info [Gilde]");
		}
	}
	
}

package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Info {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			p.sendMessage(Text.GILDE_STATS_PREFIX.getText());
			String g = manager.getPlayerGilde(p);
			for(Stats s : manager.getTyp().getStats()){
				if(s==Stats.LOC_X||s==Stats.LOC_Y||s==Stats.LOC_Z||s==Stats.WORLD)continue;
				p.sendMessage("§6"+s.getKÜRZEL()+": §b"+manager.getInt(s, g, manager.getTyp()));
			}
			manager.getMember(g);
			p.sendMessage("§6Anzahl: §b"+manager.getAnzahl(g)+"/10");
			
			String l = "§6List: ";
			for(UUID n : manager.getGilden_player().keySet()){
				if(manager.getGilden_player().get(n).equalsIgnoreCase(g)){
					if(UtilPlayer.isOnline(n)){
						l=l+" §a"+Bukkit.getPlayer(n).getName()+",";
					}else{
						if(Bukkit.getOfflinePlayer(n)==null)continue;
						l=l+" §c"+Bukkit.getOfflinePlayer(n).getName()+",";
					}
				}
			}
			l=l.substring(0, l.length()-1);
			p.sendMessage(l);
		}else if(args.length==2){
			String g = args[1];
			if(!manager.ExistGilde(g)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EXIST_NOT.getText());
				return;
			}
			p.sendMessage(Text.GILDE_STATS_PREFIXBY.getText(g));
			for(Stats s : manager.getTyp().getStats()){
				if(s==Stats.LOC_X||s==Stats.LOC_Y||s==Stats.LOC_Z||s==Stats.WORLD)continue;
				p.sendMessage("§6"+s.getKÜRZEL()+": §b"+manager.getInt(s, g, manager.getTyp()));
			}
			manager.getMember(g);
			String l = "§6List: ";
			for(UUID n : manager.getGilden_player().keySet()){
				if(manager.getGilden_player().get(n).equalsIgnoreCase(g)){
					if(UtilPlayer.isOnline(n)){
						l=l+" §a"+Bukkit.getPlayer(n).getName()+",";
					}else{
						if(Bukkit.getOfflinePlayer(n)==null&&Bukkit.getOfflinePlayer(n).getName().equalsIgnoreCase("null"))continue;
						l=l+" §c"+Bukkit.getOfflinePlayer(n).getName()+",";
					}
				}
			}
			l=l.substring(0, l.length()-1);
			p.sendMessage(l);
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde info oder /gilde info [Gilde]");
		}
	}
	
}

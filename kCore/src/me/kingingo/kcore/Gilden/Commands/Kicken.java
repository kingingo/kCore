package me.kingingo.kcore.Gilden.Commands;

import java.util.ArrayList;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Kicken {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length>=2){
			if(!manager.isPlayerInGilde(p.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			String g = manager.getPlayerGilde(p.getName());
			String owner=manager.getOwner(g);
			if(!owner.equalsIgnoreCase(p.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_OWNER_NOT.getText());
				return;
			}
			String kick_o = args[2];
			if(manager.isPlayerInGilde(kick_o)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_IS_NOT_IN_THE_GILD.getText(kick_o));
				return;
			}
			if(!manager.getPlayerGilde(kick_o).equalsIgnoreCase(g)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_IS_NOT_IN_THE_GILD.getText(kick_o));
				return;
			}
			manager.sendGildenChat(g, Text.GILDE_PREFIX.getText()+Text.GILDE_KICK_PLAYER.getText(kick_o));
			manager.removePlayerEintrag(kick_o);
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde kicken [Player]");
		}
	}
	
}

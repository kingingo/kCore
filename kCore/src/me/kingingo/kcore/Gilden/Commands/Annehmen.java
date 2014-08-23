package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Annehmen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length>=1){
			if(manager.getGilden_einladung().containsKey(p)){
				if(manager.isPlayerInGilde(p.getName())){
					p.sendMessage(Text.GILDE_PLAYER_IS_IN_GILDE.getText());
					return;
				}
				String g = manager.getGilden_einladung().get(p);
				manager.createPlayerEintrag(p.getName(), p.getUniqueId().toString(), g);
				manager.getGilden_player().put(p.getName(), g);
				manager.sendGildenChat(g, Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_ENTRE.getText(p.getName()));
			}else{
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_NICHT_EINGELADEN.getText());
			}
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde annehmen");
		}
	}
	
}

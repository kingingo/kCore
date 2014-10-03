package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;

import org.bukkit.entity.Player;

public class Annehmen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.getGilden_einladung().containsKey(p)){
				if(manager.isPlayerInGilde(p.getName())){
					p.sendMessage(Text.GILDE_PLAYER_IS_IN_GILDE.getText());
					return;
				}
				String g = manager.getGilden_einladung().get(p);
				if(manager.getAnzahl(g) >= 10){
					p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_COUNT.getText());
					manager.getGilden_einladung().remove(p);
					return;
				}
				manager.getGilden_count().remove(g.toLowerCase());
				manager.createPlayerEintrag(p.getName(), p.getUniqueId().toString(), g);
				manager.sendGildenChat(g, Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_ENTRE.getText(p.getName()));
			}else{
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_NICHT_EINGELADEN.getText());
			}
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde annehmen");
		}
	}
	
}

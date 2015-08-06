package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Language.Language;

import org.bukkit.entity.Player;

public class Annehmen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.getGilden_einladung().containsKey(p)){
				if(manager.isPlayerInGilde(p)){
					p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_IN_GILDE"));
					return;
				}
				String g = manager.getGilden_einladung().get(p);
				if(manager.getAnzahl(g) >= 10){
					p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_COUNT"));
					manager.getGilden_einladung().remove(p);
					return;
				}
				manager.getGilden_count().remove(g.toLowerCase());
				manager.createPlayerEintrag(p,g);
				manager.sendGildenChat(g,"GILDE_PLAYER_ENTRE",p.getName());
			}else{
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_NICHT_EINGELADEN"));
			}
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde annehmen");
		}
	}
	
}

package eu.epicpvp.kcore.Gilden.Commands;

import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import org.bukkit.entity.Player;

public class Kicken {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			String g = manager.getPlayerGilde(p);
			int owner = manager.getOwner(manager.getPlayerGilde(p));
			if(owner!=UtilPlayer.getPlayerId(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_OWNER_NOT"));
				return;
			}
			String kick_o = args[1];
			int kick_id = UtilPlayer.getPlayerId(kick_o);
			if(!manager.isPlayerInGilde(kick_id)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_IS_NOT_IN_THE_GILD",kick_o));
				return;
			}
			if(!manager.getPlayerGilde(kick_id).equalsIgnoreCase(g)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_IS_NOT_IN_THE_GILD",kick_o));
				return;
			}

			manager.onKick(kick_o, kick_id);

			manager.sendGildenChat(g, "GILDE_KICK_PLAYER",kick_o);
			manager.removePlayerEintrag(kick_id,kick_o);
		}else{
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" /gilde kicken [Player]");
		}
	}
}

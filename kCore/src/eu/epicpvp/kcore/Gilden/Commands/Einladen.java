package eu.epicpvp.kcore.Gilden.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Einladen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			String einladen_o = args[1];
			if(!UtilPlayer.isOnline(einladen_o)){
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_PLAYER_OFFLINE",einladen_o));
				return;
			}
			Player einladen = Bukkit.getPlayer(einladen_o);
			if(manager.isPlayerInGilde(einladen)){
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_PLAYER_IS_IN_GILDE1",einladen_o));
				return;
			}
			
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE",einladen_o));
				return;
			}
			UUID owner = manager.getOwner(manager.getPlayerGilde(p));
			if(!owner.equals(UtilPlayer.getRealUUID(p))){
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_OWNER_NOT"));
				return;
			}
			
			if(UtilDebug.isDebug())UtilDebug.debug("Gilde CMD Einladen", "Gilde:"+manager.getPlayerGilde(p)+" Anzahl: "+manager.getAnzahl(manager.getPlayerGilde(p)));
			
			if(manager.getAnzahl(manager.getPlayerGilde(p)) >= 10){
				p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_COUNT"));
				return;
			}
			
			manager.getGilden_einladung().put(einladen, manager.getPlayerGilde(p));
			p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_EINLADEN",einladen_o));
			einladen.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+TranslationManager.getText(p, "GILDE_EILADUNG",manager.getPlayerGilde(owner)));
		}else{
			p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+"/gilde einladen [Player]");
		}
	}
	
}

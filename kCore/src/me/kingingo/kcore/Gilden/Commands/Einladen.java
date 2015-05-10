package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Einladen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			String einladen_o = args[1];
			if(!UtilPlayer.isOnline(einladen_o)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_OFFLINE.getText(einladen_o));
				return;
			}
			Player einladen = Bukkit.getPlayer(einladen_o);
			if(manager.isPlayerInGilde(einladen)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_IN_GILDE1.getText(einladen_o));
				return;
			}
			
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText(einladen_o));
				return;
			}
			UUID owner = manager.getOwner(manager.getPlayerGilde(p));
			if(!owner.equals(UtilPlayer.getRealUUID(p))){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_OWNER_NOT.getText());
				return;
			}
			
			System.out.println("DEBUG GILDE_ANZAHL: "+manager.getAnzahl(manager.getPlayerGilde(p)));
			if(manager.getAnzahl(manager.getPlayerGilde(p)) >= 10){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_COUNT.getText());
				return;
			}
			
			manager.getGilden_einladung().put(einladen, manager.getPlayerGilde(p));
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EINLADEN.getText(einladen_o));
			einladen.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EILADUNG.getText( manager.getPlayerGilde(owner) ));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+"/gilde einladen [Player]");
		}
	}
	
}

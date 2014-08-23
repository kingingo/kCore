package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Einladen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			String einladen_o = args[2];
			if(!UtilPlayer.isOnline(einladen_o)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_OFFLINE.getText(einladen_o));
				return;
			}
			Player einladen = Bukkit.getPlayer(einladen_o);
			if(manager.isPlayerInGilde(einladen.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_IN_GILDE.getText());
				return;
			}
			if(manager.isPlayerInGilde(p.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			String owner = manager.getOwner(manager.getPlayerGilde(p.getName()));
			if(!owner.equalsIgnoreCase(p.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_OWNER_NOT.getText());
				return;
			}
			manager.getGilden_einladung().put(einladen, manager.getPlayerGilde(p.getName()));
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EINLADEN.getText(manager.getPlayerGilde(p.getName())));
			einladen.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EILADUNG.getText(manager.getPlayerGilde(p.getName())));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde einladen [Player]");
		}
	}
	
}

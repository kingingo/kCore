package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;

public class Verlassen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.isPlayerInGilde(p)){
				String g = manager.getPlayerGilde(p);
				UUID owner = manager.getOwner(g);
				if(owner==null){
					System.err.println("[GildenManager] Command Verlassen: Owner == NULL");
					return;
				}
				if(owner.equals(UtilPlayer.getRealUUID(p))){
					manager.sendGildenChat(g, Text.GILDE_PREFIX.getText()+Text.GILDE_CLOSED.getText());
					manager.removeGildenEintrag(g);
				}else{
					manager.sendGildenChat(g, Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_GO_OUT.getText(p.getName()));
					manager.removePlayerEintrag(p);
					p.setDisplayName(p.getName());
				}
			}else{
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_IN_GILDE.getText());
			}
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde verlassen");
		}
	}
	
}

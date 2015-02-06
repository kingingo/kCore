package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;

public class Erstellen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(manager.isPlayerInGilde(p)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_IN_GILDE.getText());
				return;
			}
			String g = args[1];
			if(g.length()<2){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_NAME_LENGTH_MIN.getText(2));
				return;
			}
			if(g.length()>5){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_NAME_LENGTH_MAX.getText(5));
				return;
			}
			if(manager.ExistGilde(g)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EXIST.getText());
				return;
			}
			
			boolean o[] = new boolean[g.length()];
			boolean n = true;
			char[] zeichen = g.toCharArray();
			char[] a = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
			
			for(int i = 0;i<zeichen.length;i++){
				for(int u=0;u<a.length;u++){
					
					if(zeichen[i] == a[u]){
						o[i] = true;
						break;
					}
					
				}
			}
			
			for (int i = 0; i < o.length; i++) {
				if (!o[i]) {
					n = false;
					break;
				}
			}
			
			
			if(!n){
				p.sendMessage(Text.GILDE_PREFIX.getText()+" §c§lDu hast ein Ungültiges Zeichen in deinen Clannamen!");
				return;
			}
			
			manager.createGildenEintrag(g, "§7"+g+"§b*§f", 10, UtilPlayer.getRealUUID(p));
			manager.createPlayerEintrag(p, g);
			manager.setInt(g, p.getLocation().getBlockX(), Stats.LOC_X);
			manager.setInt(g, p.getLocation().getBlockY(), Stats.LOC_Y);
			manager.setInt(g, p.getLocation().getBlockZ(), Stats.LOC_Z);
			manager.setString(g, p.getLocation().getWorld().getName(), Stats.WORLD);
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_CREATE.getText(g));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde erstellen [Gilde]");
		}
	}
	
}

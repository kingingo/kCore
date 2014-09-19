package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.PlayerStats.Stats;

import org.bukkit.entity.Player;

public class Erstellen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(manager.isPlayerInGilde(p.getName())){
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
			manager.createGildenEintrag(g, "�7"+g+"�b*�f", 10, p.getName());
			manager.createPlayerEintrag(p.getName(), p.getUniqueId().toString(), g);
			manager.setInt(g, manager.getTyp(), p.getLocation().getBlockX(), Stats.LOC_X);
			manager.setInt(g, manager.getTyp(), p.getLocation().getBlockY(), Stats.LOC_Y);
			manager.setInt(g, manager.getTyp(), p.getLocation().getBlockZ(), Stats.LOC_Z);
			manager.setString(g, manager.getTyp(), p.getLocation().getWorld().getName(), Stats.WORLD);
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_CREATE.getText(g));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde erstellen [Gilde]");
		}
	}
	
}

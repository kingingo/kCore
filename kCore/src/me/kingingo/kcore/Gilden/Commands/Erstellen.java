package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Gilden.SkyBlockGildenManager;
import me.kingingo.kcore.StatsManager.Stats;
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
			
			if(g.contains("'"))return;
			
			if(manager.ExistGilde(g)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_EXIST.getText());
				return;
			}
			
			if(!g.matches("[a-zA-Z0-9]*")){
				p.sendMessage(Text.GILDE_PREFIX.getText()+" §c§lDu hast ein Ungültiges Zeichen in deinen Clannamen!");
				return;
			}
			
			if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				if(sky.getStats().getDouble(Stats.MONEY, p)>=500.0){
					sky.getStats().setDouble(p, sky.getStats().getDouble(Stats.MONEY, p)-500.0, Stats.MONEY);
				}else{
					p.sendMessage(Text.PREFIX.getText()+"Du brauchst 500 Epics um eine Gilde zu erstellen.");
					return;
				}
			}
			
			manager.createGildenEintrag(g, "§7"+g+"§b*§f", 10, UtilPlayer.getRealUUID(p));
			manager.createPlayerEintrag(p, g);
			if(manager.getTyp()==GildenType.PVP){
				manager.setInt(g, p.getLocation().getBlockX(), Stats.LOC_X);
				manager.setInt(g, p.getLocation().getBlockY(), Stats.LOC_Y);
				manager.setInt(g, p.getLocation().getBlockZ(), Stats.LOC_Z);
				manager.setString(g, p.getLocation().getWorld().getName(), Stats.WORLD);
			}
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_CREATE.getText(g));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde erstellen [Gilde]");
		}
	}
	
}

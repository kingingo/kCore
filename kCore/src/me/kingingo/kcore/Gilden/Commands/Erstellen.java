package me.kingingo.kcore.Gilden.Commands;

import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Gilden.SkyBlockGildenManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;

public class Erstellen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(manager.isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_IN_GILDE"));
				return;
			}
			String g = args[1];
			if(g.length()<2){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_NAME_LENGTH_MIN",2));
				return;
			}
			if(g.length()>5){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_NAME_LENGTH_MAX",5));
				return;
			}
			
			if(g.contains("'"))return;
			
			if(manager.ExistGilde(g)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_EXIST"));
				return;
			}
			
			if(!g.matches("[a-zA-Z0-9]*")){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" §c§lDu hast ein Ungültiges Zeichen in deinen Clannamen!");
				return;
			}
			
			if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				if(sky.getStats().getDouble(Stats.MONEY, p)>=500.0){
					sky.getStats().setDouble(p, sky.getStats().getDouble(Stats.MONEY, p)-500.0, Stats.MONEY);
				}else{
					p.sendMessage(Language.getText(p, "PREFIX")+"Du brauchst 500 Epics um eine Gilde zu erstellen.");
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
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_CREATE",g));
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde erstellen [Gilde]");
		}
	}
	
}

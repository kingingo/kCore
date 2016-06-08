package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class TheWalkingDead extends Achievement{

	public TheWalkingDead() {
		super("§aThe Walking Dead", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Töte insgesamt 1000 Monster"," "),500,1000);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void BlockBreak(EntityDeathEvent ev){
		if(ev.getEntityType() == EntityType.PLAYER)return;
		if(ev.getEntity().getKiller()==null)return;
		
		int playerId = UtilPlayer.getPlayerId(ev.getEntity().getKiller());
		if(this.getPlayerProgress().containsKey(playerId)){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}

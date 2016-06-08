package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class SerieKiller extends Achievement{

	public SerieKiller() {
		super("§aSerienkiller", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Töte 100 Spieler."," "),100,500,true);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void PlayerDeath(PlayerDeathEvent ev){
		if(ev.getEntity().getKiller()==null)return;
		int playerId = UtilPlayer.getPlayerId(ev.getEntity().getKiller());
		if(this.getPlayerProgress().containsKey(playerId)){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}

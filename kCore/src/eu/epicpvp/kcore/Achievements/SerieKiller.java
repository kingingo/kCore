package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev.wolveringer.client.Callback;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class SerieKiller extends Achievement{

	public SerieKiller(){
		this(null);
	}

	public SerieKiller(Callback<Integer> done) {
		super("§aSerienkiller", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Töte 100 Spieler."," "),done,true,100);
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

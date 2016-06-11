package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import dev.wolveringer.client.Callback;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class BobTheDestroyer extends Achievement{

	public BobTheDestroyer(){
		this(null);
	}

	public BobTheDestroyer(Callback<Integer> done) {
		super("§aBob der Zerstörer", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Baue 10000 Blöcke ab"," "),done,false,10000);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void BlockBreak(BlockBreakEvent ev){
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		if(this.getPlayerProgress().containsKey(playerId)&&!ev.isCancelled()){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}

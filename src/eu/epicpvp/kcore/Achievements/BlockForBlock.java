package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import dev.wolveringer.client.Callback;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class BlockForBlock extends Achievement{

	public BlockForBlock(){
		this(null);
	}
	
	public BlockForBlock(Callback<Integer> done) {
		super("§aBlock Für Block", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Platziere 10000 Blöcke"," "),done,false,10000);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void placeBlock(BlockPlaceEvent ev){
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		if(this.getPlayerProgress().containsKey(playerId)&&!ev.isCancelled()){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}

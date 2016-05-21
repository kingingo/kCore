package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class BillGates extends Achievement{

	public BillGates() {
		super("§aBill Gates", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Besitzte 250.000 Epic's"," "),5000,1);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void PlayerStatsChanged(PlayerStatsChangedEvent ev){
		if(ev.getManager().getType() != getType())return;
		if(ev.getStats() != StatsKey.MONEY)return;
		if(ev.getManager().getDouble(ev.getPlayerId(), ev.getStats()) < 250000)return;
		
		if(this.getPlayerProgress().containsKey(ev.getPlayerId())){
			this.getPlayerProgress().put(ev.getPlayerId(), getProgress(ev.getPlayerId())+1);
			done(ev.getPlayerId());
		}
	}
}

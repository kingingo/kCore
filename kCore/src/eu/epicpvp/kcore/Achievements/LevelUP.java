package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class LevelUP extends Achievement{

	public LevelUP() {
		super("§aLevelUP", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Erreiche 100 XP Level"," "),1000,1);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void PlayerExpChange(PlayerExpChangeEvent ev){
		if(ev.getPlayer().getLevel() < 100)return;
		
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		if(this.getPlayerProgress().containsKey(playerId)){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}

package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class NiceProduct extends Achievement{

	public NiceProduct() {
		super("§aFeinsteWare", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Schere 500 Schafe."," "),500,500);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void PlayerShearEntity(PlayerShearEntityEvent ev){
		if(ev.getEntity().getType() != EntityType.SHEEP)return;
		
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		if(this.getPlayerProgress().containsKey(playerId)){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}

package me.kingingo.kcore.Fame;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.PlayerStats.StatsManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FameManager extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private GameType type;
	@Getter
	private StatsManager statsManager;
	
	public FameManager(JavaPlugin instance,StatsManager statsManager,GameType type) {
		super(instance, "[FameManager]");
		this.instance=instance;
		this.type=type;
		this.statsManager=statsManager;
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getEntity().getKiller() instanceof Player){
			getStatsManager().setInt(ev.getEntity().getKiller(), getStatsManager().getInt(Stats.FAME_KILLS, ev.getEntity().getKiller()), Stats.FAME_KILLS);
		}
	}

}

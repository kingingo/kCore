package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Achievements.Events.PlayerLoadAchievementsEvent;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Util.TimeSpan;

public class Obsession extends Achievement{

	private StatsManager stats;
	
	public Obsession() {
		super("§aSucht!", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Spiele 24 Stunden auf dem Skyblock Server."," "),5000,1);
		this.stats=StatsManagerRepository.getStatsManager(GameType.TIME);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void join(PlayerLoadAchievementsEvent ev){
		if(ev.getAchievement()==this){
			stats.loadPlayer(ev.getPlayer());
		}
	}
	
	@EventHandler
	public void load(PlayerStatsLoadedEvent ev){
		if(ev.getManager().getType()!=GameType.TIME)return;
		if(this.getPlayerProgress().containsKey(ev.getPlayerId())){
			if(ev.getManager().getInt(ev.getPlayerId(), StatsKey.SKY_TIME) > TimeSpan.DAY){
				this.getPlayerProgress().put(ev.getPlayerId(), getProgress(ev.getPlayerId())+1);
				done(ev.getPlayerId());
			}
		}
	}
}

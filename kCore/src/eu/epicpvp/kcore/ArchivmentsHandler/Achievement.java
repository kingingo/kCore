package eu.epicpvp.kcore.ArchivmentsHandler;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;

@Getter
public class Achievement implements Listener{

	private String name;
	private String[] description;
	private int profit=0;
	private int progress;
	private StatsKey key;
	private HashMap<Integer,Integer> playerProgress;
	private AchievementsHandler handler;
	
	public Achievement(String name,String[] description, int profit, int progress){
		this(name,description,profit,progress,StatsKey.MONEY);
	}
	
	public Achievement(String name,String[] description,int profit, int progress, StatsKey key){
		this.name=name;
		this.description=description;
		this.profit=profit;
		this.progress=progress;
		this.key=key;
		this.playerProgress=new HashMap<>();
	}
	
	public void register(AchievementsHandler handler){
		this.handler=handler;
		Bukkit.getPluginManager().registerEvents(this, handler.getInstance());
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev){
		if(this.playerProgress.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))){
			this.handler.saveProgress(this, ev.getPlayer());
			this.playerProgress.remove(UtilPlayer.getPlayerId(ev.getPlayer()));
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		int pprogress = this.handler.getProgress(this, ev.getPlayer());
		if(pprogress < progress){
			this.playerProgress.put(UtilPlayer.getPlayerId(ev.getPlayer()), pprogress);
		}
	}
	
}

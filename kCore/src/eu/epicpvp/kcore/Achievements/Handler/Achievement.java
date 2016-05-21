package eu.epicpvp.kcore.Achievements.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Achievements.Events.PlayerLoadAchievementsEvent;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Util.Title;
import eu.epicpvp.kcore.Util.UtilFirework;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilString;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

@Getter
public class Achievement implements Listener{

	private boolean secret;
	private String name;
	private List<String> description;
	private int profit=0;
	protected int maxprogress;
	private StatsKey key;
	private GameType type;
	private HashMap<Integer,Integer> playerProgress;
	private AchievementsHandler handler;
	
	public Achievement(String name,List<String> description, int profit, int progress){
		this(name,description,profit,progress,false);
	}
	
	public Achievement(String name,List<String> description, int profit, int progress,boolean secret){
		this(name,description,profit,progress,StatsKey.MONEY,GameType.SKYBLOCK,secret);
	}
	
	public Achievement(String name,List<String> description,int profit, int maxprogress, StatsKey key,GameType type,boolean secret){
		this.name=name;
		this.description=description;
		this.profit=profit;
		this.maxprogress=maxprogress;
		this.key=key;
		this.type=type;
		this.playerProgress=new HashMap<>();
		this.secret=secret;
	}
	
	public ArrayList<String> getDescription(Player player){
		ArrayList<String> list = new ArrayList<>(getDescription());
		int progress = getProgress(player);
		
		list.add("§6Fortschritt: "+(progress==maxprogress ? "§a" : "§c")+progress+"§7/§a"+maxprogress);
		return list;
	}
	
	public boolean done(Player player){
		return done(UtilPlayer.getPlayerId(player));
	}
	
	public boolean done(int playerId){
		if(this.playerProgress.containsKey(playerId)){
			if(getProgress(playerId) >= maxprogress){
				saveProgress(playerId);
				this.playerProgress.remove(playerId);
				StatsManagerRepository.getStatsManager(getType()).add(playerId, getKey(), getProfit());
				
				Player player = UtilPlayer.searchExact(playerId);
				if(player!=null){
					Bukkit.getScheduler().runTask(handler.getInstance(), new Runnable() {
						@Override
						public void run() {
							Title title = new Title("§6§lErfolgreich Abgeschlossen", ""+getName());
							title.send(player);
							UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), Type.BALL_LARGE);
							UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), Type.BALL_LARGE);
							UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), Type.BALL_LARGE);
							
							player.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
							player.sendMessage(" ");
							player.sendMessage(UtilString.center("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬".length(),"Du hast das Achievement erfolgreich abgeschlossen!".length())+"§aDu hast das Achievement erfolgreich abgeschlossen!");
							player.sendMessage(UtilString.center("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬".length(),("Du hast "+profit+" Epics erhalten.").length())+"§aDu hast §e"+profit+"§a Epics erhalten.");
							player.sendMessage(" ");
							player.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
						}
					});
				}
				
				return true;
			}
		}
		return false;
	}
	
	public Integer getProgress(Player player){
		return getProgress(UtilPlayer.getPlayerId(player));
	}
	
	public Integer getProgress(int playerId){
		if(this.playerProgress.containsKey(playerId))return this.playerProgress.get(playerId);
		
		if(this.handler.isConfig()){
			kConfig config = UtilServer.getUserData().getConfig(Integer.valueOf(playerId));
			if(config.isSet("Achievements."+ getClass().getName() +".Progress")){
				return config.getInt("Achievements."+ getClass().getName() +".Progress");
			}
		}else{
			
		}
		return 0;
	}
	
	public void saveProgress(Player player){
		saveProgress(UtilPlayer.getPlayerId(player));
	}
	
	public void saveProgress(int playerId){
		if(this.handler.isConfig()){
			kConfig config = UtilServer.getUserData().getConfig(Integer.valueOf(playerId));
			config.set("Achievements."+ getClass().getName() +".Progress", getPlayerProgress().get(Integer.valueOf(playerId)));
			config.save();
		}else{
			
		}
	}
	
	public void register(AchievementsHandler handler){
		this.handler=handler;
		Bukkit.getPluginManager().registerEvents(this, handler.getInstance());
		handler.getAchievements().add(this);
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev){
		if(this.playerProgress.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))){
			saveProgress(ev.getPlayer());
			this.playerProgress.remove(UtilPlayer.getPlayerId(ev.getPlayer()));
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		int pprogress = getProgress(ev.getPlayer());
		if(pprogress < maxprogress){
			this.playerProgress.put(UtilPlayer.getPlayerId(ev.getPlayer()), pprogress);
			Bukkit.getPluginManager().callEvent(new PlayerLoadAchievementsEvent(ev.getPlayer(), this));
		}
	}
	
}

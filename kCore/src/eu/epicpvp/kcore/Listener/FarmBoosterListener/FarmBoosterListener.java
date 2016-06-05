package eu.epicpvp.kcore.Listener.FarmBoosterListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import dev.wolveringer.booster.BoosterType;
import dev.wolveringer.booster.NetworkBooster;
import dev.wolveringer.client.Callback;
import dev.wolveringer.event.EventListener;
import dev.wolveringer.events.Event;
import dev.wolveringer.events.EventConditions;
import dev.wolveringer.events.EventType;
import dev.wolveringer.events.booster.BoosterStatusChangeEvent;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;

public class FarmBoosterListener extends kListener{
	
	private NetworkBooster booster;
	public int cactusModifier;
	public int caneModifier;
	public int melonModifier;
	public int mushroomModifier;
	public int pumpkinModifier;
	public int saplingModifier;
	public int wheatModifier;
	public int wartModifier;

	public FarmBoosterListener(JavaPlugin instance) {
		super(instance, "FarmBoosterListener");
		this.cactusModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.cactusModifier;
		this.caneModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.caneModifier;
		this.melonModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.melonModifier;
		this.mushroomModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.mushroomModifier;
		this.pumpkinModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.pumpkinModifier;
		this.saplingModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.saplingModifier;
		this.wheatModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.wheatModifier;
		this.wartModifier=((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.wartModifier;
		
		call(BoosterType.SKY);
		
		UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.BOOSTER_SWITCH).setConditionEnables(EventConditions.BOOSTER_TYPE, true);
		UtilServer.getClient().getHandle().getEventManager().registerListener(new EventListener() {
			
			@Override
			public void fireEvent(Event e) {
				if(e instanceof BoosterStatusChangeEvent){
					BoosterStatusChangeEvent ev = (BoosterStatusChangeEvent)e;
					
					if(ev.getBoosterType() == BoosterType.SKY){
						call(ev.getBoosterType());
					}
				}
			}
		});
	}
	
	public String timeCalculator(){
		return UtilTime.formatMili( ((booster.getStart()+booster.getTime())-System.currentTimeMillis()) );
	}
	
	public void updateTime(){
		if(booster==null)return;
		for(Player player : UtilServer.getPlayers()){
			UtilScoreboard.resetScore(player.getScoreboard(), 10, DisplaySlot.SIDEBAR);
			UtilScoreboard.setScore(player.getScoreboard(),"§eZeit"+Zeichen.DOUBLE_ARROWS_R.getIcon()+"§c  "+timeCalculator(), DisplaySlot.SIDEBAR, 10);
		}
	}
	
	public void call(BoosterType type){
		UtilServer.getClient().getNetworkBooster(type).getAsync(new Callback<NetworkBooster>() {
			
			@Override
			public void call(NetworkBooster b, Throwable exception) {
				booster=b;
				
				if(b!=null && b.isActive()){
					setModifer(2);
				}else{
					setModifer(1);
				}
			}
		});
	}
	

	@EventHandler
	public void join(PlayerSetScoreboardEvent ev){
		if(booster!=null)
			UtilScoreboard.setScore(ev.getPlayer().getScoreboard(),"§eFarm Booster by §C"+UtilServer.getClient().getPlayerAndLoad(booster.getPlayer()).getName(), DisplaySlot.SIDEBAR, 11);
	}
	
	@EventHandler
	public void async(UpdateAsyncEvent ev){
		if(ev.getType()==UpdateAsyncType.FAST){
			if(booster != null){
				if(!booster.isActive()){
					setModifer(1);
					booster=null;
				}
			}
		}
	}
	
	public void setModifer(int i){
		if(booster!=null){
			if(i==1){
				for(Player player : UtilServer.getPlayers()){
					UtilScoreboard.resetScore(player.getScoreboard(), 11, DisplaySlot.SIDEBAR);
					UtilScoreboard.resetScore(player.getScoreboard(), 10, DisplaySlot.SIDEBAR);
				}
			}else{
				for(Player player : UtilServer.getPlayers()){
					UtilScoreboard.setScore(player.getScoreboard(),"§eFarm Booster by §C"+UtilServer.getClient().getPlayerAndLoad(booster.getPlayer()).getName(), DisplaySlot.SIDEBAR, 11);
				}
			}
		}
		
		logMessage("Set the Growth Modifier to "+i);
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.cactusModifier = cactusModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.caneModifier = caneModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.melonModifier = melonModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.mushroomModifier = mushroomModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.pumpkinModifier = pumpkinModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.saplingModifier = saplingModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.wheatModifier = wheatModifier*i;
		((net.minecraft.server.v1_8_R3.World)Bukkit.getWorld("world")).spigotConfig.wartModifier = wartModifier*i;
	}
}

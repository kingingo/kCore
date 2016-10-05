package eu.epicpvp.kcore.Listener.FarmBoosterListener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.datenclient.event.EventListener;
import eu.epicpvp.datenserver.definitions.booster.BoosterType;
import eu.epicpvp.datenserver.definitions.booster.NetworkBooster;
import eu.epicpvp.datenserver.definitions.events.Event;
import eu.epicpvp.datenserver.definitions.events.EventConditions;
import eu.epicpvp.datenserver.definitions.events.EventType;
import eu.epicpvp.datenserver.definitions.events.booster.BoosterStatusChangeEvent;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;

public class FarmBoosterListener extends kListener{

	private NetworkBooster booster;
	private ArrayList<FarmWorld> worlds;

	public FarmBoosterListener(JavaPlugin instance) {
		super(instance, "FarmBoosterListener");
		this.worlds=new ArrayList<>();

		for(World world : Bukkit.getWorlds())this.worlds.add(new FarmWorld(world));

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
	public void load(WorldLoadEvent ev){
		this.worlds.add(new FarmWorld(ev.getWorld()));
	}

//	@EventHandler
//	public void join(PlayerSetScoreboardEvent ev){
//		if(booster!=null){
//			UtilScoreboard.setScore(ev.getPlayer().getScoreboard(),"§eFarm Booster by §C"+UtilServer.getClient().getPlayerAndLoad(booster.getPlayer()).getName(), DisplaySlot.SIDEBAR, 11);
//			UtilScoreboard.setScore(ev.getPlayer().getScoreboard(),"      ", DisplaySlot.SIDEBAR, 10);
//		}
//	}

	@EventHandler
	public void async(UpdateAsyncEvent ev){
		if(ev.getType()==UpdateAsyncType.SEC_4){
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
			for(FarmWorld fw : worlds)fw.setModifer(i);
			logMessage("Set the Growth Modifier to "+i);

//			if(i==1){
//				for(Player player : UtilServer.getPlayers()){
//					UtilScoreboard.resetScore(player.getScoreboard(), 11, DisplaySlot.SIDEBAR);
//					UtilScoreboard.resetScore(player.getScoreboard(), 10, DisplaySlot.SIDEBAR);
//				}
//			}else{
//				for(Player player : UtilServer.getPlayers()){
//					UtilScoreboard.setScore(player.getScoreboard(),"§eFarm Booster by §C"+UtilServer.getClient().getPlayerAndLoad(booster.getPlayer()).getName(), DisplaySlot.SIDEBAR, 11);
//					UtilScoreboard.setScore(player.getScoreboard(),"      ", DisplaySlot.SIDEBAR, 10);
//				}
//			}
		}
	}
}

package eu.epicpvp.kcore.GagdetShop;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.GagdetShop.Gagdet.Gadget;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class GadgetHandler extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private ArrayList<Gadget> gadgets;
	@Getter
	private StatsManager statsManager;
	
	public GadgetHandler(JavaPlugin instance) {
		super(instance,"GadgetHandler");
		this.instance=instance;
		this.gadgets=new ArrayList<>();
		this.statsManager = StatsManagerRepository.getStatsManager(GameType.PROPERTIES);
		this.statsManager.setAutoLoad(true);
		this.statsManager.setForceSave(true);
		if(UtilTime.getTimeManager()==null)UtilTime.setTimeManager(UtilServer.getPermissionManager());
		UtilServer.getCommandHandler().register(CommandGiveGadget.class, new CommandGiveGadget(this));
		UtilServer.setGadgetHandler(this);
	}
	
	public Gadget getGadget(String gadget){
		for(Gadget g : gadgets){
			if(g.getName().equalsIgnoreCase(gadget))return g;
		}
		return null;
	}
	
	public boolean removeGadget(Gadget gadget){
		if(this.gadgets.contains(gadget)){
			this.gadgets.remove(gadget);
			return true;
		}
		return false;
	}
	
	public void addGadget(Gadget gadget){
		this.gadgets.add(gadget);
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent ev){
		for(Gadget gadget : gadgets){
			if(gadget.getActive_player().containsKey(ev.getPlayer())){
				gadget.removePlayer(ev.getPlayer());
				break;
			}
		}
	}
}

package eu.epicpvp.kcore.Listener.FlyListener;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.Lists;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Particle.ParticleDisplayer;
import eu.epicpvp.kcore.ParticleManager.Particle.Particle;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilWorldGuard;

public class FlyListener extends kListener{
	
	private ArrayList<Player> list = Lists.newArrayList();
	
	public FlyListener() {
		super(UtilServer.getPluginInstance(),"FlyListener");
	}

	@EventHandler
	public void move(PlayerMoveEvent ev){
		if(list.contains(ev.getPlayer())){
			if(UtilWorldGuard.RegionFlag(ev.getPlayer(), DefaultFlag.PVP)){
				ev.getPlayer().setAllowFlight(false);
				ev.getPlayer().setFlying(false);
				list.remove(ev.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void update(UpdateEvent ev){
		if(ev.getType()==UpdateType.FAST){
			for(Player player : list)UtilParticle.CLOUD.display((float)0.2,(float)0.2,(float)0.2,1, 30, player.getLocation().add(0, -2, 0), 25);
		}
	}
	
	@EventHandler
	public void fly(PlayerToggleFlightEvent ev){
		if(ev.getPlayer().getGameMode()!=GameMode.CREATIVE){
			if(!UtilWorldGuard.RegionFlag(ev.getPlayer(), DefaultFlag.PVP)){
				if(!ev.getPlayer().hasMetadata("flylistener") && !list.contains(ev.getPlayer())){
					ev.getPlayer().setMetadata("flylistener", new FixedMetadataValue(UtilServer.getPluginInstance(), 1));
				}else{
					ev.getPlayer().removeMetadata("flylistener", UtilServer.getPluginInstance());
					
					if(ev.getPlayer().isFlying()){
						ev.getPlayer().setAllowFlight(false);
						ev.getPlayer().setFlying(false);
						list.remove(ev.getPlayer());
					}else{
						ev.getPlayer().setAllowFlight(true);
						ev.getPlayer().setFlying(true);
						list.add(ev.getPlayer());
					}
				}
			}
		}
	}
}

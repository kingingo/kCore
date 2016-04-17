package eu.epicpvp.kcore.Addons;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.Commands.Events.PlayerFlyFinalEvent;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerFlyFirstEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilServer;

public class AddonDoubleJump extends kListener{

	public AddonDoubleJump(JavaPlugin instance) {
		super(instance, "AddonDoubleJump");
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		ev.getPlayer().setAllowFlight(true);
		ev.getPlayer().setLevel(0);
	}
	
	@EventHandler
	public void firstfly(PlayerFlyFirstEvent ev){
		if(ev.getPlayer().getLevel()!=5){
			ev.setAllowFlight(false);
		}
	}
	
	@EventHandler
	public void finalfly(PlayerFlyFinalEvent ev){
		if(ev.isToggle()){
			ev.getPlayer().setLevel(5);
		}else{
			ev.getPlayer().setLevel(0);
			ev.setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void move(UpdateEvent ev){
		if(ev.getType()==UpdateType.FAST){
			for(Player player : UtilServer.getPlayers()){
				if(player.getGameMode()!=GameMode.CREATIVE&&player.isOnGround()){
					if(player.getLevel()!=0 && player.getLevel()<5){
						player.setAllowFlight(true);
						player.setLevel(0);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void Double(PlayerToggleFlightEvent ev){
		if(ev.getPlayer().getGameMode()!=GameMode.CREATIVE){
			if(ev.getPlayer().getLevel() < (5-1)){
				ev.setCancelled(true);
				ev.getPlayer().setLevel(ev.getPlayer().getLevel()+1);
				if(ev.getPlayer().getLevel()==(5-1))ev.getPlayer().setAllowFlight(false);
				ev.getPlayer().setFlying(false);
			    ev.getPlayer().setVelocity(ev.getPlayer().getLocation().getDirection().multiply(2D).setY(1.2));
			}
		}
	}

}

package me.kingingo.kcore.Addons;

import me.kingingo.kcore.Command.Commands.Events.PlayerFlyFinalEvent;
import me.kingingo.kcore.Command.Commands.Events.PlayerFlyFirstEvent;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
		if(ev.getPlayer().getLevel()!=3){
			ev.setAllowFlight(false);
		}
	}
	
	@EventHandler
	public void finalfly(PlayerFlyFinalEvent ev){
		if(ev.isToggle()){
			ev.getPlayer().setLevel(3);
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
					if(player.getLevel()==1||player.getLevel()==2){
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
			if(ev.getPlayer().getLevel()==0||ev.getPlayer().getLevel()==1){
				ev.setCancelled(true);
				ev.getPlayer().setLevel(ev.getPlayer().getLevel()+1);
				if(ev.getPlayer().getLevel()==2)ev.getPlayer().setAllowFlight(false);
				ev.getPlayer().setFlying(false);
			    ev.getPlayer().setVelocity(ev.getPlayer().getLocation().getDirection().multiply(2D).setY(1.2));
			}
		}
	}

}
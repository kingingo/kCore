package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.PlayerFlyFinalEvent;
import me.kingingo.kcore.Command.Commands.Events.PlayerFlyFirstEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandFly extends kListener implements CommandExecutor{
	
	public CommandFly(JavaPlugin instance){
		super(instance,"CommandFly");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "fly",alias={"kfly"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.kFLY.getPermissionToString())){
			PlayerFlyFirstEvent ev = new PlayerFlyFirstEvent(player);
			Bukkit.getPluginManager().callEvent(ev);
			
			if(ev.isAllowFlight()){
				PlayerFlyFinalEvent e = new PlayerFlyFinalEvent(player,false);
				Bukkit.getPluginManager().callEvent(e);
				player.setAllowFlight(e.isAllowFlight());
				player.setFlying(false);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "kFLY_OFF"));
			}else{
				PlayerFlyFinalEvent e = new PlayerFlyFinalEvent(player,true);
				Bukkit.getPluginManager().callEvent(e);
				player.setAllowFlight(e.isAllowFlight());
				player.setFlying(true);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "kFLY_ON"));
			}
		}
		return false;
	}
	
	@EventHandler
	public void AntiLogout(AntiLogoutAddPlayerEvent ev){
		if(ev.getPlayer().getAllowFlight()&&!ev.getPlayer().hasPermission(kPermission.ALL_PERMISSION.getPermissionToString())){
			ev.getPlayer().setAllowFlight(false);
			ev.getPlayer().setFlying(false);
		}
	}

}

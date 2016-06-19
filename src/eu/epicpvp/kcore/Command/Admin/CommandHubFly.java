package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerFlyFinalEvent;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerFlyFirstEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandHubFly extends kListener implements CommandExecutor{
	
	public CommandHubFly(JavaPlugin instance){
		super(instance,"CommandFly");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "fly",alias={"kfly"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(PermissionType.FLY_LOBBY.getPermissionToString())||player.hasPermission(PermissionType.kFLY.getPermissionToString())){
			PlayerFlyFirstEvent ev = new PlayerFlyFirstEvent(player);
			Bukkit.getPluginManager().callEvent(ev);
			
			if(ev.isAllowFlight()){
				PlayerFlyFinalEvent e = new PlayerFlyFinalEvent(player,false);
				Bukkit.getPluginManager().callEvent(e);
				player.setAllowFlight(e.isAllowFlight());
				player.setFlying(false);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "kFLY_OFF"));
			}else{
				PlayerFlyFinalEvent e = new PlayerFlyFinalEvent(player,true);
				Bukkit.getPluginManager().callEvent(e);
				player.setAllowFlight(e.isAllowFlight());
				player.setFlying(true);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "kFLY_ON"));
			}
		}
		return false;
	}
	
	@EventHandler
	public void AntiLogout(AntiLogoutAddPlayerEvent ev){
		if(ev.getPlayer().getAllowFlight()&&!ev.getPlayer().hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
			ev.getPlayer().setAllowFlight(false);
			ev.getPlayer().setFlying(false);
		}
	}

}

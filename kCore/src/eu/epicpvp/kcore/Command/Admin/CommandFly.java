package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandFly extends kListener implements CommandExecutor{
	
	public CommandFly(JavaPlugin instance){
		super(instance,"CommandFly");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "fly",alias={"kfly"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(PermissionType.kFLY.getPermissionToString())){
			if(player.getAllowFlight()){
				player.setAllowFlight(false);
				player.setFlying(false);
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "kFLY_OFF"));
			}else{
				player.setAllowFlight(true);
				player.setFlying(true);
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "kFLY_ON"));
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

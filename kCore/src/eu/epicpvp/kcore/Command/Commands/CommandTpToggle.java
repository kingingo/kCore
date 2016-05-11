package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class CommandTpToggle implements CommandExecutor{
	
	@Getter
	private TeleportManager manager;
	
	public CommandTpToggle(TeleportManager manager){
		this.manager=manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tptoggle", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player player = (Player)cs;
		if(getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_TOGGLE)){
			kConfig config = UtilServer.getUserData().getConfig(player);
				
			if(config.getBoolean("TpToggle")){
				config.set("TpToggle", "false");
				player.sendMessage(TranslationHandler.getPrefixAndText(player, "TP_TOGGLE_OFF"));
			}else{
				config.set("TpToggle", "true");
				player.sendMessage(TranslationHandler.getPrefixAndText(player, "TP_TOGGLE_ON"));
			}
		}
		return false;
	}
	
}
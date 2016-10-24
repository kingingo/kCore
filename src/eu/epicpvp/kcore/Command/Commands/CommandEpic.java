package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.AllArgsConstructor;

public class CommandEpic implements CommandExecutor {
	
	@Override
	@CommandHandler.Command(command = "epic", sender = CommandHandler.Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player plr = (Player) sender;
		if (UtilServer.getPermissionManager().hasPermission(plr, PermissionType.EPIC)) {
			kConfig config = UtilServer.getUserData().getConfig(plr);
			
			long last = config.getLong("epic");
			
			if( UtilTime.getStartOfDay().getTime() < last && UtilTime.getEndOfDay().getTime() > last ){
				plr.sendMessage(TranslationHandler.getText(plr, "PREFIX")+"§cDu kannst dir nur einmal am Tag einen Key abholen!");
			}else{
				config.set("epic", System.currentTimeMillis());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey "+plr.getName()+" legendary 1");
			}
		}
		return false;
	}
}

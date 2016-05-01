package eu.epicpvp.kcore.Command.Commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandNear implements CommandExecutor{
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "near",alias={"nah"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission(PermissionType.NEAR.getPermissionToString())){
			String time = UtilTime.getTimeManager().check("CMD:near", player);
			if(time !=null){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME", time));
				return false;
			}else{
				UtilTime.getTimeManager().add("CMD:near", player, TimeSpan.SECOND*30);
			}

			Map<Double, Player> list = UtilPlayer.getNearby(player.getLocation(), 15.0, PermissionType.NEAR_IGNORE);

			if(!list.isEmpty()){
				for(Map.Entry<Double, Player> entry : list.entrySet()){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NEAR_FIND",new String[]{entry.getValue().getName(), String.valueOf(entry.getKey())}));
				}
				list.clear();
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NEAR_EMPTY"));
			}
			return true;
		}
		return false;
	}
}

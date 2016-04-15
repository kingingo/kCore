package eu.epicpvp.kcore.Command.Commands;

import java.util.TreeMap;

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

	private Player player;
	private TreeMap<Double,Player> list;
	private String time;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "near",alias={"nah"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(player.hasPermission(PermissionType.NEAR.getPermissionToString())){
			time=UtilTime.getTimeManager().check("CMD:near", player);
			if(time!=null){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME",time));
				return false;
			}else{
				UtilTime.getTimeManager().add("CMD:near", player, TimeSpan.SECOND*30);
			}
			
			
			list = UtilPlayer.getNearby(player.getLocation(), 15.0, PermissionType.NEAR_IGNORE);
			
			if(!list.isEmpty()){
				for(double dis : list.keySet()){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NEAR_FIND",new String[]{list.get(dis).getName(),String.valueOf(dis)}));
				}
				list.clear();
				list=null;
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NEAR_EMPTY"));
			}
			return true;
		}
		return false;
	}
}

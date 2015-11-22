package me.kingingo.kcore.Command.Commands;

import java.util.TreeMap;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TimeManager.TimeManager;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNear implements CommandExecutor{

	private Player player;
	private TreeMap<Double,Player> list;
	private String time;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "near",alias={"nah"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(player.hasPermission(kPermission.NEAR.getPermissionToString())){
			time=UtilTime.getTimeManager().check("CMD:near", player);
			if(time!=null){
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "USE_BEFEHL_TIME",time));
				return false;
			}else{
				UtilTime.getTimeManager().add("CMD:near", player, TimeSpan.SECOND*30);
			}
			
			
			list = UtilPlayer.getNearby(player.getLocation(), 15.0, kPermission.NEAR_IGNORE);
			
			if(!list.isEmpty()){
				for(double dis : list.keySet()){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NEAR_FIND",new String[]{list.get(dis).getName(),String.valueOf(dis)}));
				}
				list.clear();
				list=null;
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NEAR_EMPTY"));
			}
			return true;
		}
		return false;
	}
}

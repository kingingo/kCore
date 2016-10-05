package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandHeal implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "heal", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission(PermissionType.HEAL.getPermissionToString())){
			if(args.length==0){
				String s = UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s !=null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME", s));
				}else{
					player.setHealth(player.getMaxHealth());
					player.setFoodLevel(20);
					player.setSaturation(20);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAL"));
					Long l = UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l !=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
				}
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(PermissionType.HEAL_ALL.getPermissionToString())){
						for(Player p : UtilServer.getPlayers()){
							p.setHealth(p.getMaxHealth());
							p.setFoodLevel(20);
							p.setSaturation(20);
							p.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAL_ALL", player.getName()));
						}
					}
				}else{
					if(player.hasPermission(PermissionType.HEAL_OTHER.getPermissionToString())){
						Player target = Bukkit.getPlayer(args[0]);
						if(target !=null){
							target.setHealth(target.getMaxHealth());
							target.setFoodLevel(20);
							target.setSaturation(20);
							target.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAL_ALL", player.getName()));
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAL_OTHER",args[0]));
						}else{
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "PLAYER_IS_OFFLINE",args[0]));
						}
					}
				}
			}
		}
		return false;
	}

}

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

public class CommandFeed implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "feed",alias={"eat","essen","f§ttern"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(PermissionType.FEED.getPermissionToString())){
			if(args.length==0){
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME",s));
				}else{
					player.setFoodLevel(20);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "FEED"));
					l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l!=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
				}
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(PermissionType.FEED_ALL.getPermissionToString())){
						for(Player p : UtilServer.getPlayers()){
							p.setFoodLevel(20);
							p.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "FEED_ALL",player.getName()));
						}
					}
				}else{
					if(player.hasPermission(PermissionType.FEED_OTHER.getPermissionToString())){
						if(Bukkit.getPlayer(args[0])!=null){
							Bukkit.getPlayer(args[0]).setFoodLevel(20);
							Bukkit.getPlayer(args[0]).sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "FEED_ALL",player.getName()));
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "FEED_OTHER",args[0]));
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

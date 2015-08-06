package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "feed",alias={"eat","essen","füttern"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.FEED.getPermissionToString())){
			if(args.length==0){
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "USE_BEFEHL_TIME",s));
				}else{
					player.setFoodLevel(20);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "FEED"));
					l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l!=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
				}
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(kPermission.FEED_ALL.getPermissionToString())){
						for(Player p : UtilServer.getPlayers()){
							p.setFoodLevel(20);
							p.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "FEED_ALL",player.getName()));
						}
					}
				}else{
					if(player.hasPermission(kPermission.FEED_OTHER.getPermissionToString())){
						if(Bukkit.getPlayer(args[0])!=null){
							Bukkit.getPlayer(args[0]).setFoodLevel(20);
							Bukkit.getPlayer(args[0]).sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "FEED_ALL",player.getName()));
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "FEED_OTHER",args[0]));
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
						}
					}
				}
			}
		}
		return false;
	}

}

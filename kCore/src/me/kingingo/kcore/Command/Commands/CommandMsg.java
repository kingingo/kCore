package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.PlayerMsgSendEvent;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMsg implements CommandExecutor{
	private Player player;
	private Player target;
	private StringBuilder sb;
	private String msg;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "msg",alias={"tell","m","t","w","wispher","wisphers"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(args.length<=1){
			player.sendMessage(Text.PREFIX.getText()+"/msg [Player] [Text]");
		}else{
			
			if(args[0].equalsIgnoreCase(player.getName()))return false;
			
			if(UtilPlayer.isOnline(args[0])){
				target=Bukkit.getPlayer(args[0]);
				
				sb = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]);
					sb.append(" ");
				}
				sb.setLength(sb.length() - 1);
				msg = sb.toString();
				Bukkit.getPluginManager().callEvent(new PlayerMsgSendEvent(player, target, msg,true));
				target.sendMessage(Text.PREFIX.getText()+player.getName()+"->mir: §b"+msg);
				player.sendMessage(Text.PREFIX.getText()+"mir->"+target.getName()+": §b"+msg);
			}else{
				player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
			}
		}
		
		
		return false;
	}

	
	
}

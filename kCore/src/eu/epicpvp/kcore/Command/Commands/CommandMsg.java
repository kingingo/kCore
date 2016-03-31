package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerMsgSendEvent;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandMsg implements CommandExecutor{
	private Player player;
	private Player target;
	private StringBuilder sb;
	private String msg;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "msg",alias={"tell","m","t","w","wispher","wisphers"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(args.length<=1){
			player.sendMessage(Language.getText(player, "PREFIX")+"/msg [Player] [Text]");
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
				target.sendMessage(Language.getText(target, "PREFIX")+player.getName()+"->"+Language.getText(target, "ME")+": §b"+msg);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "ME")+"->"+target.getName()+": §b"+msg);
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
			}
		}
		
		
		return false;
	}

	
	
}

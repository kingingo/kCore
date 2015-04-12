package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveAll implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "giveall", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(kPermission.COMMAND_GIVE_ALL.getPermissionToString())){
				for(Player player : UtilServer.getPlayers()){
					if(player==p)continue;
					player.getInventory().addItem(p.getItemInHand());
				}
				UtilServer.broadcast(Text.PREFIX.getText()+Text.GIVEALL.getText(new String[]{p.getName(),String.valueOf(p.getItemInHand().getAmount()), String.valueOf(p.getItemInHand().getTypeId())}));
			}
		}
		return false;
	}
	
}


package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.UtilNumber;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTppos implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "tppos", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player =(Player)sender;
		if(!player.isOp())return false;
		if(args.length<4){
			player.sendMessage(Language.getText(player, "PREFIX")+"/tppos [World] [X] [Y] [Z]");
			player.sendMessage(Language.getText(player, "PREFIX")+" Welt:"+player.getWorld().getName()+" X:"+player.getLocation().getBlockX()+" Y:"+player.getLocation().getBlockY()+" Z:"+player.getLocation().getBlockZ());
		}else{
			if(Bukkit.getWorld(args[0])==null)return false;
			World world = Bukkit.getWorld(args[0]);
			int x = UtilNumber.toInt(args[1]);
			int y = UtilNumber.toInt(args[2]);
			int z = UtilNumber.toInt(args[3]);
			
			player.teleport(new Location(world,x,y,z));
		}
		return false;
	}
	
}

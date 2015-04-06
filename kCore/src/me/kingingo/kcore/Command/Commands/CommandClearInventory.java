package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClearInventory implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "clearinventory",alias={"ci"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.getInventory().clear();
			player.getInventory().setHelmet(null);
			player.getInventory().setChestplate(null);
			player.getInventory().setLeggings(null);
			player.getInventory().setBoots(null);
			player.sendMessage(Text.PREFIX.getText()+Text.CLEARINVENTORY.getText());
		}else{
			if(args[0].equalsIgnoreCase("all")&&player.hasPermission(kPermission.CLEARINVENTORY_ALL.getPermissionToString())){
				for(Player p : UtilServer.getPlayers()){
					if(p.getName().equalsIgnoreCase(player.getName()))continue;
					p.getInventory().clear();
					p.getInventory().setHelmet(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setLeggings(null);
					p.getInventory().setBoots(null);
					p.sendMessage(Text.PREFIX.getText()+Text.CLEARINVENTORY_OTHER.getText(player.getName()));
				}
				player.sendMessage(Text.PREFIX.getText()+Text.CLEARINVENTORY_ALL.getText());
			}else if(player.hasPermission(kPermission.CLEARINVENTORY_OTHER.getPermissionToString())){
				if(UtilPlayer.isOnline(args[0])){
					Bukkit.getPlayer(args[0]).getInventory().clear();
					Bukkit.getPlayer(args[0]).getInventory().setHelmet(null);
					Bukkit.getPlayer(args[0]).getInventory().setChestplate(null);
					Bukkit.getPlayer(args[0]).getInventory().setLeggings(null);
					Bukkit.getPlayer(args[0]).getInventory().setBoots(null);
					player.sendMessage(Text.PREFIX.getText()+Text.CLEARINVENTORY_FROM_OTHER.getText(args[0]));
					player.sendMessage(Text.PREFIX.getText()+Text.CLEARINVENTORY_OTHER.getText(player.getName()));
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
				}
			}
		}
		return false;
	}

}

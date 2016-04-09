package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandClearInventory implements CommandExecutor{
	
	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "clearinventory",alias={"ci"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(PermissionType.CLEARINVENTORY.getPermissionToString())){
			if(args.length==0){
				player.getInventory().clear();
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.getInventory().setBoots(null);
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "CLEARINVENTORY"));
			}else{
				if(args[0].equalsIgnoreCase("all")&&player.hasPermission(PermissionType.CLEARINVENTORY_ALL.getPermissionToString())){
					for(Player p : UtilServer.getPlayers()){
						if(p.getName().equalsIgnoreCase(player.getName()))continue;
						p.getInventory().clear();
						p.getInventory().setHelmet(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setLeggings(null);
						p.getInventory().setBoots(null);
						p.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "CLEARINVENTORY_OTHER",player.getName()));
					}
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "CLEARINVENTORY_ALL"));
				}else if(player.hasPermission(PermissionType.CLEARINVENTORY_OTHER.getPermissionToString())){
					if(UtilPlayer.isOnline(args[0])){
						Bukkit.getPlayer(args[0]).getInventory().clear();
						Bukkit.getPlayer(args[0]).getInventory().setHelmet(null);
						Bukkit.getPlayer(args[0]).getInventory().setChestplate(null);
						Bukkit.getPlayer(args[0]).getInventory().setLeggings(null);
						Bukkit.getPlayer(args[0]).getInventory().setBoots(null);
						player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "CLEARINVENTORY_FROM_OTHER",args[0]));
						player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "CLEARINVENTORY_OTHER",player.getName()));
					}else{
						player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					}
				}
			}
		}
		return false;
	}

}

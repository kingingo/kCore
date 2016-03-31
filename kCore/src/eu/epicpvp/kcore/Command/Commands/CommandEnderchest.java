package eu.epicpvp.kcore.Command.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandEnderchest extends kListener implements CommandExecutor{

	private Player player;
	private Player target;
	private MySQL mysql;
	
	public CommandEnderchest(MySQL mysql) {
		super(mysql.getInstance(), "CommandEnderchest");
		this.mysql=mysql;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "enderchest", alias = {"endersee","echest","esee"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(PermissionType.ENDERCHEST_ADDON.getPermissionToString())&&!player.hasPermission(PermissionType.ENDERCHEST_USE.getPermissionToString()))return false;
		
		if(args.length==0){
			if(player.hasPermission(PermissionType.ENDERCHEST.getPermissionToString())){
				player.openInventory(player.getEnderChest());
			}
		}else{
			if(player.hasPermission(PermissionType.ENDERCHEST_OTHER.getPermissionToString())){
				if(UtilPlayer.isOnline(args[0])){
					target=Bukkit.getPlayer(args[0]);
					player.openInventory(target.getEnderChest());
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					
					if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
						UUID uuid = UtilPlayer.getUUID(args[0], mysql);
						target=null;
						target = UtilPlayer.loadPlayer(uuid);
						
						if(target!=null){
							player.openInventory(target.getEnderChest());
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+"§cnicht gefunden!");
						}
						
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void Click(InventoryClickEvent ev){
		if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
		if(ev.getInventory().getName().equalsIgnoreCase("Ender Chest")){
			if(ev.getInventory()==((Player)ev.getWhoClicked()).getEnderChest())return;
			if(! ((Player)ev.getWhoClicked()).hasPermission(PermissionType.ENDERCHEST_CLICK.getPermissionToString()) )ev.setCancelled(true);
		}
	}

}

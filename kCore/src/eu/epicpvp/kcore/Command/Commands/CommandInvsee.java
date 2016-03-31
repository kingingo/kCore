package eu.epicpvp.kcore.Command.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryPlayer;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandInvsee extends kListener implements CommandExecutor{
	
	private MySQL mysql;
	
	public CommandInvsee(MySQL mysql) {
		super(mysql.getInstance(), "CommandInvsee");
		this.mysql=mysql;
	}

	private Player player;
	private Player target;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "invsee",alias={"bodysee","open","openinv","openbody"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/invsee [Name]");
		}else{
			if(player.hasPermission(PermissionType.INVSEE.getPermissionToString())){
				if(UtilPlayer.isOnline(args[0])){
					target=Bukkit.getPlayer(args[0]);

					if(player.hasPermission(PermissionType.INVSEE_BODY.getPermissionToString())){
						player.openInventory( new InventoryPlayer(target).getInventory() );
					}else{
						player.openInventory(target.getInventory());
					}
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					
					if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
						UUID uuid = UtilPlayer.getUUID(args[0], mysql);
						target=null;
						target = UtilPlayer.loadPlayer(uuid);
						
						if(target!=null){
							if(player.hasPermission(PermissionType.INVSEE_BODY.getPermissionToString())){
								player.openInventory( new InventoryPlayer(target).getInventory() );
							}else{
								player.openInventory(target.getInventory());
							}
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
		if(ev.getInventory() instanceof PlayerInventory){
			if(! ((Player)ev.getWhoClicked()).hasPermission(PermissionType.INVSEE_CLICK.getPermissionToString()) ){
				ev.setCancelled(true);
			}
		}
	}

}

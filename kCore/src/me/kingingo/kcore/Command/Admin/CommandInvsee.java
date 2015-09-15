package me.kingingo.kcore.Command.Admin;

import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandInvsee extends kListener implements CommandExecutor{
	
	private MySQL mysql;
	
	public CommandInvsee(MySQL mysql) {
		super(mysql.getInstance(), "CommandInvsee");
		this.mysql=mysql;
	}

	private Player player;
	private Player target;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "invsee", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/invsee [Name]");
		}else{
			if(player.hasPermission(kPermission.INVSEE.getPermissionToString())){
				if(UtilPlayer.isOnline(args[0])){
					target=Bukkit.getPlayer(args[0]);
					player.openInventory(target.getInventory());
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					
					if(player.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString())){
						UUID uuid = UtilPlayer.getUUID(args[0], mysql);
						target=null;
						target = UtilPlayer.loadPlayer(uuid);
						
						if(target!=null){
							player.openInventory(target.getInventory());
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
			if(! ((Player)ev.getWhoClicked()).hasPermission(kPermission.INVSEE_CLICK.getPermissionToString()) )ev.setCancelled(true);
		}
	}

}

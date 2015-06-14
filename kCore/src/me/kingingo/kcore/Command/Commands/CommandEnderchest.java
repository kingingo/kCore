package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
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
import org.bukkit.material.EnderChest;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandEnderchest extends kListener implements CommandExecutor{

	private Player player;
	private Player target;
	
	public CommandEnderchest(JavaPlugin plugin) {
		super(plugin, "CommandEnderchest");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "enderchest", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(!player.hasPermission(kPermission.ENDERCHEST_USE.getPermissionToString()))return false;
		
		if(args.length==0){
			if(player.hasPermission(kPermission.ENDERCHEST.getPermissionToString())){
				player.openInventory(player.getEnderChest());
			}
		}else{
			if(player.hasPermission(kPermission.ENDERCHEST_OTHER.getPermissionToString())){
				if(UtilPlayer.isOnline(args[0])){
					target=Bukkit.getPlayer(args[0]);
					player.openInventory(target.getEnderChest());
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
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
			if(! ((Player)ev.getWhoClicked()).hasPermission(kPermission.ENDERCHEST_CLICK.getPermissionToString()) )ev.setCancelled(true);
		}
	}

}

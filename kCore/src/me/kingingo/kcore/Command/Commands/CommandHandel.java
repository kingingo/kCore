package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryTrade;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandel extends kListener implements CommandExecutor{

	private Player player;
	private Player player1;
	private InventoryBase base;
	
	public CommandHandel(JavaPlugin instance) {
		super(instance, "CommandHandel");
		this.base=new InventoryBase(instance, "");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "handel",alias={"trade"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/handel [Player]");
		}else{
			if(UtilPlayer.isOnline(args[0])){
				player1=Bukkit.getPlayer(args[0]);
				this.base.addAnother(new InventoryTrade(player, player1));
			}
		}
		return false;
	}
	
	Player p;
	InventoryPageBase page;
	@EventHandler
	public void UseInv(InventoryClickEvent ev){
		if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
			page=base.get(ev.getInventory());
			if(page!=null){
				ev.setCancelled(true);
				p=(Player)ev.getWhoClicked();
				page.getButton(44).setItemStack(UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept"));
				page.getButton(35).setItemStack(UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept"));
			}
	}

}

package me.kingingo.kcore.Command.Commands;

import java.util.HashMap;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryTrade;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandel extends kListener implements CommandExecutor{

	private InventoryBase base;
	private HashMap<Player,InventoryTrade> list;
	
	public CommandHandel(JavaPlugin instance) {
		this(instance, new InventoryBase(instance, ""));
	}
	
	public CommandHandel(JavaPlugin instance,InventoryBase base) {
		super(instance,"CommandHandel");
		this.base=base;
		this.list=new HashMap<>();
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "handel",alias={"trade"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/handel [Player]");
		}else{
			if(UtilPlayer.isOnline(args[0])){
				Player player1=Bukkit.getPlayer(args[0]);
				InventoryTrade t = new InventoryTrade(player, player1);
				list.put(player, t);
				list.put(player1, t);
				this.base.addAnother(t);
			}
		}
		return false;
	}

	@EventHandler
	public void close(InventoryCloseEvent ev){
		if(list.containsKey(ev.getPlayer())){
			Player t=list.get(ev.getPlayer()).getT();
			Player t1=list.get(ev.getPlayer()).getT1();
			list.get(ev.getPlayer()).done();
			list.remove(t);
			list.remove(t1);
		}
	}
}

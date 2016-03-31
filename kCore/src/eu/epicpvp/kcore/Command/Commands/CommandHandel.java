package eu.epicpvp.kcore.Command.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryTrade;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandHandel extends kListener implements CommandExecutor{

	private InventoryBase base;
	private HashMap<Player,InventoryTrade> list;
	private HashMap<Player,Player> anfrage;
	
	public CommandHandel(JavaPlugin instance) {
		this(instance, new InventoryBase(instance, ""));
	}
	
	public CommandHandel(JavaPlugin instance,InventoryBase base) {
		super(instance,"CommandHandel");
		this.base=base;
		this.list=new HashMap<>();
		this.anfrage=new HashMap<>();
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "handel",alias={"trade"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/handel [Player]");
		}else{
			if(UtilPlayer.isOnline(args[0])){
				Player player1=Bukkit.getPlayer(args[0]);
				if(player1.getUniqueId()==player.getUniqueId())return false;
				
				if(anfrage.containsKey(player)&&anfrage.get(player).getUniqueId()==player1.getUniqueId()){
					InventoryTrade t = new InventoryTrade(player, player1);
					list.put(player, t);
					list.put(player1, t);
					this.base.addAnother(t);
					anfrage.remove(player);
				}else{
					if(anfrage.containsKey(player1)&&anfrage.get(player1).getUniqueId()==player.getUniqueId()){
						player.sendMessage(Language.getText(player1, "PREFIX")+"§cDu hast diesen Spieler bereits eine Anfrage gesendet!");
						return false;
					}
					
					anfrage.remove(player1);
					anfrage.put(player1, player);
					player.sendMessage(Language.getText(player1, "PREFIX")+"§aDu hast §7"+player1.getName()+"§a eine anfrage gesendet!");
					player1.sendMessage(Language.getText(player1, "PREFIX")+"§aDu hast von §7"+player.getName()+"§a eine Handel anfrage erhalten!");
					player1.sendMessage(Language.getText(player1, "PREFIX")+"§azum Annehmen §7/Handel "+player.getName());
				}
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
			}
		}
		return false;
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev){
		anfrage.remove(ev.getPlayer());
	}
	
	@EventHandler
	public void close(InventoryCloseEvent ev){
		if(list.containsKey(ev.getPlayer())){
			InventoryTrade t = list.get(ev.getPlayer());
			list.remove(t.getT());
			list.remove(t.getT1());
			t.done();
		}
	}
}

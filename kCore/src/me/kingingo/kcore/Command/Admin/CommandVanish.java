package me.kingingo.kcore.Command.Admin;

import java.util.ArrayList;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandVanish extends kListener implements CommandExecutor{
	
	private Player player;
	private ArrayList<Player> invisible = new ArrayList<>();

	public CommandVanish(JavaPlugin instance){
		super(instance,"CommandVanish");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "vanish", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(player.hasPermission(kPermission.VANISH.getPermissionToString())){
			if(args.length==0){
				if(invisible.contains(player)){
					visible(player);
				}else{
					invisible(player);
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		invisible.remove(ev.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Join(PlayerJoinEvent ev){
		for(Player p : invisible)ev.getPlayer().hidePlayer(p);
	}
	
	public boolean visible(Player player){
		if(!invisible.contains(player))return true;
		invisible.remove(player);
		for(Player p : UtilServer.getPlayers()){
			if(p.getName().equalsIgnoreCase(player.getName()))continue;
			p.showPlayer(player);
		}
		return true;
	}
	
	public void invisible(Player player){
		invisible.add(player);
		for(Player p : UtilServer.getPlayers()){
			if(p.getName().equalsIgnoreCase(player.getName()))continue;
			p.hidePlayer(player);
		}
	}
	
}
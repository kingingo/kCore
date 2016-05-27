package eu.epicpvp.kcore.Command.Admin;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandVanish extends kListener implements CommandExecutor{
	
	private Player player;
	private ArrayList<Player> invisible = new ArrayList<>();

	public CommandVanish(JavaPlugin instance){
		super(instance,"CommandVanish");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "vanish", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(player.hasPermission(PermissionType.VANISH.getPermissionToString())){
			if(args.length==0){
				if(invisible.contains(player)){
					visible(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "VANISH_AUS"));
				}else{
					invisible(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "VANISH_AN"));
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		invisible.remove(ev.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Join(PlayerJoinEvent ev){
		for(Player player : UtilServer.getPlayers()){
			player.hidePlayer(ev.getPlayer());
			player.showPlayer(ev.getPlayer());
			ev.getPlayer().showPlayer(player);
			if(invisible.contains(player)){
				ev.getPlayer().hidePlayer(player);
			}
		}
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
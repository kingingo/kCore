package eu.epicpvp.kcore.Command.Admin;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerMsgSendEvent;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;

public class CommandSocialspy extends kListener implements CommandExecutor{

	private Player player;
	private ArrayList<Player> list = new ArrayList<>();
	
	public CommandSocialspy(JavaPlugin instance) {
		super(instance, "CommandSocialspy");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "socialspy", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(args.length==0){
			if(player.hasPermission(PermissionType.SOCIAL_SPY.getPermissionToString())){
				if(list.contains(player)){
					list.remove(player);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SOCIALSPY_OFF"));
				}else{
					list.add(player);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SOCIALSPY_ON"));
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void Msg(PlayerMsgSendEvent ev){
		for(Player player : list)player.sendMessage(Language.getText(player, "PREFIX")+ev.getPlayer().getName()+"->"+ev.getTarget().getName()+":§b "+ev.getMessage());
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(list.contains(ev.getPlayer()))list.remove(ev.getPlayer());
	}

	
	
}

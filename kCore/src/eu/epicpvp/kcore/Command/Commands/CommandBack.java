package eu.epicpvp.kcore.Command.Commands;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;

public class CommandBack extends kListener implements CommandExecutor{

	private Player player;
	private HashMap<Player,Location> last = new HashMap<>();
	
	public CommandBack(JavaPlugin instance) {
		super(instance, "CommandBack");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "back", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(args.length==0&&player.hasPermission(PermissionType.BACK.getPermissionToString())){
			if(last.containsKey(player))player.teleport(last.get(player));
		}
		return false;
	}
	
	@EventHandler
	public void Teleport(PlayerTeleportEvent ev){
		if(last.containsKey(ev.getPlayer()))last.remove(ev.getPlayer());
		last.put(ev.getPlayer(), ev.getFrom());
	}
}

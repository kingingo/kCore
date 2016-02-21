package me.kingingo.kcore.TreasureChest.NEW;

import java.util.Map;
import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.CommandHome;
import me.kingingo.kcore.Command.Commands.Events.PlayerHomeEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class CommandTreasureChest implements CommandExecutor{
	
	private TreasureChestManager treasureChest;
	
	public CommandTreasureChest(TreasureChestManager treasureChest){
		this.treasureChest=treasureChest;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "tc", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.isOp()){
			if(args.length==0){
				
			}else{
				if(args[0].equalsIgnoreCase("saveTemplate")){
					if(args.length==2){
						this.treasureChest.saveTemplate(player, args[1]);
						player.sendMessage(Language.getText(player, "PREFIX")+"§aDein Template §e"+args[1]+"§a wurde gespeichert!");
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/tc saveTemplate [Name]");
					}
				}else if(args[0].equalsIgnoreCase("placeTemplate")){
					if(args.length==2){
						if(this.treasureChest.placeTemplate(player.getLocation(), args[1])){
							player.sendMessage(Language.getText(player, "PREFIX")+"§aDas Template wurde gesetzt!");
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+"§cDas Template wurde nicht gefunden!");
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/tc placeTemplate [Name]");
					}
				}
			}
		}
		return false;
	}

}

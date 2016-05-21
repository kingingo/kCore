package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandFlyToggle implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "flytoggle", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.isOp()){
			if(UtilPlayer.isFly()){
				UtilPlayer.setFly(false);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aFly wurde deaktviert");
			}else{
				UtilPlayer.setFly(true);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cFly wurde aktviert");
				
				for(Player p : UtilServer.getPlayers()){
					if(!p.isOp() && p.isFlying()){
						p.setFlying(false);
						p.setAllowFlight(false);
						p.removePotionEffect(PotionEffectType.JUMP);
					}
				}
			}
		}
		return false;
	}
}

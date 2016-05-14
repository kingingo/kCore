package eu.epicpvp.kcore.GagdetShop;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.GagdetShop.Gagdet.Gadget;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandGiveGadget implements CommandExecutor {

	private GadgetHandler gadgetHandler;

	public CommandGiveGadget(GadgetHandler gadgetHandler) {
		this.gadgetHandler = gadgetHandler;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givegadget", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§a/GiveGadget [Spieler] [Gadget] [Anzahl]");
				}else if(args.length >= 3){
					if(add(args)){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"Der Spieler hat das Gadget "+args[1]+" "+args[2]+" mal bekommen!");
					}else{
						System.out.println("[ClashMC]: FEHLER");
					}
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[ClashMC]: /GiveGadget [Spieler] [Gadget] [Anzahl]");
			}else if(args.length >= 2){
				if(add(args)){
					System.out.println("[ClashMC]: Der Spieler hat das Gadget "+args[1]+" "+args[2]+" mal bekommen!");
				}else{
					System.out.println("[ClashMC]: FEHLER");
				}
			}
		}
		return false;
	}
	
	public boolean add(String[] args){
		Gadget gadget = gadgetHandler.getGadget(args[1]);
		if(gadget!=null){
			int c=UtilInteger.isNumber(args[2]);
			if(c!=-1){
				String spieler = args[0];

				if(UtilPlayer.isOnline(spieler)){
					gadget.setAmount(Bukkit.getPlayer(spieler), c);
					return true;
				}
			}
		}
		
		return false;
	}
}

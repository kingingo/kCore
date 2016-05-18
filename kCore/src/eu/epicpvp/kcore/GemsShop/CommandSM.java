package eu.epicpvp.kcore.GemsShop;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilString;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandSM implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "sm", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = Bukkit.getPlayer(args[0]);
		
		if(args[1].equalsIgnoreCase("GEMS1K")){
			sendMessage(1554764, player);
		}else if(args[1].equalsIgnoreCase("GEMS2K")){
			sendMessage(1554765, player);
		}else if(args[1].equalsIgnoreCase("GEMS5K")){
			sendMessage(1554767, player);
		}else if(args[1].equalsIgnoreCase("GEMS12K")){
			sendMessage(1554769, player);
		}else if(args[1].equalsIgnoreCase("GEMS25K")){
			sendMessage(1554770, player);
		}else if(args[1].equalsIgnoreCase("VIP")){
			sendMessage(1598429, player);
		}else if(args[1].equalsIgnoreCase("ULTRA")){
			sendMessage(1598429, player);
		}else if(args[1].equalsIgnoreCase("LEGEND")){
			sendMessage(1598443, player);
		}else if(args[1].equalsIgnoreCase("MVP")){
			sendMessage(1598450, player);
		}else if(args[1].equalsIgnoreCase("MVP+")){
			sendMessage(1598460, player);
		}else if(args[1].equalsIgnoreCase("VIPtoULTRA")){
			sendMessage(1598466, player);
		}else if(args[1].equalsIgnoreCase("VIPtoLEGEND")){
			sendMessage(1598467, player);
		}else if(args[1].equalsIgnoreCase("VIPtoMVP")){
			sendMessage(1598468, player);
		}else if(args[1].equalsIgnoreCase("VIPtoMVP+")){
			sendMessage(1598470, player);
		}else if(args[1].equalsIgnoreCase("ULTRAtoLEGEND")){
			sendMessage(1598472, player);
		}else if(args[1].equalsIgnoreCase("ULTRAtoMVP")){
			sendMessage(1598474, player);
		}else if(args[1].equalsIgnoreCase("ULTRAtoMVP+")){
			sendMessage(1598477, player);
		}else if(args[1].equalsIgnoreCase("LEGENDtoMVP")){
			sendMessage(1598478, player);
		}else if(args[1].equalsIgnoreCase("LEGENDtoMVP+")){
			sendMessage(1598486, player);
		}else if(args[1].equalsIgnoreCase("MVPtoMVP+")){
			sendMessage(1598487, player);
		}else if(args[1].equalsIgnoreCase("VIP1")){
			sendMessage(1598492, player);
		}else if(args[1].equalsIgnoreCase("VIP3")){
			sendMessage(1598493, player);
		}else if(args[1].equalsIgnoreCase("VIP6")){
			sendMessage(1598494, player);
		}else if(args[1].equalsIgnoreCase("ULTRA1")){
			sendMessage(1598497, player);
		}else if(args[1].equalsIgnoreCase("ULTRA3")){
			sendMessage(1598498, player);
		}else if(args[1].equalsIgnoreCase("ULTRA6")){
			sendMessage(1598499, player);
		}else if(args[1].equalsIgnoreCase("LEGEND1")){
			sendMessage(1598500, player);
		}else if(args[1].equalsIgnoreCase("LEGEND3")){
			sendMessage(1598501, player);
		}else if(args[1].equalsIgnoreCase("LEGEND6")){
			sendMessage(1598502, player);
		}else if(args[1].equalsIgnoreCase("MVP1")){
			sendMessage(1598503, player);
		}else if(args[1].equalsIgnoreCase("MVP3")){
			sendMessage(1598504, player);
		}else if(args[1].equalsIgnoreCase("MVP6")){
			sendMessage(1598506, player);
		}else if(args[1].equalsIgnoreCase("MVP+1")){
			sendMessage(1598507, player);
		}else if(args[1].equalsIgnoreCase("MVP+3")){
			sendMessage(1598508, player);
		}else if(args[1].equalsIgnoreCase("MVP+6")){
			sendMessage(1598511, player);
		}
		return false;
	}

	public void sendMessage(int id, Player player){
		player.sendMessage(" ");
		player.sendMessage(" ");
		player.sendMessage("§aKlicke auf den Link, um fortzufahren:");
		TextComponent message = new TextComponent("§b§lhttp://Shop.ClashMC.eu/p="+id+"&ign="+player.getName());
		message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://shop.clashmc.eu/checkout/packages?action=add&package="+id+"&ign="+player.getName()));
		player.spigot().sendMessage(message);
		player.sendMessage(" ");
		player.sendMessage(" ");
	}
}

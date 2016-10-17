package eu.epicpvp.kcore.AuktionsMarkt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandSell implements CommandExecutor {
	
	@Override
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "sell", alias={"verkaufen"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/"+cmd.getName()+" [Price/Preis]");
		}else{
			try {
	            double price = Double.valueOf(args[0]);
	            
	            AuktionsMarkt.getAuktionsMarkt().addOffer(player, price);
				return true;
	        } catch (NumberFormatException e) {
	        	player.sendMessage(TranslationHandler.getPrefixAndText(player, "MONEY_NO_DOUBLE"));
	        }
		}
		return false;
	}
}

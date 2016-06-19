package eu.epicpvp.kcore.Neuling;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandNeuling implements CommandExecutor, Listener{
	
	NeulingManager nManager;
	
	public CommandNeuling(NeulingManager nManager){
		this.nManager=nManager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "neuling", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(nManager.getPlayers().containsKey(p)){
			nManager.del(p);
		}else{
			p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "NEULING_CMD"));
		}
		return false;
	}
	
}


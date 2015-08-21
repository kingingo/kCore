package me.kingingo.kcore.Neuling;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandNeuling implements CommandExecutor, Listener{
	
	NeulingManager nManager;
	
	public CommandNeuling(NeulingManager nManager){
		this.nManager=nManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "neuling", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(nManager.getPlayers().containsKey(p)){
			nManager.del(p);
		}else{
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "NEULING_CMD"));
		}
		return false;
	}
	
}


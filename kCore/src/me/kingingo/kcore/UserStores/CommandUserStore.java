package me.kingingo.kcore.UserStores;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUserStore implements CommandExecutor{
	
	@Getter
	private UserStores userStores;
	
	public CommandUserStore(UserStores userStores){
		this.userStores=userStores;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "mystore", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		this.userStores.openInv(p);
		return false;
	}
}

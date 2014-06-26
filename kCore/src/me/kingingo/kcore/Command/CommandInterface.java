package me.kingingo.kcore.Command;
import org.bukkit.command.CommandSender;

public interface CommandInterface {
	public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String cmdLabel, String[] args);
}
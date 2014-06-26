package me.kingingo.kcore.Command;
import org.bukkit.command.CommandSender;

public abstract class Command implements CommandInterface {
	final String cmd;
	final String lbl;

	public Command(String command, String label) {
		cmd = command;
		lbl = label;
	}
	
	public String getLabel(){
		return lbl;
	}

	public String getCommand(){
		return cmd;
	}
}

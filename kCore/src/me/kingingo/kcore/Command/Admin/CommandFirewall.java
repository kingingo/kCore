package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Listener.BungeeCordFirewall.BungeeCordFirewallListener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFirewall extends kListener implements CommandExecutor{
	
	private BungeeCordFirewallListener instance;
	
	public CommandFirewall(BungeeCordFirewallListener instance){
		super(instance.getMysql().getInstance(),"kfirewall");
		this.instance=instance;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "kfirewall", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.isOp()){
				if(args.length == 0){
					if(instance.isFirewall()){
						instance.setFirewall(false);
						p.sendMessage(Language.getText(p, "PREFIX")+ "�cfalse");
					}else{
						instance.setFirewall(true);
						p.sendMessage(Language.getText(p, "PREFIX")+ "�atrue");
					}
				}
			}
		}
		return false;
	}
	
}


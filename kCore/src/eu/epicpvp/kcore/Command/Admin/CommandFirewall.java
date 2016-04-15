package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Listener.BungeeCordFirewall.BungeeCordFirewallListener;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandFirewall extends kListener implements CommandExecutor{
	
	private BungeeCordFirewallListener instance;
	
	public CommandFirewall(BungeeCordFirewallListener instance){
		super(instance.getMysql().getInstance(),"kfirewall");
		this.instance=instance;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "kfirewall", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.isOp()){
				if(args.length == 0){
					if(instance.isFirewall()){
						instance.setFirewall(false);
						p.sendMessage(TranslationManager.getText(p, "PREFIX")+ "§cfalse");
					}else{
						instance.setFirewall(true);
						p.sendMessage(TranslationManager.getText(p, "PREFIX")+ "§atrue");
					}
				}
			}
		}
		return false;
	}
	
}

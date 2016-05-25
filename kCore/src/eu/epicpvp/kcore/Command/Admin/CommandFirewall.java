package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Listener.BungeeCordFirewall.BungeeCordFirewallListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandFirewall implements CommandExecutor{
	
	private BungeeCordFirewallListener instance;
	
	public CommandFirewall(BungeeCordFirewallListener instance){
		this.instance=instance;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "kfirewall", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.isOp()){
				if(args.length == 0){
					if(instance.isFirewall()){
						instance.setFirewall(false);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX")+ "§cfalse");
					}else{
						instance.setFirewall(true);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX")+ "§atrue");
					}
				}
			}
		}else{
			if(args.length == 0){
				if(instance.isFirewall()){
					instance.setFirewall(false);
					System.out.println("[kFirewall] Die Firewall wurde deaktiviert!");
				}else{
					instance.setFirewall(true);
					System.out.println("[kFirewall] Die Firewall wurde aktiviert!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void channel(ServerMessageEvent ev){
		if(ev.getChannel().equalsIgnoreCase("firewall")){
			
		}
	}
	
}


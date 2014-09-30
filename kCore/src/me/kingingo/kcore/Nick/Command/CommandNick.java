package me.kingingo.kcore.Nick.Command;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Nick.NickManager;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandNick implements CommandExecutor, Listener{
	
	NickManager nManager;
	PermissionManager pManager;
	
	public CommandNick(NickManager nManager,PermissionManager pManager){
		this.nManager=nManager;
		this.pManager=pManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "nick", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p =(Player)cs;
		if(args.length==0){
			p.sendMessage(Text.PREFIX.getText()+"§c/nick [Name/Random]");
			return false;
		}
		if(args[0].equalsIgnoreCase("random")){
			if(pManager.hasPermission(p, Permission.NICK_RANDOM)){
				String n = nManager.setNick(p);
				p.sendMessage(Text.PREFIX.getText()+Text.NICK.getText(n));
				return false;
			}
		}else{
			if(pManager.hasPermission(p, Permission.NICK_RANDOM)){
				nManager.setNick(p,args[0]);
				p.sendMessage(Text.PREFIX.getText()+Text.NICK.getText(args[0]));
				return false;
			}
		}
		p.sendMessage(Text.PREFIX.getText()+Text.NO_PERMISSION.getText());
		return false;
	}
	
}


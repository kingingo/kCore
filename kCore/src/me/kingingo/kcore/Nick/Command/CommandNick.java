package me.kingingo.kcore.Nick.Command;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Nick.NickManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandNick implements CommandExecutor, Listener{
	
	NickManager nManager;
	
	public CommandNick(NickManager nManager){
		this.nManager=nManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "knick", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p =(Player)cs;
		if(args.length==0){
			p.sendMessage(Text.PREFIX.getText()+"§c/knick [Name/Random]");
			return false;
		}
		if(args[0].equalsIgnoreCase("random")){
			if(p.hasPermission(kPermission.NICK_RANDOM.getPermissionToString())){
				String n = nManager.setNick(p);
				p.sendMessage(Text.PREFIX.getText()+Text.NICK.getText(n));
				return false;
			}
		}else{
			if(p.hasPermission( kPermission.NICK_RANDOM.getPermissionToString())){
				nManager.setNick(p,args[0]);
				p.sendMessage(Text.PREFIX.getText()+Text.NICK.getText(args[0]));
				return false;
			}
		}
		p.sendMessage(Text.PREFIX.getText()+Text.NO_PERMISSION.getText());
		return false;
	}
	
}


package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandkSpawn implements CommandExecutor{

	private String s;
	private Long l;
	private Player p;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "kspawn", alias = {"ksp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd,String arg2,String[] args) {
		p = (Player)cs;
		if(args.length==0){
			if(p.hasPermission(kPermission.KSPAWN.getPermissionToString())){
				s=UtilTime.getTimeManager().check(cmd.getName(), p);
				if(s!=null){
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "USE_BEFEHL_TIME",s));
				}else{
					p.teleport(Bukkit.getWorld("world").getSpawnLocation());
					l=UtilTime.getTimeManager().hasPermission(p, cmd.getName());
					if( l!=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), p, l);
					}
				}
			}
		}
		return false;
	}
	
}
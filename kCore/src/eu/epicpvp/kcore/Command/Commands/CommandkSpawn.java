package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandkSpawn implements CommandExecutor{

	private AntiLogoutManager manager;
	
	public CommandkSpawn(AntiLogoutManager manager){
		this.manager=manager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "kspawn", alias = {"ksp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd,String arg2,String[] args) {
		Player p = (Player) cs;
		if(args.length==0){
			if(p.hasPermission(PermissionType.KSPAWN.getPermissionToString())){
				String s = UtilTime.getTimeManager().check(cmd.getName(), p);
				if(s !=null){
					p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "USE_BEFEHL_TIME", s));
				}else{
					if(manager!=null)manager.del(p);
					p.teleport(Bukkit.getWorld("world").getSpawnLocation());
					Long l = UtilTime.getTimeManager().hasPermission(p, cmd.getName());
					if( l !=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), p, l);
					}
				}
			}
		}
		return false;
	}
	
}
package me.kingingo.kcore.Command.Admin;

import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandURang implements CommandExecutor{

	private PermissionManager manager;
	private MySQL mysql;
	
	public CommandURang(PermissionManager manager,MySQL mysql){
		this.manager=manager;
		this.mysql=mysql;
	}
	
	Player p;
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "urang", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {	
		boolean c = false;
		
		if(sender instanceof ConsoleCommandSender){
			c = true;
		}
		
		if(!c){
			p = (Player) sender;
			
			if(!p.isOp()){
				return false;
			}
		}
		
		
		
		if(args.length == 0 || args.length == 1 || args.length == 2){
			if(c){
				System.out.println("[Befehl] /urang [Player] [Rang] [Upgrade Rang]");
			}else{
				p.sendMessage("�a[Befehl] �c/urang [Player] [Rang] [Upgrade Rang]");
			}
		}
		
		if(args.length == 3){
			if(c){
				String player = args[0];
				String isGroup = args[1];
				String toGroup = args[2];
				
				if(setGroup(player,isGroup,toGroup)){
					System.out.println("[Buycraft UPGRADE Rang] " + player + " old Group : " + isGroup + " | new Group : " + toGroup);
				}else{
					System.err.println("[Fehler] es ist ein Fehler passiert! ");
					System.err.println("[Fehler] Player : " + player);
					System.err.println("[Fehler] isGroup : " + isGroup);
					System.err.println("[Fehler] toGroup : " + toGroup);
					System.err.println("[Fehler] es ist ein Fehler passiert!");
				}
				
			}else{
				String player = args[0];
				String isGroup = args[1];
				String toGroup = args[2];
				
				if(setGroup(player,isGroup,toGroup)){
					p.sendMessage("�a[Buycraft UPGRADE Rang] �c" + player + " old Group : " + isGroup + " | new Group : " + toGroup);
				}else{
					System.err.println("[Fehler] es ist ein Fehler passiert! ");
					System.err.println("[Fehler] Player : " + player);
					System.err.println("[Fehler] isGroup : " + isGroup);
					System.err.println("[Fehler] toGroup : " + toGroup);
					System.err.println("[Fehler] es ist ein Fehler passiert!");
					p.sendMessage("�c[Fehler] kuck in die Console");
				}
			}
		}
		return false;
	}
	
	public boolean setGroup(String player,String isGroup, String toGroup){
		boolean ok = false;
		UUID uuid = UtilPlayer.getUUID(player, mysql);
		if(manager.getGroup( uuid ).equalsIgnoreCase(isGroup) ){
			manager.setGroup(uuid, toGroup);
			ok = true;
		}
		
		return ok;
	}
	
}

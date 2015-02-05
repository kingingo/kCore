package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandGroup implements CommandExecutor{

	PermissionManager manager;
	
	public CommandGroup(PermissionManager manager){
		this.manager=manager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "group", alias = {"group","g","k"}, sender = Sender.EVERYONE, permissions = {"epicpvp.*"})
	public boolean onCommand(CommandSender cs, Command cmd,String label, String[] args){
		
		if(cs instanceof ConsoleCommandSender){
			
			if(args[0].equalsIgnoreCase("addperm")){
				
				if(args.length == 1 || args.length == 2){
					System.out.println("[kPermission] /k addperm [Player] [Permission]");	
				}
				
				if(args.length == 3){
					
					String player = args[1];
					String perm = args[2];
					manager.addPermission(UtilPlayer.getUUID(player, manager.getMysql()),Permission.isPerm(perm));
					System.out.println("[kPermission] " + player + " hat die " + perm + " erhalten");
				}
				
				return false;
			}
			
			if(args[0].equalsIgnoreCase("delperm")){
				
				if(args.length == 1 || args.length == 2){
					System.out.println("[kPermission] /k delperm [Player] [Permission]");	
				}
				
				if(args.length == 3){
					
					String player = args[1];
					String perm = args[2];
					manager.removePermission(UtilPlayer.getUUID(player, manager.getMysql()), perm);
					System.out.println("[kPermission] " + player + " hat nun nicht mehr die Permission " + perm);
				}
				
				return false;
			}
			
			if(args[0].equalsIgnoreCase("add")){
				
				if(args.length == 1 || args.length == 2){
					System.out.println("[kPermission] /k add [Player] [Rang]");	
				}
				
				if(args.length == 3){
					
					String player = args[1];
					String rang = args[2];
					
					manager.setGroup(UtilPlayer.getUUID(player, manager.getMysql()), rang);
					System.out.println("[kPermission] " + player + " hat den Rang " + rang + " erhalten");
				}
				
				return false;
			}
			
			
			return false;
		}
		
		return true;
	}


}

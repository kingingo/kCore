package me.kingingo.kcore.Command.Admin;

import java.util.List;
import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.GroupTyp;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilReflection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;

public class CommandGroup implements CommandExecutor{

	PermissionManager manager;
	
	public CommandGroup(PermissionManager manager){
		this.manager=manager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "group", alias = {"group","g","k"}, sender = Sender.EVERYONE, permissions = {"epicpvp.*"})
	public boolean onCommand(CommandSender cs, Command cmd,String label, String[] args){
		
		if(cs instanceof ConsoleCommandSender){
			
			if(args[0].equalsIgnoreCase("group")){
				if(manager.getGroups().containsKey(args[1])){
					System.out.println("[EpicPvP] Group: "+args[1].toLowerCase());
					for(String perm : manager.getGroups().get(args[1]).getPerms()){
						System.out.println("[EpicPvP] Perm: "+perm);
					}	
				}else{
					System.out.println("[EpicPvP] nicht gefunden "+args[1].toLowerCase());
				}
			}
			
			if(args[0].equalsIgnoreCase("permtest")){
				if(UtilPlayer.isOnline(args[1])){
					System.out.println("[EpicPvP] Player: "+Bukkit.getPlayer(args[1]).getName()+" BOOLEAN:"+Bukkit.getPlayer(args[1]).hasPermission(args[2]));
				}
			}
			
			if(args[0].equalsIgnoreCase("r1")){
				if(UtilPlayer.isOnline(args[1])){
					UUID uuid=UtilPlayer.getRealUUID(Bukkit.getPlayer(args[1]));
					System.out.println("[EpicPvP] Player: "+Bukkit.getPlayer(args[1]).getName());
					if(manager.getPlist().containsKey(uuid)){
						Bukkit.getPlayer(args[1]).removeAttachment(manager.getPlist().get(uuid));
						manager.getPlist().get(uuid).remove();
						manager.getPlist().remove(uuid);
					}
					manager.loadPermission(uuid);
					Bukkit.getPlayer(args[1]).recalculatePermissions();
				}
			}
			
			if(args[0].equalsIgnoreCase("r")){
				if(UtilPlayer.isOnline(args[1])){
					UUID uuid=UtilPlayer.getRealUUID(Bukkit.getPlayer(args[1]));
					System.out.println("[EpicPvP] Player: "+Bukkit.getPlayer(args[1]).getName());
					
					try{
						PermissibleBase base = (PermissibleBase)UtilReflection.getValue("perm", Bukkit.getPlayer(args[1]));
						List<PermissionAttachment> attachments = (List<PermissionAttachment>)UtilReflection.getValue("attachments", base);
						if(!attachments.contains(manager.getPlist().get(uuid)))attachments.add(manager.getPlist().get(uuid));
					}catch(Exception e){
						System.out.println("[EpicPvP] ERROR: "+e.getMessage());
					}
					Bukkit.getPlayer(args[1]).recalculatePermissions();
				}
			}
			
			if(args[0].equalsIgnoreCase("user")){
				if(UtilPlayer.isOnline(args[1])){
					UUID uuid=UtilPlayer.getRealUUID(Bukkit.getPlayer(args[1]));
					System.out.println("[EpicPvP] Player: "+Bukkit.getPlayer(args[1]).getName());
					for(String perm : manager.getPlist().get(uuid).getPermissions().keySet()){
						System.out.println("[EpicPvP] Perm: "+perm+" "+manager.getPlist().get(uuid).getPermissions().get(perm));
					}
					
					Bukkit.getPlayer(args[1]).recalculatePermissions();
				}
			}
			
			if(args[0].equalsIgnoreCase("addperm")){
				
				if(args.length == 1 || args.length == 2){
					System.out.println("[kPermission] /k addperm [Player] [Permission]");	
				}
				
				if(args.length == 3){
					
					String player = args[1];
					String perm = args[2];
					
					manager.addPermission(UtilPlayer.getUUID(player, manager.getMysql()),kPermission.isPerm(perm));
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
					UUID uuid;
					
					if(UtilPlayer.isOnline(player)){
						uuid=UtilPlayer.getRealUUID(Bukkit.getPlayer(player));
					}else{
						uuid=UtilPlayer.getUUID(player, manager.getMysql());
					}
					
					manager.setGroup(uuid, rang);
					System.out.println("[kPermission] " + player + " hat den Rang " + rang + " erhalten");
				}
				
				return false;
			}
			
			
			return false;
		}
		
		return true;
	}


}

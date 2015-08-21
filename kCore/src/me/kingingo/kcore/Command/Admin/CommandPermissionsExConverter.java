package me.kingingo.kcore.Command.Admin;

import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.GroupTyp;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandPermissionsExConverter implements CommandExecutor, Listener{
	
	PermissionManager permManager;
	boolean chat=true;
	
	public CommandPermissionsExConverter(PermissionManager permManager){
		this.permManager=permManager;
		Bukkit.getPluginManager().registerEvents(this, permManager.getInstance());
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "pexconvert", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(permManager.hasPermission(p, kPermission.ALL_PERMISSION)){
				convert(GroupTyp.get(args[0]));
			}
		}else{
			convert(GroupTyp.get(args[0]));
		}
		return false;
	}
	
	public void convert(GroupTyp typ){
		if(Bukkit.getPluginManager().getPlugin("PermissionsEx")!=null){
			ru.tehkode.permissions.PermissionManager pex = ((PermissionsEx)Bukkit.getPluginManager().getPlugin("PermissionsEx")).getPermissionsManager();	
			int a = 0;
			int ea = 0;
			UUID uuid=null;
			for(PermissionGroup group : pex.getGroups()){
				if(group.getName().equalsIgnoreCase("default"))continue;
					for(int i = 0; i<group.getUsers().size() ; i++){
						try{
							uuid=UUID.fromString(((PermissionUser)group.getUsers().toArray()[i]).getIdentifier());
							uuid=UtilPlayer.getRealUUID(((PermissionUser)group.getUsers().toArray()[i]).getName(), uuid);
							System.out.println("[EpicPvP] SPIELER:"+((PermissionUser)group.getUsers().toArray()[i]).getName()+" GROUP:"+group.getName()+" UUID:"+uuid);
							a++;
						}catch(IllegalArgumentException e){
							uuid=UtilPlayer.getOfflineUUID(((PermissionUser)group.getUsers().toArray()[i]).getName());
							System.out.println("[EpicPvP] SPIELER:"+((PermissionUser)group.getUsers().toArray()[i]).getName()+" GROUP:"+group.getName()+" OFFLINE-UUID"+uuid);
							ea++;
						}
						permManager.getMysql().Update("INSERT INTO game_perm (prefix,permission,pgroup,grouptyp,uuid) values ('none','none','"+group.getName().toLowerCase()+"','"+typ.getName()+"','"+uuid+"');");
					}
			}
			System.out.println("[EpicPvP] PermissionsEx Converter FERTIG["+a+"/"+ea+"]");
		}
	}
	
}


package me.kingingo.kcore.Command.Commands;

import java.util.Locale;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Mob;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawnmob implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "spawnmob", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.SPAWNMOB.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"/spawnmob [Mob] [Anzahl]");
				String s = Text.PREFIX.getText()+"";
				for(Mob mob : Mob.values())s+=mob.name()+",";
				player.sendMessage(s.substring(0, s.length()-1));
			}else{
				Mob mob = Mob.fromName(args[0]);
				
				if(mob==null){
					player.sendMessage(Text.PREFIX.getText()+"Mob Type nicht gefunden!");
					return false;
				}
				if(!player.hasPermission(kPermission.SPAWNMOB.getPermissionToString()+"."+mob.name.toLowerCase(Locale.ENGLISH))){
					if(!player.hasPermission(kPermission.SPAWNMOB_ALL.getPermissionToString())){
						player.sendMessage(Text.PREFIX.getText()+Text.NO_PERMISSION.getText());
						return false;
					}
				}
				
				int a = 10;
				
				if(args.length==2&&player.hasPermission(kPermission.SPAWNMOB_ALL.getPermissionToString())){
					try{
						a=Integer.valueOf(args[1]);
					}catch(NumberFormatException e){
						player.sendMessage(Text.PREFIX.getText()+"Das ist keine Zahl!");
						return false;
					}
				}
				
				for(int i = 0; i<=a;i++){
					player.getLocation().getWorld().spawnCreature(player.getLocation(), mob.getType());
				}
			}
		}
		return false;
	}

}

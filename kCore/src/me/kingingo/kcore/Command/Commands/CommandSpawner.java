package me.kingingo.kcore.Command.Commands;

import java.util.HashSet;
import java.util.Locale;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Mob;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawner implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "spawner", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.SPAWNER.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/spawner [Mob]");
			}else{
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "USE_BEFEHL_TIME",s));
				}else{
				Mob mob = Mob.fromName(args[0]);
				
				if(mob==null){
					player.sendMessage(Language.getText(player, "PREFIX")+"Mob Type nicht gefunden!");
					return false;
				}
				if(!player.hasPermission(kPermission.SPAWNER.getPermissionToString()+"."+mob.name.toLowerCase(Locale.ENGLISH))){
					if(!player.hasPermission(kPermission.SPAWNER_ALL.getPermissionToString())){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_PERMISSION"));
						return false;
					}
				}
				
				Block block = player.getTargetBlock((HashSet<Byte>) null, 100);
				if(block!=null&&block.getType()==Material.MOB_SPAWNER){
					l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l!=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
					CreatureSpawner spawner = (CreatureSpawner)block.getState();
					spawner.setSpawnedType(mob.getType());
					spawner.update();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "LOOK_ON_SPAWNER"));
				}
				}
			}
		}
		return false;
	}

}

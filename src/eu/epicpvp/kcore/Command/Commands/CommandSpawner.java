package eu.epicpvp.kcore.Command.Commands;

import java.util.HashSet;
import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Enum.Mob;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandSpawner implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "spawner", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission(PermissionType.SPAWNER.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/spawner [Mob]");
			}else{
				String s = UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s !=null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME", s));
				}else{
				Mob mob = Mob.fromName(args[0]);
				
				if(mob==null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"Mob Type nicht gefunden!");
					return false;
				}
				if(!player.hasPermission(PermissionType.SPAWNER.getPermissionToString()+"."+mob.name.toLowerCase(Locale.ENGLISH))){
					if(!player.hasPermission(PermissionType.SPAWNER_ALL.getPermissionToString())){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_PERMISSION"));
						return false;
					}
				}
				
				Block block = player.getTargetBlock((HashSet<Byte>) null, 100);
				if(block!=null&&block.getType()==Material.MOB_SPAWNER){
					Long l = UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l !=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
					CreatureSpawner spawner = (CreatureSpawner)block.getState();
					spawner.setSpawnedType(mob.getType());
					spawner.update();
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "LOOK_ON_SPAWNER"));
				}
				}
			}
		}
		return false;
	}

}

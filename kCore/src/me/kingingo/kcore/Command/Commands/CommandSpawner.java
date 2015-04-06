package me.kingingo.kcore.Command.Commands;

import java.util.Locale;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Mob;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawner implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "spawner", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.SPAWNER.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"/spawner [Mob]");
			}else{
				Mob mob = Mob.fromName(args[0]);
				
				if(mob==null){
					player.sendMessage(Text.PREFIX.getText()+"Mob Type nicht gefunden!");
					return false;
				}
				if(!player.hasPermission(kPermission.SPAWNER+"."+mob.name.toLowerCase(Locale.ENGLISH))){
					if(!player.hasPermission(kPermission.SPAWNER_ALL.getPermissionToString())){
						player.sendMessage(Text.PREFIX.getText()+Text.NO_PERMISSION.getText());
						return false;
					}
				}
				
				Block block = player.getTargetBlock(null,20);
				if(block!=null&&block.getType()==Material.MOB_SPAWNER){
					CreatureSpawner spawner = (CreatureSpawner)block.getState();
					spawner.setSpawnedType(mob.getType());
					spawner.update();
				}else{
					player.sendMessage(Text.PREFIX.getText()+"Du musst einen MobSpawner ankucken.");
				}
			}
		}
		return false;
	}

}

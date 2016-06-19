package eu.epicpvp.kcore.Command.Commands;

import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Enum.Mob;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandSpawnmob implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "spawnmob", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission(PermissionType.SPAWNMOB.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/spawnmob [Mob] [Anzahl]");
				StringBuilder sb = new StringBuilder();
				sb.append(TranslationHandler.getText(player, "PREFIX"));
				for(Mob mob : Mob.values())sb.append(mob.name()).append(", ");
				player.sendMessage(sb.substring(0, sb.length()-2));
			}else{
				String s = UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s !=null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME", s));
				}else{
					Mob mob = Mob.fromName(args[0]);
					
					if(mob==null){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "MOB_TYPE_NOT_FOUND"));
						return false;
					}
					if(!player.hasPermission(PermissionType.SPAWNMOB.getPermissionToString()+"."+mob.name.toLowerCase(Locale.ENGLISH))){
						if(!player.hasPermission(PermissionType.SPAWNMOB_ALL.getPermissionToString())){
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_PERMISSION"));
							return false;
						}
					}
					
					int a = 10;
					
					if(args.length==2&& player.hasPermission(PermissionType.SPAWNMOB_ALL.getPermissionToString())){
						try{
							a=Integer.valueOf(args[1]);
						}catch(NumberFormatException e){
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_INTEGER"));
							return false;
						}
					}

					Long l = UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l !=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
					
					for(int i = 0; i<=a;i++){
						player.getLocation().getWorld().spawnCreature(player.getLocation(), mob.getType());
					}
				}
			}
		}
		return false;
	}

}

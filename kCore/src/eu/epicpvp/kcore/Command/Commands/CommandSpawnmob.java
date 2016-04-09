package eu.epicpvp.kcore.Command.Commands;

import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Enum.Mob;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandSpawnmob implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "spawnmob", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(PermissionType.SPAWNMOB.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/spawnmob [Mob] [Anzahl]");
				String s = TranslationManager.getText(player, "PREFIX")+"";
				for(Mob mob : Mob.values())s+=mob.name()+",";
				player.sendMessage(s.substring(0, s.length()-1));
			}else{
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "USE_BEFEHL_TIME",s));
				}else{
					Mob mob = Mob.fromName(args[0]);
					
					if(mob==null){
						player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "MOB_TYPE_NOT_FOUND"));
						return false;
					}
					if(!player.hasPermission(PermissionType.SPAWNMOB.getPermissionToString()+"."+mob.name.toLowerCase(Locale.ENGLISH))){
						if(!player.hasPermission(PermissionType.SPAWNMOB_ALL.getPermissionToString())){
							player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NO_PERMISSION"));
							return false;
						}
					}
					
					int a = 10;
					
					if(args.length==2&&player.hasPermission(PermissionType.SPAWNMOB_ALL.getPermissionToString())){
						try{
							a=Integer.valueOf(args[1]);
						}catch(NumberFormatException e){
							player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NO_INTEGER"));
							return false;
						}
					}

					l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l!=0 ){
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

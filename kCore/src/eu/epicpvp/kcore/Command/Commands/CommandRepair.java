package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandRepair implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "repair",alias={"rp","fix","ifix"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(PermissionType.REPAIR.getPermissionToString())){
			
			s=UtilTime.getTimeManager().check(cmd.getName(), player);
			if(s!=null){
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "USE_BEFEHL_TIME",s));
			}else{
				
				l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
				if( l!=0 ){
					UtilTime.getTimeManager().add(cmd.getName(), player, l);
				}
				
				if(args.length==0){
					if(player.hasPermission(PermissionType.REPAIR_HAND.getPermissionToString())){
						UtilItem.RepairItem(player.getItemInHand());
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "REPAIR_HAND"));
					}
				}else{
					if(args[0].equalsIgnoreCase("all")){
						if(player.hasPermission(PermissionType.REPAIR_ALL.getPermissionToString())){
							if(player.hasPermission(PermissionType.REPAIR_BODY.getPermissionToString())){
								UtilInv.repairInventory(player, true);
							}else{
								UtilInv.repairInventory(player, false);
							}
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "REPAIR_ALL"));
						}
					}else if(args[0].equalsIgnoreCase("allplayer")){
						if(player.hasPermission(PermissionType.REPAIR_ALL_PLAYERS.getPermissionToString())){
							for(Player p : UtilServer.getPlayers()){
								UtilInv.repairInventory(p, true);
								p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "REPAIR_ALL_BY",new String[]{player.getName()}));
							}
						}
					}
				}
			}
		}
		return false;
	}

}

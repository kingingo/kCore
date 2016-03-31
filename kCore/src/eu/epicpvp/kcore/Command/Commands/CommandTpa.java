package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class CommandTpa implements CommandExecutor{
	
	@Getter
	private TeleportManager manager;
	private Player player;
	private Player tp;
	private String s;
	private long l;
	
	public CommandTpa(TeleportManager manager){
		this.manager=manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpa", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_A)){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"§6/tpa [Player]");
			}else{
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "USE_BEFEHL_TIME",s));
				}else{
					if(UtilPlayer.isOnline(args[0])){
						tp=Bukkit.getPlayer(args[0]);
						if(getManager().getTeleport_anfrage().containsKey(tp))getManager().getTeleport_anfrage().remove(tp);
						if(player.hasPermission(PermissionType.PLAYER_TELEPORT_A_BYPASS.getPermissionToString())){
							//new Teleporter(player, tp)
							getManager().getTeleport_anfrage().put(tp, new Teleporter(player, tp));
						}else{
							//new Teleporter(player, tp,3)
							getManager().getTeleport_anfrage().put(tp, new Teleporter(player, tp,3));
						}
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "TELEPORT_ANFRAGE_SENDER",tp.getName()));
						tp.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "TELEPORT_ANFRAGE_EMPF§NGER",player.getName()));
						l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
						if( l!=0 ){
							UtilTime.getTimeManager().add(cmd.getName(), player, l);
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					}
				}
			}
		}
		return false;
	}
	
}
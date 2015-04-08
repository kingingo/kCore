package me.kingingo.kcore.Command.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "tpa", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(getManager().getPermManager().hasPermission(player, kPermission.PLAYER_TELEPORT_A)){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"�6/tpa [Player]");
			}else{
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(Text.PREFIX.getText()+Text.USE_BEFEHL_TIME.getText(s));
				}else{
					if(UtilPlayer.isOnline(args[0])){
						tp=Bukkit.getPlayer(args[0]);
						if(getManager().getTeleport_anfrage().containsKey(tp))getManager().getTeleport_anfrage().remove(tp);
						getManager().getTeleport_anfrage().put(tp, new Teleporter(player, tp));
						player.sendMessage(Text.PREFIX.getText()+Text.TELEPORT_ANFRAGE_SENDER.getText(tp.getName()));
						tp.sendMessage(Text.PREFIX.getText()+Text.TELEPORT_ANFRAGE_EMPF�NGER.getText(player.getName()));
						l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
						if( l!=0 ){
							UtilTime.getTimeManager().add(cmd.getName(), player, l);
						}
					}else{
						player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
					}
				}
			}
		}
		return false;
	}
	
}
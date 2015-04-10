package me.kingingo.kcore.Command.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.TeleportManager.Teleporter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpdeny implements CommandExecutor{
	
	@Getter
	private TeleportManager manager;
	private Player player;
	
	public CommandTpdeny(TeleportManager manager){
		this.manager=manager;
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "tpdeny",alias={"tpno"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(getManager().getPermManager().hasPermission(player, kPermission.PLAYER_TELEPORT_ACCEPT)){
			if(getManager().getTeleport_anfrage().containsKey(player)){
				if(getManager().getTeleport_anfrage().get(player).getPlayer_to()!=null&&!getManager().getTeleport_anfrage().get(player).getPlayer_to().getName().equalsIgnoreCase(player.getName())){
					getManager().getTeleport_anfrage().get(player).getPlayer_to().sendMessage(Text.PREFIX.getText()+Text.DENY_FROM.getText(player.getName()));
				}
				if(getManager().getTeleport_anfrage().get(player).getFrom()!=null&&!getManager().getTeleport_anfrage().get(player).getFrom().getName().equalsIgnoreCase(player.getName())){
					getManager().getTeleport_anfrage().get(player).getFrom().sendMessage(Text.PREFIX.getText()+Text.DENY_FROM.getText(player.getName()));
				}
				getManager().getTeleport_anfrage().remove(player);
				player.sendMessage(Text.PREFIX.getText()+Text.DENY.getText());
			}else{
				player.sendMessage(Text.PREFIX.getText()+Text.NO_ANFRAGE.getText());
			}
		}
		return false;
	}
	
}
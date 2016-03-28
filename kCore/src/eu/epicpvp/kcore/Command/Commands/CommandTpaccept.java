package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import lombok.Getter;

public class CommandTpaccept implements CommandExecutor{
	
	@Getter
	private TeleportManager manager;
	private Player player;
	
	public CommandTpaccept(TeleportManager manager){
		this.manager=manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpaccept",alias={"tpyes"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_ACCEPT)){
			if(getManager().getTeleport_anfrage().containsKey(player)){
				getManager().getTeleport().add(getManager().getTeleport_anfrage().get(player));
				if(getManager().getTeleport_anfrage().get(player).getTo()!=null&&!getManager().getTeleport_anfrage().get(player).getTo().getName().equalsIgnoreCase(player.getName())){
					getManager().getTeleport_anfrage().get(player).getTo().sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "ACCEPT_FROM",player.getName()));
				}
				if(getManager().getTeleport_anfrage().get(player).getFrom()!=null&&!getManager().getTeleport_anfrage().get(player).getFrom().getName().equalsIgnoreCase(player.getName())){
					getManager().getTeleport_anfrage().get(player).getFrom().sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "ACCEPT_FROM",player.getName()));
				}
				getManager().getTeleport_anfrage().remove(player);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "ACCEPT"));
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_ANFRAGE"));
			}
		}
		return false;
	}
	
}
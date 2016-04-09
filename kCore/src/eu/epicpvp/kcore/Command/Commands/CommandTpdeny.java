package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.Translation.TranslationManager;
import lombok.Getter;

public class CommandTpdeny implements CommandExecutor{
	
	@Getter
	private TeleportManager manager;
	private Player player;
	
	public CommandTpdeny(TeleportManager manager){
		this.manager=manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpdeny",alias={"tpno"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_ACCEPT)){
			if(getManager().getTeleport_anfrage().containsKey(player)){
				if(getManager().getTeleport_anfrage().get(player).getTo()!=null&&!getManager().getTeleport_anfrage().get(player).getTo().getName().equalsIgnoreCase(player.getName())){
					getManager().getTeleport_anfrage().get(player).getTo().sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "DENY_FROM",player.getName()));
				}
				if(getManager().getTeleport_anfrage().get(player).getFrom()!=null&&!getManager().getTeleport_anfrage().get(player).getFrom().getName().equalsIgnoreCase(player.getName())){
					getManager().getTeleport_anfrage().get(player).getFrom().sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "DENY_FROM",player.getName()));
				}
				getManager().getTeleport_anfrage().remove(player);
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "DENY"));
			}else{
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NO_ANFRAGE"));
			}
		}
		return false;
	}
	
}
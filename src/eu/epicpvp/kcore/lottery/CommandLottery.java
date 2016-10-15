package eu.epicpvp.kcore.lottery;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Util.UtilInv;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CommandLottery implements CommandExecutor {

	private final Lottery lottery;

	@CommandHandler.Command(command = "lottery", sender = CommandHandler.Sender.PLAYER)
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		Player plr = (Player) sender;
		lottery.getLotteryInventory().open(plr, UtilInv.getBase());
		return true;
	}
}

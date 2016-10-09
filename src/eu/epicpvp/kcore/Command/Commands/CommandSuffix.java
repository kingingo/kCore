package eu.epicpvp.kcore.Command.Commands;

import java.util.regex.Pattern;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSuffix implements CommandExecutor {
	
	private static final String bold_and_or_cursive = "(?:&l(?:&o)?|&o(?:&l)?)";
	private static final String allowedColors = "[1-35-9a-f]";
	public static final Pattern SUFFIX_PATTERN = Pattern.compile("^(?:(?:&" + allowedColors + ")" + bold_and_or_cursive + "?|" + bold_and_or_cursive + ")$", Pattern.CASE_INSENSITIVE);
	private UserDataConfig userData;

	public CommandSuffix(UserDataConfig userData) {
		this.userData = userData;
	}

	@Override
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "suffix", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player) sender;
		if (player.hasPermission(PermissionType.SUFFIX.getPermissionToString())) {
			if (args.length == 0) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/suffix &COLOR");
			} else if (args.length == 1 || ! player.isOp()) {
				if (args[0].equalsIgnoreCase("reset")) {
					this.userData.getConfig(player).set("Chat.Suffix", null);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "SUFFIX_RESET"));
				} else {
					setSuffix(player, player, args[0]);
				}
			} else if (player.isOp()) {
				String name = args[0];
				Player target = Bukkit.getPlayerExact(name);
				if (target == null) {
					player.sendMessage("§cDer Spieler ist zurzeit nicht online.");
				} else {
					setSuffix(player, target, args[0]);
				}
			}
			return true;
		}
		return false;
	}
	
	public void setSuffix(Player initiator, Player target, String color) {
		if (SUFFIX_PATTERN.matcher(color).matches()) {
			color = ChatColor.translateAlternateColorCodes('&', color);
			this.userData.getConfig(target).set("Chat.Suffix", color);
			initiator.sendMessage(TranslationHandler.getText(initiator, "PREFIX") + TranslationHandler.getText(initiator, "SUFFIX_SAVE"));
		}
		initiator.sendMessage(TranslationHandler.getText(initiator, "PREFIX") + TranslationHandler.getText(initiator, "NOT_COLOR_CODE"));
	}
}

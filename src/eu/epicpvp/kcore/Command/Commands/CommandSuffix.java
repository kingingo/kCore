package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import net.md_5.bungee.api.ChatColor;

public class CommandSuffix implements CommandExecutor {

	private UserDataConfig userData;

	public CommandSuffix(UserDataConfig userData) {
		this.userData = userData;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "suffix", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player) sender;
		if (player.hasPermission(PermissionType.SUFFIX.getPermissionToString())) {
			if (args.length == 0) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/suffix &COLOR");
			} else {
				if (args[0].equalsIgnoreCase("reset")) {
					this.userData.getConfig(player).set("Chat.Suffix", null);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "SUFFIX_RESET"));
					return true;
				} else {
					String color = args[0];

					if (color.contains("&")) {
						color = ChatColor.translateAlternateColorCodes('&', color);
						this.userData.getConfig(player).set("Chat.Suffix", color);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "SUFFIX_SAVE"));
						return true;
						/*
						if (color.length() >= 2) {
							if (color.length() <= 10) {
								if (Color.isColor(color)) {
									
								} else {
									player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NOT_COLOR_CODE"));
								}
							} else {
								player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "SUFFIX_TO_LONG"));
							}
						} else {
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "SUFFIX_TO_SHORT"));
						}
						return false;
						*/
					}

					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NOT_COLOR_CODE"));
				}
			}
			return true;
		}
		return false;
	}
}

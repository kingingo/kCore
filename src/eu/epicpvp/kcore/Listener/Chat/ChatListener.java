package eu.epicpvp.kcore.Listener.Chat;

import java.util.HashMap;

import eu.epicpvp.kcore.Command.Commands.CommandSuffix;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends kListener {
	
	private String suffix = "§8 " + Zeichen.DOUBLE_ARROWS_R.getIcon() + " §7";
	private PermissionManager manager;
	private UserDataConfig userData;
	
	public ChatListener() {
		super(UtilServer.getPluginInstance(), "ChatListener");
		this.manager = UtilServer.getPermissionManager();
		this.userData = UtilServer.getUserData();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			Player p = event.getPlayer();
			String msg = event.getMessage();

//			msg = msg.replaceAll("%", "");
			msg = msg.replaceAll("%", "%%"); //the first % escapes the second %
			if (p.hasPermission(PermissionType.CHAT_FARBIG.getPermissionToString())) {
				msg = ChatColor.translateAlternateColorCodes('&', msg);
				if (!p.hasPermission(PermissionType.CHAT_NERV.getPermissionToString())) {
					for (int i = 0; i < 10; i++) {
						msg = msg.replace("§m", "");
						msg = msg.replace("§n", "");
						msg = msg.replace("§k", "");
					}
				}
			}
			
			kConfig userConfig;
			String playerSuffix = "";
			if (this.userData != null) {
				userConfig = this.userData.getConfig(p);
				
				if (userConfig.contains("Chat.Suffix")) {
					if (!CommandSuffix.SUFFIX_PATTERN.matcher(userConfig.getString("Chat.Suffix")).matches()) {
						userConfig.set("Chat.Suffix", null);
						p.sendMessage(TranslationHandler.getPrefixAndText(p, "SUFFIX_RESET"));
					}
				}
				if (p.hasPermission(PermissionType.SUFFIX.getPermissionToString())) {
					playerSuffix = (userConfig.contains("Chat.Suffix") ? userConfig.getString("Chat.Suffix") : "");
				}
			}
			
			String permissionPrefix = manager.getPrefix(p);
			if (UtilServer.getGildenManager() != null && UtilServer.getGildenManager().isPlayerInGilde(p)) {
				GildenManager gildenmanager = UtilServer.getGildenManager();
				String gilde = gildenmanager.getPlayerGilde(p).toLowerCase();
				String gildenTag = gildenmanager.getTag(gildenmanager.getPlayerGilde(p));
				
				HashMap<String, Integer> gildenExtraPrefixes = gildenmanager.getExtra_prefix();
				if (gildenExtraPrefixes.containsKey(gilde)) {
					Integer gildenExtraPrefix = gildenExtraPrefixes.get(gilde);
					if (gildenExtraPrefix == 1) {
						gildenTag = gildenTag.replace("§7", "§4§l");
					} else if (gildenExtraPrefix == 2) {
						gildenTag = gildenTag.replace("§7", "§2§l");
					} else if (gildenExtraPrefix == 3) {
						gildenTag = gildenTag.replace("§7", "§e§l");
					} else if (gildenExtraPrefix >= 4 && gildenExtraPrefix <= 6) {
						gildenTag = gildenTag.replace("§7", "§3");
					} else if (gildenExtraPrefix >= 7 && gildenExtraPrefix <= 9) {
						gildenTag = gildenTag.replace("§7", "§d");
					} else if (gildenExtraPrefix >= 10 && gildenExtraPrefix <= 12) {
						gildenTag = gildenTag.replace("§7", "§a");
					} else if (gildenExtraPrefix >= 13 && gildenExtraPrefix <= 15) {
						gildenTag = gildenTag.replace("§7", "§b");
					}
				}
				event.setFormat(permissionPrefix + gildenTag + permissionPrefix.substring(0, 2) + p.getName() + suffix + playerSuffix + msg);
			} else if (UtilServer.getGildeHandler() != null && UtilServer.getGildeHandler().hasGilde(p)) {
				String gildeShortname = UtilServer.getGildeHandler().getSection(p).getHandle().getShortName();
				gildeShortname = "§7" + gildeShortname + "§e* ";
				event.setFormat(permissionPrefix + gildeShortname + permissionPrefix.substring(0, 2) + p.getName() + suffix + playerSuffix + msg);
			} else {
				event.setFormat(permissionPrefix + p.getName() + suffix + playerSuffix + msg);
			}
		}
	}
}

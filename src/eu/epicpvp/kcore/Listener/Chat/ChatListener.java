package eu.epicpvp.kcore.Listener.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilServer;

public class ChatListener extends kListener {

	private String suffix = "§8 " + Zeichen.DOUBLE_ARROWS_R.getIcon() + " §7";
	private PermissionManager manager;
	private UserDataConfig userData;

	public ChatListener() {
		super(UtilServer.getPluginInstance(), "ChatListener");
		this.manager=UtilServer.getPermissionManager();
		this.userData=UtilServer.getUserData();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			Player p = event.getPlayer();
			String msg = event.getMessage();

			msg = msg.replaceAll("%", "");
			if (p.hasPermission(PermissionType.CHAT_FARBIG.getPermissionToString())) {
				msg = msg.replaceAll("&", "§");
				if (!p.hasPermission(PermissionType.CHAT_NERV.getPermissionToString())) {
					msg = msg.replaceAll("§l", "");
					msg = msg.replaceAll("§n", "");
					msg = msg.replaceAll("§k", "");
				}
			}

			if (UtilServer.getGildenManager() != null && UtilServer.getGildenManager().isPlayerInGilde(p)) {
				GildenManager gildenmanager = UtilServer.getGildenManager();
				String g = gildenmanager.getPlayerGilde(p);
				String tag = gildenmanager.getTag(gildenmanager.getPlayerGilde(p));
				g = g.toLowerCase();
				if (gildenmanager.getExtra_prefix().containsKey(g)) {
					if (gildenmanager.getExtra_prefix().get(g) == 1) {
						tag = tag.replaceAll("§7", "§4§l");
					} else if (gildenmanager.getExtra_prefix().get(g) == 2) {
						tag = tag.replaceAll("§7", "§2§l");
					} else if (gildenmanager.getExtra_prefix().get(g) == 3) {
						tag = tag.replaceAll("§7", "§e§l");
					} else if (gildenmanager.getExtra_prefix().get(g) >= 4 && gildenmanager.getExtra_prefix().get(g) <= 6) {
						tag = tag.replaceAll("§7", "§3");
					} else if (gildenmanager.getExtra_prefix().get(g) >= 7 && gildenmanager.getExtra_prefix().get(g) <= 9) {
						tag = tag.replaceAll("§7", "§d");
					} else if (gildenmanager.getExtra_prefix().get(g) >= 10 && gildenmanager.getExtra_prefix().get(g) <= 12) {
						tag = tag.replaceAll("§7", "§a");
					} else if (gildenmanager.getExtra_prefix().get(g) >= 13 && gildenmanager.getExtra_prefix().get(g) <= 15) {
						tag = tag.replaceAll("§7", "§b");
					}
				}

				event.setFormat(manager.getPrefix(p) + tag + manager.getPrefix(p).subSequence(0, 2) + p.getName() + suffix + (p.hasPermission(PermissionType.SUFFIX.getPermissionToString()) ? (this.userData != null ? (this.userData.getConfig(p).contains("Chat.Suffix") ? this.userData.getConfig(p).getString("Chat.Suffix") : "") : "") : "") + msg);
			} else if(UtilServer.getGildeHandler()!=null&&UtilServer.getGildeHandler().hasGilde(p)){
				String tag = UtilServer.getGildeHandler().getSection(p).getHandle().getShortName();
				tag="§7"+tag+"§e* ";
				event.setFormat(manager.getPrefix(p) + tag + manager.getPrefix(p).subSequence(0, 2) + p.getName() + suffix + (p.hasPermission(PermissionType.SUFFIX.getPermissionToString()) ? (this.userData != null ? (this.userData.getConfig(p).contains("Chat.Suffix") ? this.userData.getConfig(p).getString("Chat.Suffix") : "") : "") : "") + msg);
			}else {
				event.setFormat(manager.getPrefix(p) + p.getName() + suffix + (p.hasPermission(PermissionType.SUFFIX.getPermissionToString()) ? (this.userData != null ? (this.userData.getConfig(p).contains("Chat.Suffix") ? this.userData.getConfig(p).getString("Chat.Suffix") : "") : "") : "") + msg);
			}
		}
	}

	}

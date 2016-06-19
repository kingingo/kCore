package eu.epicpvp.kcore.Listener.Chat.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatFilterListener extends kListener {

	@Getter
	private final PermissionManager manager;
	private final Map<UUID, LastChatMessages> lastChatMessages = new HashMap<>();
	@Getter
	@Setter
	private boolean detectMessageContainingLag = false;
	@Getter
	@Setter
	private int lastMessageAmount = 2;
	@Getter
	@Setter
	private double capsPercentage = .9;
	@Getter
	@Setter
	private int maxSameCharacters = 4;

	public ChatFilterListener(JavaPlugin instance, PermissionManager manager) {
		super(instance, "AntiSpamListener");
		this.manager = manager;
	}

	private LastChatMessages getOrCreateLastChatMessages(UUID uuid) {
		return lastChatMessages.computeIfAbsent(uuid, uuid_ -> new LastChatMessages());
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		String msg = event.getMessage();

		if (ChatUtils.isCaps(msg, capsPercentage)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease deactivate your capslock.");
			event.setCancelled(true);
			return;
		}
		if (detectMessageContainingLag && ChatUtils.isLagMessage(msg)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§7Thanks for the information that the server is lagging. Tell it a team member.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.isZeichenSpam(msg, maxSameCharacters)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not spam.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.isInsult(msg)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cBe moderate while chatting please.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.isInternetAddress(msg)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not advertise.");
			event.setCancelled(true);
			return;
		}
		LastChatMessages lastChatMessages = getOrCreateLastChatMessages(event.getPlayer().getUniqueId());
		if (lastChatMessages.containsOrAddChatMessage(msg, lastMessageAmount)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not spam.");
			event.setCancelled(true);
			return;
		}
		msg = ChatUtils.replaceSpecial(msg);
		event.setMessage(msg);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		lastChatMessages.remove(event.getPlayer().getUniqueId());
	}
}




















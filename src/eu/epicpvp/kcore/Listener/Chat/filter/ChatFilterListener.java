package eu.epicpvp.kcore.Listener.Chat.filter;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

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
	private final Map<UUID, ChatHistory> chatHistories = new HashMap<>();
	@Getter
	@Setter
	private boolean detectLagMessages = false;
	@Getter
	@Setter
	private boolean replaceSpecialChars = false;
	@Getter
	@Setter
	private int chatHistoryLength = 2;
	@Getter
	@Setter
	private double capsPercentage = .9;
	@Getter
	@Setter
	private int maxSameCharacters = 4;
	@Getter
	private static final Set<Pattern> blacklistPatterns = new LinkedHashSet<>();

	public ChatFilterListener(JavaPlugin instance, PermissionManager manager, boolean addDefaultBlacklistPatterns) {
		super(instance, "AntiSpamListener");
		this.manager = manager;
		if (addDefaultBlacklistPatterns) {
			blacklistPatterns.add(Pattern.compile("alpha.*centauri"));
		}
	}

	private ChatHistory getOrCreateChatHistory(UUID uuid) {
		return chatHistories.computeIfAbsent(uuid, uuid_ -> new ChatHistory());
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		String msg = event.getMessage();

		if (ChatUtils.isCaps(msg, capsPercentage)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease deactivate your capslock.");
			event.setCancelled(true);
			return;
		}
		if (detectLagMessages && ChatUtils.isLagMessage(msg)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§7Thanks for the information that the server is lagging. Tell it a team member in a private message or report it on our teamspeak.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.isCharacterSpam(msg, maxSameCharacters)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not spam.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.isInsult(msg)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cBe moderate while chatting please.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.doesMath(msg, blacklistPatterns, true)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not advertise.");
			event.setCancelled(true);
			return;
		}
		if (ChatUtils.isInternetAddress(msg, true)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not advertise.");
			event.setCancelled(true);
			return;
		}
		ChatHistory chatHistory = getOrCreateChatHistory(event.getPlayer().getUniqueId());
		if (chatHistory.containsOrAddChatMessage(msg, chatHistoryLength, true)) {
			event.getPlayer().sendMessage(TranslationHandler.getText(event.getPlayer(), "PREFIX") + "§cPlease do not spam.");
			event.setCancelled(true);
			return;
		}
		if (replaceSpecialChars) {
			msg = ChatUtils.replaceSpecial(msg);
		}
		event.setMessage(msg);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		chatHistories.remove(event.getPlayer().getUniqueId());
	}
}




















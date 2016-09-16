package eu.epicpvp.kcore.Listener.Chat.filter;

import java.util.ArrayList;
import java.util.List;

public class LastChatMessages {
	
	private final List<String> messages = new ArrayList<>();

	/**
	 * @param message the message the player typed
	 * @return whether the message is in the player's last messages
	 */
	public boolean containsOrAddChatMessage(String message, int amountToSave) {
		String lowerCase = message.toLowerCase();
		for (String msg : messages) {
			if (ChatUtils.isSimilar(lowerCase, msg, .8)) {
				return true;
			}
		}
		messages.add(lowerCase);
		removeOldest(amountToSave);
		return false;
	}

	private void removeOldest(int amountToSave) {
		int tooMuchAmount = messages.size() - amountToSave;
		if (tooMuchAmount > 0) {
			for (int i = 0; i < tooMuchAmount; i++) {
				messages.remove(0);
			}
		}
	}
}

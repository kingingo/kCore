package eu.epicpvp.kcore.Gilden.Commands;

import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import org.bukkit.entity.Player;

public class Verlassen {

	public static void use(Player p, String[] args, GildenManager manager) {
		if (args.length == 1) {
			if (manager.isPlayerInGilde(p)) {
				String g = manager.getPlayerGilde(p);
				int owner = manager.getOwner(manager.getPlayerGilde(p));

				if (owner == UtilPlayer.getPlayerId(p)) {
					if (owner != UtilPlayer.getPlayerId(p)) {
						System.err.println("[GildenManager] Command Verlassen: Owner == NULL");
						System.err.println("[GildenManager] Gilde: " + g);
						System.err.println("[GildenManager] Owner-ID: " + owner);
						System.err.println("[GildenManager] Player-ID: " + UtilPlayer.getPlayerId(p));
						return;
					}
					manager.onOwnerLeave(g);

					manager.sendGildenChat(g, "GILDE_CLOSED");
					manager.removeGildenEintrag(p, g);
				} else {
					manager.onMemberLeave(p);

					manager.sendGildenChat(g, "GILDE_PLAYER_GO_OUT", p.getName());
					manager.removePlayerEintrag(p);
					p.setDisplayName(p.getName());
				}
			} else {
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX") + TranslationHandler.getText(p, "GILDE_PLAYER_IS_IN_GILDE"));
			}
		} else {
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX") + " /gilde verlassen");
		}
	}
}

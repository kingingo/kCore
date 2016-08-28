package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;

public class BillGates extends Achievement {

	private GameType type;

	public BillGates(GameType type) {
		this(type, null);
	}

	public BillGates(GameType type, Callback<Integer> done) {
		super("§aBill Gates", Arrays.asList(" ", "§a" + Zeichen.DOUBLE_ARROWS_R.getIcon() + " §7Besitzte 250.000 Epic's", " "), done, false, 1);
		this.type = type;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerStatsChanged(PlayerStatsChangedEvent ev) {
		if (ev.getManager().getType() != type)
			return;
		if (ev.getStats() != StatsKey.MONEY)
			return;
		if (ev.getManager().getDouble(ev.getPlayerId(), ev.getStats()) < 250000)
			return;

		if (this.getPlayerProgress().containsKey(ev.getPlayerId())) {
			this.getPlayerProgress().put(ev.getPlayerId(), getProgress(ev.getPlayerId()) + 1);
			done(ev.getPlayerId());
		}
	}
}

package eu.epicpvp.kcore.Teams.Events;

import javax.annotation.Nullable;

import eu.epicpvp.kcore.Teams.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class TeamPlayerTeleportedEvent extends Event implements Cancellable {
	@Getter
	private final Player player;
	@Getter
	private final Team team;
	@Getter
	@Setter
	private boolean cancelled;
	@Getter
	@Setter
	@Nullable
	private String cancelReason;
	//static things for bukkit events
	@Getter
	private static final HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}

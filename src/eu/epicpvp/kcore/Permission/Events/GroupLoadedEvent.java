package eu.epicpvp.kcore.Permission.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.Group.Group;
import lombok.Getter;

public class GroupLoadedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	@Getter
	private PermissionManager manager;
	@Getter
	private Group group;

	public GroupLoadedEvent(PermissionManager manager, Group group) {
		this.group = group;
		this.manager = manager;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
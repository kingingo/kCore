package eu.epicpvp.kcore.UpdateAsync.Event;

import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateAsyncEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private UpdateAsyncType _type;

	public UpdateAsyncEvent(UpdateAsyncType example) {
		super(true); //set async flag
		this._type = example;
	}

	public UpdateAsyncType getType() {
		return this._type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

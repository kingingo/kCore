package eu.epicpvp.kcore.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.datenclient.client.ClientWrapper;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import lombok.Getter;

public class ServerMessageEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private ClientWrapper client;
	@Getter
	private DataBuffer buffer;
	@Getter
	private String channel;

	public ServerMessageEvent(ClientWrapper client, DataBuffer buffer, String channel){
		this.client=client;
		this.buffer=buffer;
		this.channel=channel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }

}

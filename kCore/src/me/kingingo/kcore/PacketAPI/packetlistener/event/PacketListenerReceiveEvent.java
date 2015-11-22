package me.kingingo.kcore.PacketAPI.packetlistener.event;

import java.lang.reflect.Field;

import me.kingingo.kcore.PacketAPI.packetlistener.Cancellable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketListenerReceiveEvent extends Event {

	private static HandlerList	handlers	= new HandlerList();
	private Player				player;
	private Object				packet;
	private Cancellable			cancel;

	public PacketListenerReceiveEvent(Object packet, Cancellable cancel, Player player) {
		super(true);
		this.player = player;
		this.packet = packet;
		this.cancel = cancel;
	}

	public void setPacketValue(String field, Object value) {
		try {
			Field f = this.packet.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(this.packet, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getPacketValue(String field) {
		Object value = null;
		try {
			Field f = this.packet.getClass().getDeclaredField(field);
			f.setAccessible(true);
			value = f.get(this.packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public void setCancelled(boolean b) {
		this.cancel.setCancelled(b);
	}

	public boolean isCancelled() {
		return this.cancel.isCancelled();
	}

	public Player getPlayer() {
		return this.player;
	}

	public String getPlayername() {
		return this.player.getName();
	}

	public void setPacket(Object packet) {
		this.packet = packet;
	}

	public Object getPacket() {
		return this.packet;
	}

	public String getPacketName() {
		return this.packet.getClass().getSimpleName();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

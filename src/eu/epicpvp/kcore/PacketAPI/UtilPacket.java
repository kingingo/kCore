package eu.epicpvp.kcore.PacketAPI;

import java.lang.reflect.Field;
import java.nio.channels.Channel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.PacketAPI.packetlistener.Cancellable;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.PacketHandler;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.ReceivedPacket;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.SentPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NetworkManager;

public class UtilPacket {
	@Getter
	@Setter
	private static JavaPlugin instance;

	public static int toFixedPoint(double d) {
		return MathHelper.floor(d * 32.0D);
	}
	   
	public static byte toPackedByte(float f) {
		return ((byte)(int)(f * 256.0F / 360.0F));
	}
	
	public static Field getChannelField(Player player){
		Field channelField = null;
		try {
	         for (Field field : NetworkManager.class.getDeclaredFields()) { // Alle Felder durchgehen
	            if (field.getType().isAssignableFrom(Channel.class)) { // wenns 'n Channel ist
	               channelField = field; // lokal speichern
	               channelField.setAccessible(true); // und accessible machen
	               break; // fertig.
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		return channelField;
	}
	
	public static void callAsnycEvent(final Event e) {
		Bukkit.getScheduler().runTaskAsynchronously(instance, new Runnable() {

			@Override
			public void run() {
				try {
					Bukkit.getPluginManager().callEvent(e);
				} catch (IllegalStateException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public static void callEvent(final Event e) {
		try {
			Bukkit.getPluginManager().callEvent(e);
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
		}
	}
	
	public static Object onPacketReceive(Player p, Object packet, Cancellable cancellable) {
		if (!packet.getClass().getName().startsWith("net.minecraft.server.")) return packet;
		
		PacketListenerReceiveEvent event = new PacketListenerReceiveEvent(packet, cancellable, p);
		callEvent(event);

		ReceivedPacket pckt = new ReceivedPacket(packet, cancellable, p);
		PacketHandler.notifyHandlers(pckt);
		return pckt.getPacket();
	}
	
	public static Object onPacketSend(Player p, Object packet, Cancellable cancellable) {
		if (!packet.getClass().getName().startsWith("net.minecraft.server.")) return packet;
		
		PacketListenerSendEvent event = new PacketListenerSendEvent(packet, cancellable, p);
		callEvent(event);
		
		SentPacket pckt = new SentPacket(event.getPacket(), event.getCancel(), event.getPlayer());
		
		PacketHandler.notifyHandlers(pckt);
		return pckt.getPacket();
	}
	
}

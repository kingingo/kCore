package me.kingingo.kcore.PacketAPI;

import java.lang.reflect.Field;
import java.nio.channels.Channel;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Disguise.disguises.livings.DisguiseZombie;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntity;
import me.kingingo.kcore.PacketAPI.packetlistener.Cancellable;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.PacketAPI.packetlistener.handler.PacketHandler;
import me.kingingo.kcore.PacketAPI.packetlistener.handler.ReceivedPacket;
import me.kingingo.kcore.PacketAPI.packetlistener.handler.SentPacket;
import me.kingingo.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

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

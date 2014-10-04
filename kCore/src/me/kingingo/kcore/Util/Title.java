package me.kingingo.kcore.Util;

import net.minecraft.server.v1_7_R4.ChatSerializer;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;
import org.spigotmc.ProtocolInjector.PacketTitle;

public class Title {
	private static int VERSION = 47;

	public static void sendTitle(Player p, String title) {
		if (!(UtilPlayer.getVersion(p) != VERSION)) return;
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, ChatSerializer.a("{\"text\": \"\"}").a(title)));
	}

	public static void sendSubTitle(Player p, String subtitle) {
		if (!(UtilPlayer.getVersion(p) != VERSION)) return;
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, ChatSerializer.a("{\"text\": \"\"}").a(subtitle)));
	}

	public static void sendTimings(Player p, int fadeIn, int stay, int fadeOut) {
		if (!(UtilPlayer.getVersion(p) != VERSION)) return;
		try {
			final Object handle = UtilReflection.getHandle(p);
			final Object connection = UtilReflection.getField(handle.getClass(), "playerConnection").get(handle);
			Object packet = PacketTitle.class.getConstructor(PacketTitle.Action.class, int.class, int.class, int.class).newInstance(PacketTitle.Action.TIMES, fadeIn, stay, fadeOut);
			UtilReflection.getMethod(connection.getClass(), "sendPacket").invoke(connection, packet);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reset(Player p) {
		if (!(UtilPlayer.getVersion(p) != VERSION)) return;
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.RESET));
	}

	public static void clear(Player p) {
		if (!(UtilPlayer.getVersion(p) != VERSION)) return;
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.CLEAR));
	}
}

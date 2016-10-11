package eu.epicpvp.kcore.PacketAPI.packetlistener.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import eu.epicpvp.kcore.PacketAPI.packetlistener.reflection.AccessUtil;
import eu.epicpvp.kcore.PacketAPI.packetlistener.reflection.NMSUtils;


public abstract class PacketHandler {

	private static final List<PacketHandler>	handlers	= new ArrayList<PacketHandler>();

	public static boolean addHandler(PacketHandler handler) {
		boolean b = handlers.contains(handler);
		handlers.add(handler);
		return !b;
	}

	public static boolean removeHandler(PacketHandler handler) {
		return handlers.remove(handler);
	}

	public static void notifyHandlers(SentPacket packet) {
		for (PacketHandler handler : getHandlers()) {
			try {
				PacketOptions options = handler.getClass().getMethod("onSend", SentPacket.class).getAnnotation(PacketOptions.class);
				if (options != null) {
					if (options.forcePlayer() && options.forceServer()) throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
					if (options.forcePlayer()) {
						if (!packet.hasPlayer()) {
							continue;
						}
					} else if (options.forceServer()) {
						if (packet.hasPlayer()) {
							continue;
						}
					}
				}
				handler.onSend(packet);
			} catch (Exception e) {
				System.err.println("[PacketListenerAPI] An exception occured while trying to execute 'onSend'" + (handler.plugin != null ? " in plugin " + handler.plugin.getName() : "") + ": " + e.getMessage());
				e.printStackTrace(System.err);
			}
		}
	}

	public static void notifyHandlers(ReceivedPacket packet) {
		for (PacketHandler handler : getHandlers()) {
			try {
				PacketOptions options = handler.getClass().getMethod("onReceive", ReceivedPacket.class).getAnnotation(PacketOptions.class);
				if (options != null) {
					if (options.forcePlayer() && options.forceServer()) throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
					if (options.forcePlayer()) {
						if (!packet.hasPlayer()) {
							continue;
						}
					} else if (options.forceServer()) {
						if (packet.hasPlayer()) {
							continue;
						}
					}
				}
				handler.onReceive(packet);
			} catch (Exception e) {
				System.err.println("[PacketListenerAPI] An exception occured while trying to execute 'onReceive'" + (handler.plugin != null ? " in plugin " + handler.plugin.getName() : "") + ": " + e.getMessage());
				e.printStackTrace(System.err);
			}
		}
	}

	public static List<PacketHandler> getHandlers() {
		return new ArrayList<>(handlers);
	}

	public static List<PacketHandler> getForPlugin(Plugin plugin) {
		List<PacketHandler> handlers = new ArrayList<>();
		if (plugin == null) return handlers;
		for (PacketHandler h : getHandlers())
			if (plugin.equals(h.getPlugin())) {
				handlers.add(h);
			}
		return handlers;
	}

	// Sending methods
	public void sendPacket(Player p, Object packet) {
		if (p == null || packet == null) throw new NullPointerException("player or packet is null");
		try {
			Object handle = NMSUtils.getHandle(p);
			Object connection = NMSUtils.getField(handle.getClass(), "playerConnection").get(handle);
			NMSUtils.getMethod(connection.getClass(), "sendPacket", NMSUtils.getNMSClass("Packet")).invoke(connection, new Object[] { packet });
		} catch (Exception e) {
			System.err.println("[PacketListenerAPI] Exception while sending " + packet + " to " + p);
			e.printStackTrace();
		}
	}

	public Object cloneObject(Object obj) throws Exception {
		if (obj == null) return obj;
		Object clone = obj.getClass().newInstance();
		for (Field f : obj.getClass().getDeclaredFields()) {
			f = AccessUtil.setAccessible(f);
			f.set(clone, f.get(obj));
		}
		return clone;
	}

	// //////////////////////////////////////////////////

	private Plugin	plugin;

	@Deprecated
	public PacketHandler() {

	}

	public PacketHandler(Plugin plugin) {
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public abstract void onSend(SentPacket packet);

	public abstract void onReceive(ReceivedPacket packet);

}

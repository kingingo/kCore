package eu.epicpvp.kcore.Listener.Command;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayInTabComplete;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;

public class ListenerCMD extends kListener {

	public ListenerCMD(JavaPlugin instance) {
		super(instance, "ListenerCMD");
		org.spigotmc.SpigotConfig.unknownCommandMessage = TranslationHandler.getText("PREFIX") + TranslationHandler.getText("UNKNOWN_CMD");
	}

	String cmd;
	String[] parts;

	@EventHandler
	public void CMD(PlayerCommandPreprocessEvent ev) {
		if (ev.getPlayer().isOp())
			return;
		cmd = "";
		if (ev.getMessage().contains(" ")) {
			parts = ev.getMessage().split(" ");
			cmd = parts[0].toLowerCase();
		} else {
			cmd = ev.getMessage().toLowerCase();
		}

		if (cmd.toLowerCase().equalsIgnoreCase("/plugins")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().equalsIgnoreCase("/pl")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().equalsIgnoreCase("/?")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().equalsIgnoreCase("/help")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().contains("/bukkit:")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().contains("/minecraft:")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().contains("/essentials:")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().contains("/about")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().contains("/ver")) {
			ev.setCancelled(true);
		} else if (cmd.toLowerCase().contains("/version")) {
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void Receive(PacketListenerReceiveEvent ev) {
		if (ev.getPacket() instanceof PacketPlayInTabComplete && ev.getPlayer() != null && !ev.getPlayer().isOp()) {
			try {
				WrapperPacketPlayInTabComplete use = new WrapperPacketPlayInTabComplete(ev.getPacket());
				if (use.getChatMessage().toLowerCase().contains("/?")) {
					ev.setCancelled(true);
				} else if (use.getChatMessage().toLowerCase().contains("/bukkit:")) {
					ev.setCancelled(true);
				} else if (use.getChatMessage().toLowerCase().contains("/minecraft:")) {
					ev.setCancelled(true);
				} else if (use.getChatMessage().toLowerCase().contains("/essentials:")) {
					ev.setCancelled(true);
				} else if (use.getChatMessage().toLowerCase().contains("/about")) {
					ev.setCancelled(true);
				} else if (use.getChatMessage().toLowerCase().contains("/ver")) {
					ev.setCancelled(true);
				} else if (use.getChatMessage().toLowerCase().equalsIgnoreCase("/")) {
					ev.setCancelled(true);
				}
				if(ev.isCancelled())
					UtilPlayer.sendPacket(ev.getPlayer(), new PacketPlayOutTabComplete(new String[]{"§cTabcomplete is not supported for this commands!"}));
			} catch (Exception e) {
				logMessage("Error: ");
				e.printStackTrace();
			}
		}
	}

}

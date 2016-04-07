package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Events.ServerMessageEvent;

public class PermissionChannelHandler implements PluginMessageListener, Listener{
	ArrayList<PermissionChannelListener> listener = new ArrayList<>();
	PermissionManager manager;
	
	public PermissionChannelHandler(PermissionManager manager) {
		this.manager = manager;
	}
	
	public void addListener(PermissionChannelListener listener){
		this.listener.add(listener);
	}
	
	public void removeListener(PermissionChannelListener listener){
		this.listener.remove(listener);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(!channel.equalsIgnoreCase("permission"))
			return;
		DataBuffer buffer = new DataBuffer(data);
		UUID from = buffer.readUUID();
		for(PermissionChannelListener listener : new ArrayList<>(this.listener))
			listener.handle(from, buffer);
	}
	
	public PluginMessageFutureTask<DataBuffer> sendMessage(Player player,DataBuffer buffer){
		if(player == null){
			System.out.println("[PermissionManager]: Cant send plugin message (player == null)");
			return new PluginMessageFutureTask<>();
		}
		final UUID taskId = UUID.randomUUID();

		final PluginMessageFutureTask<DataBuffer> task = new PluginMessageFutureTask<>();
		
		addListener(new PermissionChannelListener() {
			@Override
			public void handle(UUID fromPacket, DataBuffer buffer) {
				if(fromPacket.equals(taskId))
					task.done(buffer);
			}
		});
		sendToBungeecord(player, taskId, buffer);
		return task;
	}
	
	private void sendToBungeecord(Player player,UUID uuid, DataBuffer data) {
		DataBuffer buffer = new DataBuffer();
		buffer.writeUUID(uuid);

		byte[] cbuffer = new byte[data.writerIndex()];
		System.arraycopy(data.array(), 0, cbuffer, 0, data.writerIndex());
		buffer.writeBytes(cbuffer);
		
		byte[] bbuffer = new byte[buffer.writerIndex()];
		System.arraycopy(buffer.array(), 0, bbuffer, 0, buffer.writerIndex());
		
		player.sendPluginMessage(manager.getInstance(), "permission", bbuffer);
	}
	
	@EventHandler
	public void a(ServerMessageEvent e){
		if(e.getChannel().equalsIgnoreCase("permission")){
			byte action = e.getBuffer().readByte();
			if(action == 0)
				manager.updatePlayer(e.getBuffer().readUUID());
			else if(action == 1)
				manager.updateGroup(e.getBuffer().readString());
		}
	}
}

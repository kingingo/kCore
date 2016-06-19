package eu.epicpvp.kcore.Permission;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.spigotmc.AsyncCatcher;

import dev.wolveringer.client.threadfactory.ThreadFactory;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PermissionChannelHandler extends kListener implements PluginMessageListener{
	HashMap<UUID,PermissionChannelListener> listener = new HashMap<>();
	PermissionManager manager;
	
	public PermissionChannelHandler(PermissionManager manager) {
		super(manager.getInstance(),"PermissionChannelHandler");
		this.manager = manager;
	}
	
	public void addListener(UUID taskId,PermissionChannelListener listener){
		this.listener.put(taskId,listener);
	}
	
	public void removeListener(UUID taskId){
		this.listener.remove(taskId);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(!channel.equalsIgnoreCase("permission")) return;
		DataBuffer buffer = new DataBuffer(data);
		UUID from = buffer.readUUID();
		
		ThreadFactory.getFactory().createThread(new Runnable() {
			@Override
			public void run() {
				if(listener.containsKey(from)){
					listener.get(from).handle(from, buffer);
					removeListener(from);
				}
			}
		}).start();
	}
	
	public PluginMessageFutureTask<DataBuffer> sendMessage(Player player,DataBuffer buffer){
		if(player == null){
			logMessage("Cant send plugin message (player == null)");
			return new PluginMessageFutureTask<>();
		}
		final UUID taskId = UUID.randomUUID();

		final PluginMessageFutureTask<DataBuffer> task = new PluginMessageFutureTask<>();
		long start = System.currentTimeMillis();
		addListener(taskId,new PermissionChannelListener() {
			@Override
			public boolean handle(UUID fromPacket, DataBuffer buffer) {
				if(fromPacket.equals(taskId)){
					System.out.println("Request response avariable ("+taskId+" -> "+(System.currentTimeMillis()-start)+"ms)");
					task.done(buffer);
					return true;
				}
				return false;
			}
		});
		System.out.println("Sending message ("+taskId+")");
		sendToBungeecord(player, taskId, buffer);
		return task;
	}
	
	private void sendToBungeecord(Player player,UUID uuid, DataBuffer data) {
		try{
			AsyncCatcher.catchOp("");
		}catch(Exception e){
			Bukkit.getScheduler().runTask(manager.getInstance(), new Runnable() {
				@Override
				public void run() {
					sendToBungeecord(player, uuid, data);
				}
			});
			return;
		}
		DataBuffer buffer = new DataBuffer();
		buffer.writeUUID(uuid);

		byte[] cbuffer = new byte[data.writerIndex()];
		System.arraycopy(data.array(), 0, cbuffer, 0, data.writerIndex());
		buffer.writeBytes(cbuffer);
		
		byte[] bbuffer = new byte[buffer.writerIndex()];
		System.arraycopy(buffer.array(), 0, bbuffer, 0, buffer.writerIndex());
		
		if(UtilPlayer.getCraftPlayer(player).getHandle().playerConnection == null){
			logMessage(player.getName()+" is the playerConnection == NULL!");
		}
		
		if(!UtilPlayer.getCraftPlayer(player).getListeningPluginChannels().contains("permission")){
			UtilPlayer.getCraftPlayer(player).addChannel("permission");
			logMessage("add "+player.getName()+" 'permission' channel!");
		}
		
		player.sendPluginMessage(manager.getInstance(), "permission", bbuffer);
	}
	
	@EventHandler
	public void a(ServerMessageEvent e){
		if(e.getChannel().equalsIgnoreCase("permission")){
			byte action = e.getBuffer().readByte();
			if(action == 0)
				manager.updatePlayer(e.getBuffer().readInt());
			else if(action == 1)
				manager.updateGroup(e.getBuffer().readString());
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent ev){
		manager.unloadPlayer(ev.getPlayer());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void load(PlayerJoinEvent ev){
		Bukkit.getScheduler().runTaskAsynchronously(manager.getInstance(), new Runnable() {
			@Override
			public void run() {
				manager.loadPlayer(ev.getPlayer(), UtilPlayer.getPlayerId(ev.getPlayer()));
				System.out.println("setting player tablist");
				manager.setTabList(ev.getPlayer());
			}
		});
	}
}

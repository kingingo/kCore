package eu.epicpvp.kcore.Translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PluginMessageFutureTask;
import lombok.Getter;

public class TranslationChannelHandler extends kListener implements PluginMessageListener{
	@Getter
	public static DocumentBuilderFactory factory;
	@Getter
	public DocumentBuilder builder;
	@Getter
	public HashMap<Language,Translation> translations;
	@Getter
	private JavaPlugin instance;
	@Getter
	public HashMap<Player,Language> language;
	private ArrayList<TranslationChannelListener> listener;

	public TranslationChannelHandler(JavaPlugin instance) {
		super(instance, "TranslationChannelHandler");
		this.instance=instance;
		this.listener=new ArrayList<>();
		this.factory = DocumentBuilderFactory.newInstance();
		this.language = new HashMap<>();
		this.translations = new HashMap<>();
		
		try {
			this.builder = getFactory().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void addListener(TranslationChannelListener listener){
		this.listener.add(listener);
	}
	
	public void removeListener(TranslationChannelListener listener){
		this.listener.remove(listener);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(!channel.equalsIgnoreCase("language"))
			return;
		DataBuffer buffer = new DataBuffer(data);
		UUID from = buffer.readUUID();
		for(TranslationChannelListener listener : new ArrayList<>(this.listener))
			listener.handle(from, buffer);
	}
	
	public PluginMessageFutureTask<DataBuffer> sendMessage(Player player,DataBuffer buffer){
		if(player == null){
			Log("Cant send plugin message (player == null)");
			return new PluginMessageFutureTask<>();
		}
		final UUID taskId = UUID.randomUUID();

		final PluginMessageFutureTask<DataBuffer> task = new PluginMessageFutureTask<>();
		
		addListener(new TranslationChannelListener() {
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
		
		player.sendPluginMessage(instance, "permission", bbuffer);
	}
	
	private void loadLanguage(Player player){
		Log("Requesting player language");
		DataBuffer buffer = sendMessage(player, new DataBuffer().writeByte(0).writeUUID(player.getUniqueId())).getSync(); //Action: 0 (Get-Perms)
		if(buffer == null){
			System.out.println("Response == null");
			return;
		}
		int length = buffer.readInt();
		if(length == -1){ //Error
			Log("Having an error: "+buffer.readString());
			return;
		}
		
		getLanguage().put(player, Language.values()[buffer.readByte()]);
		Log("Player geladen");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void load(PlayerLoginEvent ev){
		if(getLanguage().containsKey(ev.getPlayer()))return;
		Bukkit.getScheduler().runTaskAsynchronously(instance, new BukkitRunnable() {
			
			@Override
			public void run() {
				loadLanguage(ev.getPlayer());
			}
		});
	}
}

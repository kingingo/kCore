package eu.epicpvp.kcore.Arena;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.event.EventListener;
import dev.wolveringer.events.Event;
import dev.wolveringer.events.EventConditions;
import dev.wolveringer.events.EventType;
import dev.wolveringer.events.player.PlayerServerSwitchEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Listener.SkinCatcherListener.SkinCatcherListener;
import eu.epicpvp.kcore.PacketAPI.Packets.kGameProfile;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import eu.epicpvp.kcore.PacketAPI.Packets.kPlayerInfoData;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilSkin;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class TabManager extends kListener{
	
	@Getter
	private GameType[] types;
	private kPacketPlayOutPlayerInfo info;
	private HashMap<UUID,kPlayerInfoData> players;
	private HashMap<UUID,kPacketPlayOutPlayerInfo> last_packet;
	
	public TabManager(JavaPlugin instance,GameType[] types) {
		super(instance, "TabManager");
		this.types=types;
		this.players=new HashMap<>();
		this.last_packet=new HashMap<>();
		new SkinCatcherListener(instance, TimeSpan.HOUR*4);
		this.info = new kPacketPlayOutPlayerInfo();
		this.info.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
		UtilServer.createPacketListener(instance);
		UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.SERVER_SWITCH).setConditionEnables(EventConditions.GAME_TYPE_ARRAY, true);
		for(GameType type : types)UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.SERVER_SWITCH).getCondition(EventConditions.GAME_TYPE_ARRAY).addValue(type);
		
		UtilServer.getClient().getHandle().getEventManager().registerListener(new EventListener() {
			
			@SuppressWarnings("deprecation")
			public void fireEvent(Event e) {
				if(e instanceof PlayerServerSwitchEvent){
					PlayerServerSwitchEvent ev = (PlayerServerSwitchEvent)e;
					LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(ev.getPlayerId());
					
					if(ev.getFrom() != null && ev.getFrom().startsWith("versus") && ev.getTo()!= null && ev.getTo().startsWith("a")){
						Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, new Runnable() {
							
							@Override
							public void run() {
								if(!UtilPlayer.isOnline(loadedplayer.getName())){
									kPacketPlayOutPlayerInfo add = new kPacketPlayOutPlayerInfo();
									add.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
									
									add.getList().add(new kPlayerInfoData(add,EnumGamemode.SPECTATOR, new kGameProfile(loadedplayer.getUUID(),loadedplayer.getName(),(UtilSkin.getCatcher().getSkins().containsKey(loadedplayer.getName().toLowerCase()) ? UtilSkin.getCatcher().getSkins().get(loadedplayer.getName().toLowerCase()) : null)), (UtilSkin.getCatcher().getTabnames().containsKey(loadedplayer.getUUID()) ? UtilSkin.getCatcher().getTabnames().get(loadedplayer.getUUID())+loadedplayer.getName() : "§7"+loadedplayer.getName())));
									for(Player player : UtilServer.getPlayers()){
										UtilPlayer.sendPacket(player, add);
									}
									last_packet.put(loadedplayer.getUUID(), add);
								}
							}
						}, 10);
						
						players.put(loadedplayer.getUUID(), new kPlayerInfoData(info,EnumGamemode.SPECTATOR, new kGameProfile(loadedplayer.getUUID(),loadedplayer.getName(),(UtilSkin.getCatcher().getSkins().containsKey(loadedplayer.getName().toLowerCase()) ? UtilSkin.getCatcher().getSkins().get(loadedplayer.getName().toLowerCase()) : null)),(UtilSkin.getCatcher().getTabnames().containsKey(loadedplayer.getUUID()) ? UtilSkin.getCatcher().getTabnames().get(loadedplayer.getUUID())+loadedplayer.getName() : "§7"+loadedplayer.getName())));
						info.getList().add(players.get(loadedplayer.getUUID()));
					}else if(ev.getFrom() != null && ev.getFrom().startsWith("a") && players.containsKey(loadedplayer.getUUID())){
						kPacketPlayOutPlayerInfo remove = last_packet.get(loadedplayer.getUUID());
						remove.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
						for(Player player : UtilServer.getPlayers())UtilPlayer.sendPacket(player, remove);
						info.getList().remove(players.get(loadedplayer.getUUID()));
						players.remove(loadedplayer.getUUID());
						last_packet.remove(loadedplayer.getUUID());
					}
				}
			}
		});
		UtilServer.setTabManager(this);
	}
	
	public int getSize(){
		return players.size();
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		UtilPlayer.sendPacket(ev.getPlayer(), info);
	}
}

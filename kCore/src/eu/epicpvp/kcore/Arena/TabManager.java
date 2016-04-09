package eu.epicpvp.kcore.Arena;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.event.EventListener;
import dev.wolveringer.events.Event;
import dev.wolveringer.events.EventConditions;
import dev.wolveringer.events.EventType;
import dev.wolveringer.events.player.PlayerServerSwitchEvent;
import dev.wolveringer.skin.Skin;
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

public class TabManager extends kListener{
	
	@Getter
	private GameType[] types;
	private kPacketPlayOutPlayerInfo info;
	private HashMap<UUID,kPlayerInfoData> players;
	
	public TabManager(JavaPlugin instance,GameType[] types) {
		super(instance, "TabManager");
		this.types=types;
		new SkinCatcherListener(instance, TimeSpan.HOUR*4);
		this.info = new kPacketPlayOutPlayerInfo();
		this.info.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
		UtilServer.createPacketListener(instance);
		UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.SERVER_SWITCH).setConditionEnables(EventConditions.GAME_TYPE_ARRAY, true);
		for(GameType type : types)UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.SERVER_SWITCH).getCondition(EventConditions.GAME_TYPE_ARRAY).addValue(type);
		
		UtilServer.getClient().getHandle().getEventManager().registerListener(new EventListener() {
			
			@Override
			public void fireEvent(Event e) {
				if(e instanceof PlayerServerSwitchEvent){
					PlayerServerSwitchEvent ev = (PlayerServerSwitchEvent)e;
					LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(ev.getPlayer());
					Log("FIRE "+loadedplayer.getName());
					
					if(ev.getFrom().startsWith("versushub") && ev.getTo()!= null && ev.getTo().startsWith("a")){
						UtilSkin.loadSkin(new Callback<Skin>() {

							@Override
							public void call(Skin data) {
								players.put(loadedplayer.getUUID(), new kPlayerInfoData(info, new kGameProfile(loadedplayer.getUUID(),loadedplayer.getName(),data), "§7"+loadedplayer.getName()));
								info.getList().add(players.get(loadedplayer.getUUID()));
								Log("ADD "+loadedplayer.getName());
							}
						}, loadedplayer.getUUID());
					}else if(ev.getFrom().startsWith("a") && players.containsKey(loadedplayer.getUUID())){
						kPacketPlayOutPlayerInfo remove = new kPacketPlayOutPlayerInfo();
						remove.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
						
						remove.getList().add(new kPlayerInfoData(remove, new kGameProfile(loadedplayer.getUUID(),loadedplayer.getName(),(UtilSkin.getCatcher().getSkins().containsKey(loadedplayer.getName().toLowerCase()) ? UtilSkin.getCatcher().getSkins().get(loadedplayer.getName().toLowerCase()) : null)), "§7"+loadedplayer.getName()));
						for(Player player : UtilServer.getPlayers())UtilPlayer.sendPacket(player, remove);
						
						info.getList().remove(players.get(loadedplayer.getUUID()));
						players.remove(loadedplayer.getUUID());
						
						Log("REMOVE "+loadedplayer.getName());
					}
				}
			}
		});
		UtilServer.setTabManager(this);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		UtilPlayer.sendPacket(ev.getPlayer(), info);
	}
}

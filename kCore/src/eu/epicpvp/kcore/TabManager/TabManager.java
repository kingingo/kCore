package eu.epicpvp.kcore.TabManager;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.futures.ServerStatusResponseFuture;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutServerStatus;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.Packets.kGameProfile;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import eu.epicpvp.kcore.PacketAPI.Packets.kPlayerInfoData;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class TabManager extends kListener{
	
	@Getter
	private List<String> list_players;
	@Getter
	private GameType[] types;
	private kPacketPlayOutPlayerInfo info;
	private kPacketPlayOutPlayerInfo add;
	private kPacketPlayOutPlayerInfo del;
	
	public TabManager(JavaPlugin instance,GameType[] types) {
		super(instance, "TabManager");
		this.types=types;
		this.info = new kPacketPlayOutPlayerInfo();
		this.info.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
		this.add = new kPacketPlayOutPlayerInfo();
		this.add.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
		this.del = new kPacketPlayOutPlayerInfo();
		this.del.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
		UtilServer.createPacketListener(instance);
	}
	
	@EventHandler
	public void updater(UpdateAsyncEvent ev){
		if(ev.getType()==UpdateAsyncType.SLOWEST){
			ServerStatusResponseFuture response = UtilServer.getClient().getGameTypeServerStatus(getTypes(), true);
			response.getAsync(new Callback<PacketOutServerStatus>() {
				
				@Override
				public void call(PacketOutServerStatus packet) {
					if(packet!=null){
						add.getList().clear();
						del.getList().clear();
						
						for(String player : packet.getPlayers()){
							if(!list_players.contains(player)){
								add.getList().add(new kPlayerInfoData(add, new kGameProfile(UUID.randomUUID(), player), player));
								info.getList().add(new kPlayerInfoData(info, new kGameProfile(UUID.randomUUID(), player), player));
							}
						}
						
						for(String player : list_players){
							if(!packet.getPlayers().contains(player)){
								del.getList().add(new kPlayerInfoData(del, new kGameProfile(UUID.randomUUID(), player), player));
							}
						}
						
						for(PlayerInfoData playerinfo : del.getList()){
							if(playerinfo instanceof kPlayerInfoData){
								list_players.remove( ((kPlayerInfoData)playerinfo).getGameProfile().getName() );
								info.getList().remove(playerinfo);
							}
						}
						
						for(Player player : UtilServer.getPlayers()){
							UtilPlayer.sendPacket(player, del);
							UtilPlayer.sendPacket(player, add);
						}
					}
				}
			});
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		UtilPlayer.sendPacket(ev.getPlayer(), info);
	}
}

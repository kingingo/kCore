package eu.epicpvp.kcore.Listener.ConvertListener;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.google.common.base.Charsets;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.Action;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.EditStats;
import dev.wolveringer.dataserver.protocoll.packets.PacketServerMessage;
import dev.wolveringer.gamestats.Statistic;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.Events.PlayerLoadPermissionEvent;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class ConvertListener extends kListener{

	private class OldPermission{
		private String perm;
		private GroupTyp typ;
		
		public OldPermission(String perm,GroupTyp typ){
			this.perm=perm;
			this.typ=typ;
		}
	}
	@Getter
	private MySQL mysql;
	private StatsManager money;
	
	public ConvertListener(StatsManager money) {
		super(money.getInstance(), "ConvertListener");
		this.mysql=new MySQL("root", "55P_YHmK8MXlPiqEpGKuH_5WVlhsXT", "mysql.connect-handler.net", "convert", money.getInstance());
		this.money=money;
	}
	
	@EventHandler
	public void perm(PlayerLoadPermissionEvent ev){
		String pgroup = "default";
		ArrayList<OldPermission> perms = null;
		
		try {
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE prefix='none' AND permission='none' AND pgroup!='none' AND uuid='"+getRealUUID(ev.getPlayer())+"' AND grouptyp='"+GroupTyp.ALL.name()+"'");

			while (rs.next()) {
				pgroup=rs.getString(1).toLowerCase();
			}

			rs.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		try {
			ResultSet rs = mysql.Query("SELECT permission, grouptyp FROM game_perm WHERE permission!='none' AND uuid='"+getRealUUID(ev.getPlayer())+"' AND prefix='none' AND pgroup='none';");
		     
			while (rs.next()) {
				if(perms == null)perms=new ArrayList<>();
				perms.add(new OldPermission(rs.getString(1), GroupTyp.get(rs.getString(2))));
			}

			rs.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		if(!pgroup.equalsIgnoreCase("default")){
			for(Group group : UtilServer.getPermissionManager().getPermissionPlayer(ev.getPlayer()).getGroups()){
				if(group.getName().equalsIgnoreCase(pgroup)){
					pgroup="default";
				}
			}
			
			if(!pgroup.equalsIgnoreCase("default")){
				setGroup(UtilServer.getClient().getPlayerAndLoad(ev.getPlayer().getName()), pgroup);
				logMessage("Set Right Group "+pgroup+" from "+ev.getPlayer().getName());
			}
			mysql.Delete("game_perm", "uuid='"+getRealUUID(ev.getPlayer())+"' AND pgroup!='none';");
		}
		
		if(perms!=null){
			for(OldPermission perm : perms){
				UtilServer.getPermissionManager().getPermissionPlayer(ev.getPlayer()).addPermission(perm.perm, perm.typ);
			}
			
			mysql.Delete("game_perm", "uuid='"+getRealUUID(ev.getPlayer())+"'");
		}
	}
	
	@EventHandler
	public void statsLoaded(PlayerStatsLoadedEvent ev){
		if(ev.getManager().getType()==GameType.Money && UtilPlayer.isOnline(ev.getPlayerId())){
			Player player = (UtilPlayer.searchExact(ev.getPlayerId()));
			
			Integer gems = mysql.getInt("SELECT gems FROM gems_list WHERE uuid='" + getRealUUID(player) + "'");
			Integer coins = mysql.getInt("SELECT coins FROM coins_list WHERE uuid='" + getRealUUID(player) + "'");
			Double pvp_money = mysql.getDouble("SELECT money FROM users_PvP WHERE uuid='" + getRealUUID(player) + "'");
			Double sky_money = mysql.getDouble("SELECT money FROM users_Sky WHERE uuid='" + getRealUUID(player) + "'");
			
			if(gems!=null && gems!=0){
				if(money.getInt(player, StatsKey.GEMS)<gems){
					money.set(player, StatsKey.GEMS, gems);
					logMessage("Set Right Gems "+gems+" from "+player.getName());
				}
				mysql.Delete("gems_list", "uuid='"+getRealUUID(player)+"'");
			}
			
			if(coins!=null && coins!=0){
				if(money.getInt(player, StatsKey.COINS)<coins){
					money.set(player, StatsKey.COINS, coins);
					logMessage("Set Right Coins "+coins+" from "+player.getName());
				}
				mysql.Delete("coins_list", "uuid='"+getRealUUID(player)+"'");
			}
			
			if(pvp_money!=null && pvp_money!=0){
				UtilServer.getClient().getPlayerAndLoad(player.getName()).getStats(GameType.PVP).getAsync(new Callback<Statistic[]>() {

					@Override
					public void call(Statistic[] obj) {
						for (Statistic s : obj) {
							if(s.getStatsKey() == StatsKey.MONEY){
								if(UtilNumber.toDouble(s.getValue()) < pvp_money){
									UtilServer.getClient().getPlayerAndLoad(player.getName()).setStats(new EditStats(GameType.PVP, Action.SET, StatsKey.MONEY, pvp_money));
									logMessage("Set Right PvP Money "+pvp_money+" from "+player.getName());
								}
								break;
							}
						}
					}
				});
				mysql.Delete("users_PvP", "uuid='"+getRealUUID(player)+"'");
			}
			
			if(sky_money!=null && sky_money!=0){
				UtilServer.getClient().getPlayerAndLoad(player.getName()).getStats(GameType.SKYBLOCK).getAsync(new Callback<Statistic[]>() {

					@Override
					public void call(Statistic[] obj) {
						for (Statistic s : obj) {
							if(s.getStatsKey() == StatsKey.MONEY){
								if(UtilNumber.toDouble(s.getValue()) < sky_money){
									UtilServer.getClient().getPlayerAndLoad(player.getName()).setStats(new EditStats(GameType.SKYBLOCK, Action.SET, StatsKey.MONEY, sky_money));
									logMessage("Set Right Sky Money "+sky_money+" from "+player.getName());
								}
								break;
							}
						}
					}
				});
				mysql.Delete("users_Sky", "uuid='"+getRealUUID(player)+"'");
			}
		}
	}

	public void setGroup(LoadedPlayer loadedplayer, String toGroup){
		DataBuffer buffer = new DataBuffer();
		buffer.writeByte(2);
		buffer.writeInt(loadedplayer.getPlayerId());
		buffer.writeString(null);
		buffer.writeString(toGroup);
		PacketServerMessage packet = new PacketServerMessage("permission", ClientType.BUNGEECORD, buffer);

		UtilReflection.setValue("targets", packet, new PacketServerMessage.Target[]{new PacketServerMessage.Target(ClientType.BUNGEECORD,"targetlimit;1")});

		UtilServer.getClient().writePacket(packet);
		System.out.println("[OLD]: Set "+loadedplayer.getName()+"("+loadedplayer.getPlayerId()+") to "+toGroup);
	}
	
	public void check(Player player){
		
		
	}
	
	public static UUID getRealUUID(Player player){
		UUID uuid = player.getUniqueId();
		if(UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player.getName()).toString().getBytes(Charsets.UTF_8)).equals(uuid)){
			uuid=getOfflineUUID(player.getName().toLowerCase());
		}
		return uuid;
	}
	
	public static UUID getOfflineUUID(String player){
		return UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player.toLowerCase()).toString().getBytes(Charsets.UTF_8));
	}

}

package eu.epicpvp.kcore.Command.Admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.Action;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.EditStats;
import dev.wolveringer.dataserver.protocoll.packets.PacketServerMessage;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandConvert implements CommandExecutor{
	
	private class OldPermission{
		private String perm;
		private GroupTyp typ;
		
		public OldPermission(String perm,GroupTyp typ){
			this.perm=perm;
			this.typ=typ;
		}
	}
	
	private class OldData{
		private String pgroup="default";
		private int coins=0;
		private int gems=0;
		private double pvp_money=0;
		private double sky_money=0;
		private int playerId;
		private ArrayList<OldPermission> perms = new ArrayList<>();
	}
	
	private JavaPlugin instance;
	private StatsManager money;
	private MySQL mysql;
	private HashMap<Player,OldData> list;
	private MySQL m;
	
	public CommandConvert(StatsManager money,MySQL m, MySQL m1){
		this.instance=money.getInstance();
		this.list=new HashMap<>();
		this.money=money;
		this.m=m;
		this.mysql=m1;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "old",alias={"old"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(!player.hasPermission("epicpvp.bg.kinfo")){
			System.out.println("NO!!" + UtilServer.getPermissionManager().hasPermission(player, "epicpvp.bg.kinfo"));
			
			
			return false;
		}
		if(args.length==0){
			player.sendMessage("§a/old convert [Spieler]");
		}else{
			if(args[0].equalsIgnoreCase("convert")){
				if(UtilPlayer.isOnline(args[1])){
					Player convert = Bukkit.getPlayer(args[1]);
					
					if(!list.containsKey(convert)){
						OldData old = loadOldData(convert);
						old.playerId = UtilPlayer.getPlayerId(convert);
						list.put(convert, old);
						
						for(OldPermission perm : old.perms){
							player.sendMessage("§aPerm: §e"+perm.perm+" "+perm.typ.name());
						}
						
						player.sendMessage("§aCoins: §e"+old.coins);
						player.sendMessage("§aGems: §e"+old.gems);
						player.sendMessage("§aPvP Money: §e"+old.pvp_money);
						player.sendMessage("§aSkyBlock Money: §e"+old.sky_money);
						player.sendMessage("§aGroup: §e"+old.pgroup);
						player.sendMessage("§amach §e'/old right "+convert.getName()+"'§a um die Daten zu übernehmen!");
					}else{
						player.sendMessage("§cDieser Spieler ist schon convertiert mach /old right [Spieler] §l!");
					}
				}else{
					player.sendMessage("§cDer Spieler "+args[1]+" ist nicht auf diesen Server§l!");
				}
			}else if(args[0].equalsIgnoreCase("right")){
				if(UtilPlayer.isOnline(args[1])){
					Player convert = Bukkit.getPlayer(args[1]);
					
					if(list.containsKey(convert)){
						OldData old = list.get(convert);
						if(old.gems!=0)money.set(old.playerId, StatsKey.GEMS, old.gems);
						if(old.coins!=0)money.set(old.playerId, StatsKey.COINS, old.coins);
						LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(old.playerId);
						if(old.pvp_money!=0&&old.sky_money!=0)loadedplayer.setStats(new EditStats[]{new EditStats(GameType.PVP, Action.SET, StatsKey.MONEY, old.pvp_money),new EditStats(GameType.SKYBLOCK, Action.SET, StatsKey.MONEY, old.sky_money)});
						
						if(!old.pgroup.equalsIgnoreCase("default"))setGroup(loadedplayer, old.pgroup);
						
						for(OldPermission p : old.perms){
							m.Update("INSERT INTO game_perm (prefix,permission,pgroup,grouptyp,playerId) values ('none','"+p.perm+"','none','"+p.typ.name().toLowerCase()+"','"+old.playerId+"');");
						}
						
						mysql.Delete("gems_list", "uuid='"+getRealUUID(player)+"'");
						mysql.Delete("coins_list", "uuid='"+getRealUUID(player)+"'");
						mysql.Delete("users_PvP", "uuid='"+getRealUUID(player)+"'");
						mysql.Delete("users_Sky", "uuid='"+getRealUUID(player)+"'");
						mysql.Delete("game_perm", "uuid='"+getRealUUID(player)+"'");
						list.remove(convert);
						player.sendMessage("§aDie Spieler daten wurden convertiert!");
					}else{
						player.sendMessage("§cDieser Spieler ist nicht convertiert§l!");
					}
				}
			}
		}
		return false;
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
	
	public OldData loadOldData(Player player){
		OldData old = new OldData();
		
		old.gems=mysql.getInt("SELECT gems FROM gems_list WHERE uuid='" + getRealUUID(player) + "'");
		old.coins=mysql.getInt("SELECT coins FROM coins_list WHERE uuid='" + getRealUUID(player) + "'");
		old.pvp_money=mysql.getDouble("SELECT money FROM users_PvP WHERE uuid='" + getRealUUID(player) + "'");
		old.sky_money=mysql.getDouble("SELECT money FROM users_Sky WHERE uuid='" + getRealUUID(player) + "'");
		
		try {
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE prefix='none' AND permission='none' AND pgroup!='none' AND uuid='"+getRealUUID(player)+"' AND grouptyp='"+GroupTyp.ALL.name()+"'");

			while (rs.next()) {
				old.pgroup=rs.getString(1).toLowerCase();
			}

			rs.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		try {
			ResultSet rs = mysql.Query("SELECT permission, grouptyp FROM game_perm WHERE permission!='none' AND uuid='"+getRealUUID(player)+"' AND prefix='none' AND pgroup='none';");
		     
			while (rs.next()) {
				old.perms.add(new OldPermission(rs.getString(1), GroupTyp.get(rs.getString(2))));
			}

			rs.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return old;
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


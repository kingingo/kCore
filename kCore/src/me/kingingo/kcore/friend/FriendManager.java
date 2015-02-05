package me.kingingo.kcore.friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FriendManager implements Listener {

	@Getter
	CommandHandler cmd;
	@Getter
	private JavaPlugin instance;
	@Getter
	private MySQL mysql;
	@Getter
	HashMap<UUID,List<UUID>> FriendList = new HashMap<>();
	@Getter
	HashMap<Player,Player> anfrage = new HashMap<>();
	@Getter
	HashMap<Player,Player> del_friend = new HashMap<>();
	@Getter
	HashMap<Player,Integer> del_friend_timer = new HashMap<>();
	
	public FriendManager(JavaPlugin plugin,MySQL mysql,CommandHandler cmd){
		this.instance=plugin;
		this.cmd=cmd;
		this.mysql=mysql;
		cmd.register(CommandFriend.class, new CommandFriend(this));
		Bukkit.getPluginManager().registerEvents(this, getInstance());
		mysql.Update("CREATE TABLE IF NOT EXISTS list_friend (player_uuid varchar(100),friede_uuid varchar(100))");
	}
	
	int i;
	Player a;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(del_friend.isEmpty())return;
		for(Player f : del_friend.keySet()){
			if(!del_friend.containsKey(f))continue;
			i=del_friend_timer.get(f);
			del_friend_timer.remove(f);
			a=del_friend.get(f);
			if(i==0){
				del_friend.remove(f);
				DelFriend(f,a);
				if(getFriendList().containsKey(UtilPlayer.getRealUUID(f)))getFriendList().remove(UtilPlayer.getRealUUID(f));
				if(getFriendList().containsKey(UtilPlayer.getRealUUID(a)))getFriendList().remove(UtilPlayer.getRealUUID(a));
				continue;
			}
			if(f.isOnline())f.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_DEL_IN.getText(new String[]{String.valueOf(i),a.getName()}));
			if(a.isOnline())a.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_DEL_IN.getText(new String[]{String.valueOf(i),f.getName()}));
			i--;
			del_friend_timer.put(f, i);
		}
		
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(getFriendList().containsKey(ev.getPlayer())){
			getFriendList().remove(ev.getPlayer());
		}
	}
	
	public List<UUID> getFriendList(Player p){
		List<UUID> list = new ArrayList<UUID>();
		try {
			ResultSet rs = getMysql().Query("SELECT friede_uuid FROM list_friend WHERE player_uuid='"+UtilPlayer.getRealUUID(p)+"'");
			while(rs.next()){
				list.add(UUID.fromString(rs.getString(1)));
			}
			rs.close();
		} catch (SQLException e) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY, e,getMysql()));
		}
		return list;
	}
	
	public void addFriend(Player p, Player p1){
		if(!getFriendList().containsKey(p))getFriendList().put(UtilPlayer.getRealUUID(p), getFriendList(p));
		getMysql().Update("INSERT INTO list_friend (player_uuid,friede_uuid) VALUES ('"+UtilPlayer.getRealUUID(p)+"','"+UtilPlayer.getRealUUID(p1)+"')");
		getMysql().Update("INSERT INTO list_friend (player_uuid,friede_uuid) VALUES ('"+UtilPlayer.getRealUUID(p1)+"','"+UtilPlayer.getRealUUID(p)+"')");
		
		getFriendList().get(UtilPlayer.getRealUUID(p)).add(UtilPlayer.getRealUUID(p1));
		if(getFriendList().containsKey(UtilPlayer.getRealUUID(p1)))getFriendList().get(UtilPlayer.getRealUUID(p1)).add(UtilPlayer.getRealUUID(p));
	}
	
	public void DelFriend(Player p, Player p1){
		getMysql().Update("DELETE FROM list_friend WHERE player_uuid='" + UtilPlayer.getRealUUID(p) + "' AND friede_uuid='"+UtilPlayer.getRealUUID(p1)+"'");
		getMysql().Update("DELETE FROM list_friend WHERE player_uuid='" + UtilPlayer.getRealUUID(p1) + "' AND friede_uuid='"+UtilPlayer.getRealUUID(p)+"'");
	}
	
	public void DelFriend(Player p, UUID p1){
		getMysql().Update("DELETE FROM list_friend WHERE player_uuid='" + UtilPlayer.getRealUUID(p) + "' AND friede_uuid='"+p1+"'");
		getMysql().Update("DELETE FROM list_friend WHERE player_uuid='" + p1 + "' AND friede_uuid='"+UtilPlayer.getRealUUID(p)+"'");
	}
	
	Player attack;
	Player defend;
	Projectile attack_projectile;
	@EventHandler(priority=EventPriority.HIGH)
	public void DamageTeam(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player){
			attack = (Player)ev.getDamager();
			defend = (Player)ev.getEntity();
			if(!getFriendList().containsKey(UtilPlayer.getRealUUID(attack)))getFriendList().put(UtilPlayer.getRealUUID(attack), getFriendList(attack));
			if(!getFriendList().containsKey(UtilPlayer.getRealUUID(defend)))getFriendList().put(UtilPlayer.getRealUUID(defend), getFriendList(defend));
			
			if(getFriendList().containsKey(UtilPlayer.getRealUUID(attack))&&getFriendList().get(UtilPlayer.getRealUUID(attack)).contains(UtilPlayer.getRealUUID(defend))){
				if(getFriendList().containsKey(UtilPlayer.getRealUUID(defend))&&getFriendList().get(UtilPlayer.getRealUUID(defend)).contains(UtilPlayer.getRealUUID(attack))){
					ev.setCancelled(true);
					attack.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_HIT.getText());
				}
			}
		}else if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Projectile){
			attack_projectile = (Projectile)ev.getDamager();
			if(!(attack instanceof Arrow))return;
			defend = (Player)ev.getEntity();
			if(!(attack_projectile.getShooter() instanceof Player))return;
			if(!getFriendList().containsKey(UtilPlayer.getRealUUID(((Player)attack_projectile.getShooter()))))getFriendList().put(UtilPlayer.getRealUUID(((Player)attack_projectile.getShooter())), getFriendList(((Player)attack_projectile.getShooter())));
			if(!getFriendList().containsKey(UtilPlayer.getRealUUID(defend)))getFriendList().put(UtilPlayer.getRealUUID(defend), getFriendList(defend));
			
			if(getFriendList().containsKey(UtilPlayer.getRealUUID(((Player)attack_projectile.getShooter())))&&getFriendList().get(UtilPlayer.getRealUUID(((Player)attack_projectile.getShooter()))).contains(UtilPlayer.getRealUUID(defend))){
				if(getFriendList().containsKey(UtilPlayer.getRealUUID(defend))&&getFriendList().get(UtilPlayer.getRealUUID(defend)).contains(UtilPlayer.getRealUUID(((Player)attack_projectile.getShooter())))){
					ev.setCancelled(true);
					((Player)attack_projectile.getShooter()).sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_HIT.getText());
				}
			}
		}
	}
	
}

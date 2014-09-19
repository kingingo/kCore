package me.kingingo.kcore.friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
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
	HashMap<String,List<String>> FriendList = new HashMap<>();
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
		mysql.Update("CREATE TABLE IF NOT EXISTS list_friend (player varchar(30),friede varchar(30))");
	}
	
	int i;
	Player a;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(del_friend_timer.isEmpty())return;
		for(Player f : del_friend_timer.keySet()){
			if(!del_friend.containsKey(f))continue;
			i=del_friend_timer.get(f);
			a=del_friend.get(f);
			if(i==0){
				del_friend.remove(f);
				DelFriend(f.getName(), a.getName());
				if(f.isOnline()){
					if(getFriendList().containsKey(f.getName().toLowerCase()))getFriendList().remove(f.getName().toLowerCase());
				}
				if(a.isOnline()){
					if(getFriendList().containsKey(a.getName().toLowerCase()))getFriendList().remove(f.getName().toLowerCase());
				}
				continue;
			}
			if(f.isOnline())f.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_DEL_IN.getText(new String[]{String.valueOf(i),a.getName()}));
			if(a.isOnline())a.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_DEL_IN.getText(new String[]{String.valueOf(i),f.getName()}));
			i--;
		}
		
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(getFriendList().containsKey(ev.getPlayer())){
			getFriendList().remove(ev.getPlayer());
		}
	}
	
	public List<String> getFriendList(Player p){
		List<String> list = new ArrayList<String>();
		try {
			ResultSet rs = getMysql().Query("SELECT friede FROM list_friend WHERE player='"+p.getName().toLowerCase()+"'");
			while(rs.next()){
				list.add(rs.getString(1).toLowerCase());
			}
			rs.close();
		} catch (SQLException e) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY, e,getMysql()));
		}
		return list;
	}
	
	public void addFriend(Player p, String p1){
		if(!getFriendList().containsKey(p))getFriendList().put(p.getName().toLowerCase(), getFriendList(p));
		getMysql().Update("INSERT INTO list_friend (player,friede) VALUES ('"+p.getName().toLowerCase()+"','"+p1.toLowerCase()+"')");
		getMysql().Update("INSERT INTO list_friend (player,friede) VALUES ('"+p1.toLowerCase()+"','"+p.getName().toLowerCase()+"')");
		
		getFriendList().get(p.getName().toLowerCase()).add(p1.toLowerCase());
		if(getFriendList().containsKey(p1.toLowerCase()))getFriendList().get(p1.toLowerCase()).add(p.getName().toLowerCase());
	}
	
	public void DelFriend(String p, String p1){
		getMysql().Update("DELETE FROM list_friend WHERE player='" + p.toLowerCase() + "' AND friede='"+p1.toLowerCase()+"'");
		getMysql().Update("DELETE FROM list_friend WHERE player='" + p1.toLowerCase() + "' AND friede='"+p.toLowerCase()+"'");
	}
	
	public void DelFriend(Player p, String p1){
		if(!getFriendList().containsKey(p))getFriendList().put(p.getName().toLowerCase(), getFriendList(p));
		getMysql().Update("DELETE FROM list_friend WHERE player='" + p.getName().toLowerCase() + "' AND friede='"+p1.toLowerCase()+"'");
		getMysql().Update("DELETE FROM list_friend WHERE player='" + p1.toLowerCase() + "' AND friede='"+p.getName().toLowerCase()+"'");
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void DamageTeam(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player){
			Player attack = (Player)ev.getDamager();
			Player defend = (Player)ev.getEntity();
			if(!getFriendList().containsKey(attack))getFriendList().put(attack.getName().toLowerCase(), getFriendList(attack));
			if(!getFriendList().containsKey(defend))getFriendList().put(defend.getName().toLowerCase(), getFriendList(defend));
			if(getFriendList().containsKey(attack)&&getFriendList().get(attack).contains(defend.getName())){
				if(getFriendList().containsKey(defend)&&getFriendList().get(defend).contains(attack.getName().toLowerCase())){
					ev.setCancelled(true);
				}
			}
		}else if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Projectile){
			Projectile attack = (Projectile)ev.getDamager();
			Player defend = (Player)ev.getEntity();
			if(!(attack.getShooter() instanceof Player))return;
			if(!getFriendList().containsKey(attack))getFriendList().put(((Player)attack.getShooter()).getName().toLowerCase(), getFriendList(((Player)attack.getShooter())));
			if(!getFriendList().containsKey(defend))getFriendList().put(defend.getName().toLowerCase(), getFriendList(defend));
			if(getFriendList().containsKey(((Player)attack.getShooter()))&&getFriendList().get(((Player)attack.getShooter())).contains(defend.getName())){
				if(getFriendList().containsKey(defend)&&getFriendList().get(defend).contains(((Player)attack.getShooter()).getName().toLowerCase())){
					ev.setCancelled(true);
				}
			}
		}
	}
	
}

package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Calendar.Calendar;
import me.kingingo.kcore.Calendar.Calendar.CalendarType;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.Callback;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.GIVE_COINS;
import me.kingingo.kcore.Packet.Packets.PLAYER_ONLINE;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

public class Coins extends kListener{
	@Getter
	private MySQL mysql;
	@Getter
	private HashMap<UUID,Integer> coins = new HashMap<>();
	private ArrayList<UUID> change_coins = new ArrayList<>();
	private HashMap<String,Long> give_coins_time = new HashMap<>();
	private HashMap<String,Integer> give_coins = new HashMap<>();
	private ItemStack item;
	@Getter
	@Setter
	private boolean join_Check=true;
	@Getter
	@Setter
	private boolean async=false;
	
	public Coins(JavaPlugin instance,MySQL mysql){
		super(instance,"Coins");
		this.mysql=mysql;
		this.item=UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aCoins-Bottle");
	}
	
	public void SaveAll(){
		for(UUID uuid : coins.keySet()){
			if(change_coins.contains(uuid))
				if(isAsync()){
					mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+coins.get(uuid)+"' WHERE uuid='"+uuid+"'");
				}else{
					mysql.Update("UPDATE `coins_list` SET coins='"+coins.get(uuid)+"' WHERE uuid='"+uuid+"'");
				}
		}
		coins.clear();
		change_coins.clear();
	}
	
	public void refreshScoreboard(Player player){
		Score score = UtilScoreboard.searchScore(player.getScoreboard(), "coins");
		if(score!=null){
			UtilScoreboard.setScore(player.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot(), getCoins(player));
		}
	}
	
//	public void einlösen(ItemStack item){
//		if(UtilItem.ItemNameEquals(this.item,item)&&item.hasItemMeta()&&!item.getItemMeta().getLore().isEmpty()){
//			try{
//				Integer i = Integer.valueOf(item.getItemMeta().getLore().get(2));
//			}catch(NumberFormatException e){
//				System.err.println("[Coins] Einlösen FAIL!!!");
//			}
//		}
//	}
	
	public ItemStack getCoinsBottle(int coins){
		ItemStack i = this.item.clone();
		i=UtilItem.SetDescriptions(i, new String[]{"§eWenn du diesen Coins-Bottle","§eeinlöst erhälst du","§d"+coins});
		return i;
	}
	
	public void CreateAccount(UUID uuid,String name){
		if(isAsync()){
			mysql.asyncUpdate("INSERT INTO coins_list (name,coins,uuid) SELECT '" +name.toLowerCase()+"','0','"+UtilPlayer.getRealUUID(name,uuid)+"' FROM DUAL WHERE NOT EXISTS (SELECT uuid FROM coins_list WHERE uuid='" +UtilPlayer.getRealUUID(name, uuid)+"');");
		}else{
			mysql.Update("INSERT INTO coins_list (name,coins,uuid) SELECT '" +name.toLowerCase()+"','0','"+UtilPlayer.getRealUUID(name,uuid)+"' FROM DUAL WHERE NOT EXISTS (SELECT uuid FROM coins_list WHERE uuid='" +UtilPlayer.getRealUUID(name, uuid)+"');");
		}
	}
	
	public void getAsyncCoins(Player p,Callback callback){
		getAsyncCoins(UtilPlayer.getRealUUID(p),p.getName(),callback);
	}
	
	public void getAsyncCoins(UUID uuid,String name,Callback callback){
		if(coins.containsKey(UtilPlayer.getRealUUID(name,uuid))){
			callback.done(coins.get(UtilPlayer.getRealUUID(name,uuid)));
		}else{
			mysql.asyncGetInt("SELECT coins FROM coins_list WHERE uuid='" + UtilPlayer.getRealUUID(name,uuid) + "'", callback);
		}
	}
	
	public Integer getCoins(Player p){
		return getCoins(UtilPlayer.getRealUUID(p),p.getName());
	}
	
	public Integer getCoins(UUID uuid,String name){
		if(coins.containsKey(UtilPlayer.getRealUUID(name,uuid)))return coins.get(UtilPlayer.getRealUUID(name,uuid));
		int d = -999;
		try{
			ResultSet rs = mysql.Query("SELECT coins FROM coins_list WHERE uuid='" + UtilPlayer.getRealUUID(name,uuid) + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){	
			UtilException.catchException(err, "Coins", Bukkit.getIp(), this.mysql);
		}
		
		if(d==-999){
			CreateAccount(uuid,name.toLowerCase());
			d=0;
			coins.put(UtilPlayer.getRealUUID(name,uuid), d);
		}else{
			coins.put(UtilPlayer.getRealUUID(name,uuid), d);
		}
		
		return d;
	}
	
	public void giveCoins(PacketManager packetManager,String player,int coins){
		if(UtilPlayer.isOnline(player)){
			addCoinsWithScoreboardUpdate(Bukkit.getPlayer(player), true, coins);
		}else{
			if(give_coins.containsKey(player)){
				coins+=give_coins.get(player);
				give_coins.remove(player);
				give_coins_time.remove(player);
			}else{
				packetManager.SendPacket("BG", new PLAYER_ONLINE(player, packetManager.getClient().getName(), "coins", "null"));
			}
			
			give_coins_time.put(player, System.currentTimeMillis()+TimeSpan.SECOND*9);
			give_coins.put(player, coins);
		}
	}
	
	@EventHandler
	public void update(UpdateEvent ev){
		if(ev.getType()==UpdateType.SLOW){
			if(!give_coins_time.isEmpty()){
				String p;
				for(int i = 0; i<give_coins_time.size(); i++){
					p=(String)give_coins_time.keySet().toArray()[i];
					if(give_coins_time.get(p) < System.currentTimeMillis()){
						give_coins_time.remove(p);
						addCoins(UtilPlayer.getUUID(p, mysql),p, give_coins.get(p));
						give_coins.remove(p);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof PLAYER_ONLINE){
			PLAYER_ONLINE packet = (PLAYER_ONLINE)ev.getPacket();
			
			if(packet.getReason().equalsIgnoreCase("coins")){
				give_coins_time.remove(packet.getPlayer());
				
				if(packet.getServer().contains("loginhub")){
					addCoins(UtilPlayer.getUUID(packet.getPlayer(), mysql),packet.getPlayer(), give_coins.get(packet.getPlayer()));
				}else{
					ev.getPacketManager().SendPacket(packet.getServer(), new GIVE_COINS(packet.getPlayer(), give_coins.get(packet.getPlayer())));
				}
				
				give_coins.remove(packet.getPlayer());
			}
		}else if(ev.getPacket() instanceof GIVE_COINS){
			GIVE_COINS packet = (GIVE_COINS)ev.getPacket();
			
			if(UtilPlayer.isOnline(packet.getPlayer())){
				addCoinsWithScoreboardUpdate(Bukkit.getPlayer(packet.getPlayer()), true, packet.getCoins());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(coins.containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			if(change_coins.contains(UtilPlayer.getRealUUID(ev.getPlayer())))addCoins(ev.getPlayer(),true,0);
			coins.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(coins.containsKey(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())))coins.remove(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
		if(join_Check){
			getCoins(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()),ev.getName());
		}
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "COINS_DEL",coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			change_coins.remove(UtilPlayer.getRealUUID(p));
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "COINS_DEL",coins));
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		
		if(p.hasPermission(kPermission.COINS_ONE_ADD.getPermissionToString())){
			coins++;
		}else if(p.hasPermission(kPermission.COINS_TWO_ADD.getPermissionToString())){
			coins=coins+2;
		}else if(p.hasPermission(kPermission.COINS_THREE_ADD.getPermissionToString())){
			coins=coins+3;
		}
		
		if(Calendar.holiday!=null&&(Calendar.holiday==CalendarType.GEBURSTAG||Calendar.holiday==CalendarType.GEBURSTAG_MANUEL)){
			coins=coins*2;
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "COINS_ADD_DOUBLE"));
		}
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "COINS_ADD",coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			change_coins.remove(UtilPlayer.getRealUUID(p));
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "COINS_ADD",coins));
		}
	}
	
	public UUID inList(UUID uuid){
		for(int i = 0; i < coins.size(); i++){
			if(((UUID)coins.keySet().toArray()[i])==uuid){
				return ((UUID)coins.keySet().toArray()[i]);
			}
		}
		return null;
	}
	
	public void delCoins(UUID uuid,String name,Integer coi){
		change_coins.remove(UtilPlayer.getRealUUID(name, uuid));
		int c = getCoins(UtilPlayer.getRealUUID(name, uuid),name);
		int co=c+coi;
		
		if(isAsync()){
			mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}else{
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}
	}
	
	public void addCoins(UUID uuid,String name,Integer coi){
		if(Calendar.holiday!=null&&Calendar.holiday==CalendarType.GEBURSTAG)coi=coi*2;
		change_coins.remove(UtilPlayer.getRealUUID(name, uuid));
		int c = getCoins(UtilPlayer.getRealUUID(name, uuid),name);
		int co=c+coi;
		
		if(isAsync()){
			mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}else{
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_DEL",coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			change_coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_DEL",coins));
		}
		return true;
	}
	
	public boolean delCoinsWithScoreboardUpdate(Player p,boolean save,Integer coins){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			
			Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
			if(score!=null){
				UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
				UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
			}
			
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_DEL",coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			
			Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
			if(score!=null){
				UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
				UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
			}
			
			change_coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_DEL",coins));
		}
		return true;
	}
	
	public void addCoinsWithScoreboardUpdate(Player p,boolean save,Integer coins){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(Calendar.holiday!=null&&(Calendar.holiday==CalendarType.GEBURSTAG||Calendar.holiday==CalendarType.GEBURSTAG_MANUEL)){
			coins=coins*2;
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_ADD_DOUBLE"));
		}
		
		if(p.hasPermission(kPermission.COINS_ONE_ADD.getPermissionToString())){
			coins++;
		}else if(p.hasPermission(kPermission.COINS_TWO_ADD.getPermissionToString())){
			coins=coins+2;
		}else if(p.hasPermission(kPermission.COINS_THREE_ADD.getPermissionToString())){
			coins=coins+3;
		}
		
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			
			Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
			if(score!=null){
				UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
				UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
			}
			
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_ADD",coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			
			Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
			if(score!=null){
				UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
				UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
			}
			
			change_coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_ADD",coins));
		}
	}
	
	public void addCoins(Player p,boolean save,Integer coins){
		if(p.hasPermission(kPermission.COINS_ONE_ADD.getPermissionToString())){
			coins++;
		}else if(p.hasPermission(kPermission.COINS_TWO_ADD.getPermissionToString())){
			coins=coins+2;
		}else if(p.hasPermission(kPermission.COINS_THREE_ADD.getPermissionToString())){
			coins=coins+3;
		}
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(Calendar.holiday!=null&&(Calendar.holiday==CalendarType.GEBURSTAG||Calendar.holiday==CalendarType.GEBURSTAG_MANUEL)){
			coins=coins*2;
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_ADD_DOUBLE"));
		}
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_ADD",coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			change_coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "COINS_ADD",coins));
		}
	}
	
}

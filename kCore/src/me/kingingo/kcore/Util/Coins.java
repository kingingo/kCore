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
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.NOT_SAVE_COINS;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Coins implements Listener{
	private MySQL mysql;
	@Getter
	private HashMap<UUID,Integer> coins = new HashMap<>();
	private ArrayList<UUID> change_coins = new ArrayList<>();
	CalendarType holiday;
	private ItemStack item;
	@Getter
	@Setter
	private boolean join_Check=true;
	
	public Coins(JavaPlugin instance,MySQL mysql){
		this.mysql=mysql;
		this.holiday=Calendar.getHoliday();
		this.item=UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aCoins-Bottle");
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void SaveAll(){
		for(UUID uuid : coins.keySet()){
			if(change_coins.contains(uuid))addCoins(uuid, 0);
		}
		coins.clear();
		change_coins.clear();
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
		mysql.Update("INSERT INTO coins_list (name,coins,uuid) values ('"+name+"','0','"+uuid+"');");
	}
	
	public Integer getCoins(Player p){
		if(coins.containsKey(UtilPlayer.getRealUUID(p)))return coins.get(UtilPlayer.getRealUUID(p));
		int d = -999;
		try{
			ResultSet rs = mysql.Query("SELECT coins FROM coins_list WHERE uuid='" + UtilPlayer.getRealUUID(p) + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		
		if(d==-999){
			CreateAccount(UtilPlayer.getRealUUID(p),p.getName().toLowerCase());
			d=0;
		}
		
		coins.put(UtilPlayer.getRealUUID(p), d);
		return d;
	}
	
	public Integer getCoins(UUID uuid){
		if(coins.containsKey(uuid))return coins.get(uuid);
		int d = -999;
		try{
			ResultSet rs = mysql.Query("SELECT coins FROM coins_list WHERE uuid='" + uuid + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		
		if(d==-999){
			CreateAccount(uuid,"none");
			d=0;
		}
		
		coins.put(uuid, d);
		return d;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(coins.containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			if(change_coins.contains(UtilPlayer.getRealUUID(ev.getPlayer())))addCoins(ev.getPlayer(),true,0);
			coins.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
	@EventHandler
	public void Packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof NOT_SAVE_COINS){
			coins.remove( ((NOT_SAVE_COINS)ev.getPacket()).getUuid() );
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(coins.containsKey(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())))coins.remove(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
		if(join_Check) getCoins(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getCoins(UtilPlayer.getRealUUID(p));
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_DEL.getText(coins));
		}else{
			int c = getCoins(UtilPlayer.getRealUUID(p));
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			change_coins.remove(UtilPlayer.getRealUUID(p));
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(holiday!=null&&holiday==CalendarType.GEBURSTAG)coins=coins*2;
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_ADD.getText(coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			change_coins.remove(UtilPlayer.getRealUUID(p));
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_ADD.getText(coins));
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
	
	public void delCoins(UUID uuid,Integer coi){
		change_coins.remove(uuid);
		int c = getCoins(uuid);
		int co=c+coi;
		mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+uuid+"'");
	}
	
	public void addCoins(UUID uuid,Integer coi){
		if(holiday!=null&&(holiday==CalendarType.GEBURSTAG||holiday==CalendarType.OSTERN))coi=coi*2;
		change_coins.remove(uuid);
		int c = getCoins(uuid);
		int co=c+coi;
		mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+uuid+"'");
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_DEL.getText(coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			change_coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins){
		if(!change_coins.contains(UtilPlayer.getRealUUID(p)))change_coins.add(UtilPlayer.getRealUUID(p));
		if(holiday!=null&&(holiday==CalendarType.GEBURSTAG||holiday==CalendarType.OSTERN))coins=coins*2;
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_ADD.getText(coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			change_coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.remove(UtilPlayer.getRealUUID(p));
			this.coins.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_ADD.getText(coins));
		}
	}
	
}

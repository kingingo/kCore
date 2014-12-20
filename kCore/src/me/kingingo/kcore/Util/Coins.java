package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.HashMap;

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
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Coins implements Listener{
	private MySQL mysql;
	@Getter
	private HashMap<String,Integer> coins = new HashMap<>();
	CalendarType holiday;
	private ItemStack item;
	@Getter
	@Setter
	private boolean join_Check=true;
	
	public Coins(JavaPlugin instance,MySQL mysql){
		this.mysql=mysql;
		this.holiday=Calendar.getHoliday(3);
		this.item=UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aCoins-Bottle");
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void SaveAll(){
		for(String p : coins.keySet()){
			addCoins(p.toLowerCase(), 0);
		}
		coins.clear();
	}
	
	public void einlösen(ItemStack item){
		if(UtilItem.ItemNameEquals(this.item,item)&&item.hasItemMeta()&&!item.getItemMeta().getLore().isEmpty()){
			try{
				Integer i = Integer.valueOf(item.getItemMeta().getLore().get(2));
			}catch(NumberFormatException e){
				System.err.println("[Coins] Einlösen FAIL!!!");
			}
		}
	}
	
	public ItemStack getCoinsBottle(int coins){
		ItemStack i = this.item.clone();
		i=UtilItem.SetDescriptions(i, new String[]{"§eWenn du diesen Coins-Bottle","§eeinlöst erhälst du","§d"+coins});
		return i;
	}
	
	public void CreateAccount(String p){
		mysql.Update("INSERT INTO coins_list (name,coins) values ('"+p.toLowerCase()+"','0');");
	}
	
	public Integer getCoins(String p){
		if(coins.containsKey(p.toLowerCase()))return coins.get(p.toLowerCase());
		int d = -9999999;
		try{
			ResultSet rs = mysql.Query("SELECT coins FROM coins_list WHERE name='" + p.toLowerCase() + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		
		if(d==-9999999){
			CreateAccount(p);
			d=0;
		}
		
		coins.put(p.toLowerCase(), d);
		return d;
	}
	
	public Integer getCoins(Player p){
		if(coins.containsKey(p.getName().toLowerCase()))return coins.get(p.getName().toLowerCase());
		int d = 0;
		
		try{
			
			ResultSet rs = mysql.Query("SELECT coins FROM coins_list WHERE name='" + p.getName().toLowerCase() + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
 			
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		
		coins.put(p.getName().toLowerCase(), d);
		return d;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(coins.containsKey(ev.getPlayer().getName().toLowerCase())){
			addCoins(ev.getPlayer(),true,0);
			coins.remove(ev.getPlayer().getName().toLowerCase());
		}
	}
	
	@EventHandler
	public void Packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof NOT_SAVE_COINS){
			coins.remove( ((NOT_SAVE_COINS)ev.getPacket()).getSpieler().toLowerCase() );
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(coins.containsKey(ev.getName().toLowerCase()))coins.remove(ev.getName().toLowerCase());
		if(join_Check) getCoins(ev.getName().toLowerCase());
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p.getName().toLowerCase(), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_DEL.getText(coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins,GameType typ){
		if(holiday!=null&&holiday==CalendarType.GEBURSTAG)coins=coins*2;
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p.getName().toLowerCase(), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_ADD.getText(coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_ADD.getText(coins));
		}
	}
	
	public String inList(String player){
		for(int i = 0; i < coins.size(); i++){
			if(((String)coins.keySet().toArray()[i]).equalsIgnoreCase(player)){
				return ((String)coins.keySet().toArray()[i]).toLowerCase();
			}
		}
		return null;
	}
	
	public void delCoins(String p,Integer coi){
//		String player = inList(p);
//		if(player!=null){
//			if(coins.containsKey(player)){
//				addCoins(player,0);
//				coins.remove(player);
//			}
//		}
		int c = getCoins(p);
		int co=c+coi;
		mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.toLowerCase()+"'");
	}
	
	public void addCoins(String p,Integer coi){
		if(holiday!=null&&holiday==CalendarType.GEBURSTAG)coi=coi*2;
//		String player = inList(p);
//		if(player!=null){
//			if(coins.containsKey(player)){
//				addCoins(player,0);
//				coins.remove(player);
//			}
//		}
		int c = getCoins(p);
		int co=c+coi;
		mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.toLowerCase()+"'");
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins){
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p.getName().toLowerCase(), co);
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_DEL.getText(coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins){
		if(holiday!=null&&holiday==CalendarType.GEBURSTAG)coins=coins*2;
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p.getName().toLowerCase(), co);
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_ADD.getText(coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX.getText()+Text.COINS_ADD.getText(coins));
		}
	}
	
}

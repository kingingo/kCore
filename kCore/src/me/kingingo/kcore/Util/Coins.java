package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.HashMap;

import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Coins implements Listener{
	private MySQL mysql;
	private HashMap<Player,Integer> coins = new HashMap<>();
	
	public Coins(JavaPlugin instance,MySQL mysql){
		this.mysql=mysql;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void SaveAll(){
		for(Player p : coins.keySet()){
			addCoins(p, true, 0);
		}
		coins.clear();
	}
	
	public boolean Exist(String p){
		boolean b = false;
		try{
			
			ResultSet rs =mysql.Query("SELECT coins FROM coins_list WHERE name='" + p.toLowerCase() + "'");
			
			while(rs.next()){
				b=Boolean.valueOf(true);
			}
 			
			rs.close();
		}catch (Exception err){
			System.err.println(err);
		}
		
		if(!b){
			CreateAccount(p);
		}
				
		return b;
	}
	
	public void CreateAccount(String p){
		mysql.Update("INSERT INTO coins_list (name,coins) values ('"+p.toLowerCase()+"','0');");
	}
	
	public Integer getCoins(Player p){
		if(coins.containsKey(p))return coins.get(p);
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
		coins.put(p, d);
		return d;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(coins.containsKey(ev.getPlayer())){
			addCoins(ev.getPlayer(),true,0);
			coins.remove(ev.getPlayer());
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Join(PlayerLoginEvent ev){
		Exist(ev.getPlayer().getName());
		if(coins.containsKey(ev.getPlayer()))coins.remove(ev.getPlayer());
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p, co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_DEL.getText(coins));
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p, co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins,GameType typ){
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p, co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_ADD.getText(coins));
		}else{
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p, co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.COINS_ADD.getText(coins));
		}
	}
	
	public boolean delCoins(Player p,boolean save,Integer coins){
		if(!save){
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p, co);
		}else{
			int c = getCoins(p);
			if(c<coins)return false;
			int co=c-coins;
			this.coins.put(p, co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
		}
		return true;
	}
	
	public void addCoins(Player p,boolean save,Integer coins){
		if(!save){
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p, co);
		}else{
			int c = getCoins(p);
			int co=c+coins;
			this.coins.put(p, co);
			mysql.Update("UPDATE `coins_list` SET coins='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
		}
	}
	
}

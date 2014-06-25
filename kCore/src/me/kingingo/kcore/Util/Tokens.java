package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.HashMap;

import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Tokens implements Listener {

	private MySQL mysql;
	private HashMap<Player,Integer> tokens = new HashMap<>();
	
	public Tokens(JavaPlugin instance,MySQL mysql){
		this.mysql=mysql;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public boolean Exist(String p){
		boolean b = false;
		try{
			
			ResultSet rs =mysql.Query("SELECT tokens FROM tokens_list WHERE name='" + p.toLowerCase() + "'");
			
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
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		addTokens(ev.getPlayer(),true,0);
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		Exist(ev.getPlayer().getName());
		getTokens(ev.getPlayer());
	}
	
	public void CreateAccount(String p){
		mysql.Update("INSERT INTO tokens_list (name,tokens) values ('"+p.toLowerCase()+"','0');");
	}
	
	public Integer getTokens(Player p){
		if(tokens.containsKey(p))return tokens.get(p);
		int d = 0;
		
		try{
			
			ResultSet rs =mysql.Query("SELECT tokens FROM tokens_list WHERE name='" + p.getName().toLowerCase() + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
 			
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		tokens.put(p, d);
		return d;
	}
	
	public boolean delTokens(Player p,boolean save,Integer coins){
		if(!save){
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(p, co);
		}else{
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(p, co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.getName()+"'");
		}
		return true;
	}
	
	public void addTokens(Player p,boolean save,Integer coins){
		if(!save){
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p, co);
		}else{
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p, co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
		}
	}
	
	
}

package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Tokens implements Listener {

	private MySQL mysql;
	@Getter
	private HashMap<String,Integer> tokens = new HashMap<>();
	@Getter
	@Setter
	private boolean join_Check=true;
	
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
	
	public void SaveAll(){
		for(String p : tokens.keySet()){
			addTokens(p, true, 0);
		}
		tokens.clear();
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(tokens.containsKey(ev.getName().toLowerCase()))tokens.remove(ev.getName().toLowerCase());
		if(join_Check) getTokens(ev.getName().toLowerCase());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		addTokens(ev.getPlayer(),true,0);
		if(tokens.containsKey(ev.getPlayer().getName().toLowerCase()))tokens.remove(ev.getPlayer().getName().toLowerCase());
	}
	
	public void CreateAccount(String p){
		mysql.Update("INSERT INTO tokens_list (name,tokens) values ('"+p.toLowerCase()+"','0');");
	}
	
	public Integer getTokens(String p){
		if(tokens.containsKey(p.toLowerCase()))return tokens.get(p.toLowerCase());
		int d = 0;
		
		try{
			
			ResultSet rs =mysql.Query("SELECT tokens FROM tokens_list WHERE name='" + p.toLowerCase() + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
 			
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		tokens.put(p.toLowerCase(), d);
		return d;
	}
	
	public Integer getTokens(Player p){
		if(tokens.containsKey(p.getName().toLowerCase()))return tokens.get(p.getName().toLowerCase());
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
		tokens.put(p.getName().toLowerCase(), d);
		return d;
	}
	
	public boolean delTokens(Player p,boolean save,Integer coins,GameType typ){
		if(!save){
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(p.getName().toLowerCase(), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_DEL.getText(coins));
		}else{
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.getName()+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addTokens(Player p,boolean save,Integer coins,GameType typ){
		if(!save){
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p.getName().toLowerCase(), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_ADD.getText(coins));
		}else{
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_ADD.getText(coins));
		}
	}
	
	public boolean delTokens(Player p,boolean save,Integer coins){
		if(!save){
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(p.getName().toLowerCase(), co);
		}else{
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.getName()+"'");
		}
		return true;
	}
	
	public void addTokens(String p,boolean save,Integer coins){
		if(!save){
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p.toLowerCase(), co);
		}else{
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p.toLowerCase(), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.toLowerCase()+"'");
		}
	}
	
	public void addTokens(Player p,boolean save,Integer coins){
		if(!save){
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p.getName().toLowerCase(), co);
		}else{
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(p.getName().toLowerCase(), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE name='"+p.getName().toLowerCase()+"'");
		}
	}
	
	
}

package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

public class Tokens_Old implements Listener {

	private MySQL mysql;
	@Getter
	private HashMap<UUID,Integer> tokens = new HashMap<>();
	private ArrayList<UUID> change_tokens = new ArrayList<>();
	@Getter
	@Setter
	private boolean join_Check=true;
	
	public Tokens_Old(JavaPlugin instance,MySQL mysql){
		this.mysql=mysql;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public boolean Exist(UUID uuid){
		boolean b = false;
		try{
			
			ResultSet rs =mysql.Query("SELECT tokens FROM tokens_list WHERE uuid='" + uuid + "'");
			
			while(rs.next()){
				b=Boolean.valueOf(true);
			}
 			
			rs.close();
		}catch (Exception err){
			System.err.println(err);
		}
		
		if(!b){
			CreateAccount(uuid);
		}
				
		return b;
	}
	
	public void SaveAll(){
		for(UUID p : tokens.keySet()){
			if(change_tokens.contains(p))addTokens(p, true, 0);
		}
		tokens.clear();
		change_tokens.clear();
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(tokens.containsKey(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())))tokens.remove(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId()));
		if(join_Check) getTokens(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId()));
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(change_tokens.contains(UtilPlayer.getRealUUID(ev.getPlayer())))addTokens(ev.getPlayer(),true,0);
		if(tokens.containsKey(UtilPlayer.getRealUUID(ev.getPlayer())))tokens.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
	}
	
	public void CreateAccount(UUID uuid){
		mysql.Update("INSERT INTO tokens_list (uuid,tokens) values ('"+uuid+"','0');");
	}
	
	public Integer getTokens(UUID uuid){
		if(tokens.containsKey(uuid))return tokens.get(uuid);
		int d = 0;
		
		try{
			
			ResultSet rs =mysql.Query("SELECT tokens FROM tokens_list WHERE uuid='" + uuid + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
 			
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		tokens.put(uuid, d);
		return d;
	}
	
	public Integer getTokens(Player p){
		if(tokens.containsKey(UtilPlayer.getRealUUID(p)))return tokens.get(UtilPlayer.getRealUUID(p));
		int d = 0;
		
		try{
			
			ResultSet rs =mysql.Query("SELECT tokens FROM tokens_list WHERE uuid='" + UtilPlayer.getRealUUID(p) + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
 			
			rs.close();
		}catch (Exception err){	
			System.err.println(err);
		}
		tokens.put(UtilPlayer.getRealUUID(p), d);
		return d;
	}
	
	public boolean delTokens(Player p,boolean save,Integer coins,GameType typ){
		if(!change_tokens.contains(UtilPlayer.getRealUUID(p)))change_tokens.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_DEL.getText(coins));
		}else{
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			change_tokens.remove(UtilPlayer.getRealUUID(p));
			tokens.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_DEL.getText(coins));
		}
		return true;
	}
	
	public void addTokens(Player p,boolean save,Integer coins,GameType typ){
		if(!change_tokens.contains(UtilPlayer.getRealUUID(p)))change_tokens.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_ADD.getText(coins));
		}else{
			int c = getTokens(p);
			int co=c+coins;
			change_tokens.remove(UtilPlayer.getRealUUID(p));
			tokens.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Text.PREFIX_GAME.getText(typ.name())+Text.TOKENS_ADD.getText(coins));
		}
	}
	
	public boolean delTokens(Player p,boolean save,Integer coins){
		if(!change_tokens.contains(UtilPlayer.getRealUUID(p)))change_tokens.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			tokens.put(UtilPlayer.getRealUUID(p), co);
		}else{
			int c = getTokens(p);
			if(c<coins)return false;
			int co=c-coins;
			change_tokens.remove(UtilPlayer.getRealUUID(p));
			tokens.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
		}
		return true;
	}
	
	public void addTokens(UUID uuid,boolean save,Integer coins){
		if(!change_tokens.contains(uuid))change_tokens.add(uuid);
		if(!save){
			int c = getTokens(uuid);
			int co=c+coins;
			tokens.put(uuid, co);
		}else{
			int c = getTokens(uuid);
			int co=c+coins;
			change_tokens.remove(uuid);
			tokens.put(uuid, co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE uuid='"+uuid+"'");
		}
	}
	
	public void addTokens(Player p,boolean save,Integer coins){
		if(!change_tokens.contains(UtilPlayer.getRealUUID(p)))change_tokens.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getTokens(p);
			int co=c+coins;
			tokens.put(UtilPlayer.getRealUUID(p), co);
		}else{
			int c = getTokens(p);
			int co=c+coins;
			change_tokens.remove(UtilPlayer.getRealUUID(p));
			tokens.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `tokens_list` SET tokens='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
		}
	}
	
	
}

package eu.epicpvp.kcore.Gilden;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kSkyblock.SkyBlockManager;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;

public class SkyBlockGildenManager extends GildenManager{

	@Getter
	private SkyBlockManager sky;
	@Getter
	private StatsManager stats;
	
	public SkyBlockGildenManager(SkyBlockManager manager, MySQL mysql,GildenType typ, CommandHandler cmd,StatsManager stats) {
		super(mysql, typ, cmd,stats);
		this.sky=manager;
		this.stats=stats;
		manager.addGildenWorld("gilde", this);
	}
	
	@EventHandler
	public void Teleport(GildenPlayerTeleportEvent ev){
		if(!isPlayerInGilde(ev.getPlayer())){
			ev.setReason(TranslationHandler.getText(ev.getPlayer(), "GILDE_PLAYER_IS_NOT_IN_GILDE"));
			ev.setCancelled(true);
		}else{
			ev.setReason(TranslationHandler.getText(ev.getPlayer(), "SKYBLOCK_NO_ISLAND"));
			ev.setCancelled( sky.getGilden_world().getIslandHome(getPlayerGilde(ev.getPlayer()))==null );
		}
	}
	
	public void removeGildenEintrag(Player player,String name){
		ArrayList<UUID> l = new ArrayList<>();
		getMember(name);
		for(int n : new ArrayList<>(getGilden_player().keySet())){
			if( getGilden_player().get(n).equalsIgnoreCase(name)){
				getGilden_player().remove(n);
			}
		}
		
		if(getTyp()==GildenType.SKY){
			sky.getGilden_world().removeIsland(player,name);
		}
		
		super.mysql.Update("DELETE FROM list_gilden_"+typ.getKuerzel()+" WHERE gilde='" + name.toLowerCase() + "'");
		super.mysql.Update("DELETE FROM list_gilden_"+typ.getKuerzel()+"_data WHERE gilde='" + name.toLowerCase() + "'");
		super.mysql.Update("DELETE FROM list_gilden_"+typ.getKuerzel()+"_user WHERE gilde='" + name.toLowerCase() + "'");
		super.gilden_data.remove(name);
		super.gilden_tag.remove(name);
		super.gilden_data_musst_saved.remove(name);
	}
	
	public void LoadRanking(boolean b){
		if(ranking.isEmpty()||b){
			extra_prefix.clear();
			try{
			     ResultSet rs = getMysql().Query("SELECT `money`,`gilde` FROM `list_gilden_"+typ.getKuerzel()+"_data` ORDER BY money DESC LIMIT 15;");

			      int zahl = 1;
			      
			      while (rs.next()) {
			    	  if(zahl==1){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§4§l " + rs.getString(2));
			  			}else if(zahl==2){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§2§l " + rs.getString(2));
			  			}else if(zahl==3){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§e§l " + rs.getString(2));
			  			}else if(zahl>=4 && zahl<=6){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§3 " + rs.getString(2));
			  			}else if(zahl>=7 && zahl<=9){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§d " + rs.getString(2));
			  			}else if(zahl>=10 && zahl<=12){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§a " + rs.getString(2));
			  			}else if(zahl>=13 && zahl<=15){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§b " + rs.getString(2));
			  			}else{
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§6 " + rs.getString(2));
			  			}
				     extra_prefix.put(rs.getString(2).toLowerCase(), zahl);
				     zahl++;
			      }

			      rs.close();
			 } catch (Exception err) {
			      System.out.println("MySQL-Error: " + err.getMessage());
			 }
		}
	}
	
	public void Ranking(Player p){
		p.sendMessage("§b– – – – – – – –  §6§lGilden Ranking | Top 15 §b– – – – – – – – ");
		p.sendMessage("§b Place | Money | Gilde");
		LoadRanking(false);
		for(Integer i : ranking.keySet())p.sendMessage(ranking.get(i));
	}
	
	public void TeleportToHome(Player p){
		if(!isPlayerInGilde(p)){
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
			return;
		}
		String g = getPlayerGilde(p);
		
		if(getTyp()==GildenType.SKY){
			p.teleport(getSky().getGilden_world().getIslandHome(g));
			p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "GILDE_TELEPORTET"));
			UtilPlayer.sendPacket(p, sky.getGilden_world().getIslandBorder(p));;
		}else{
			String w = getString(StatsKey.WORLD, g, getTyp());
			int x = getInt(StatsKey.LOC_X, g, super.getTyp());
			int y = getInt(StatsKey.LOC_Y, g, super.getTyp());
			int z = getInt(StatsKey.LOC_Z, g, super.getTyp());
			if(Bukkit.getWorld(w)==null)return;
			if(x==0&&y==0&&z==0&&g.equalsIgnoreCase("0"))return;
			Location loc = new Location(Bukkit.getWorld(w),x,y,z);
			p.teleport(loc);
			p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "GILDE_TELEPORTET"));	
		}
	}

}

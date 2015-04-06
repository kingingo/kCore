package me.kingingo.kcore.Gilden;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kSkyblock.SkyBlockManager;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.PlayerStats.Stats;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SkyBlockGildenManager extends GildenManager{

	@Getter
	private SkyBlockManager sky;
	
	public SkyBlockGildenManager(SkyBlockManager manager, MySQL mysql,GildenType typ, CommandHandler cmd) {
		super(manager.getInstance(), mysql, typ, cmd);
		this.sky=manager;
		manager.addGildenWorld("gilde", this);
	}
	
	@EventHandler
	public void Teleport(GildenPlayerTeleportEvent ev){
		if(!isPlayerInGilde(ev.getPlayer())){
			ev.setReason(Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
			ev.setCancelled(true);
		}else{
			ev.setReason(Text.SKYBLOCK_NO_ISLAND.getText());
			ev.setCancelled( sky.getGilden_world().getIslandHome(getPlayerGilde(ev.getPlayer()))==null );
		}
	}
	
	public void removeGildenEintrag(String name){
		ArrayList<UUID> l = new ArrayList<>();
		getMember(name);
		for(UUID n : getGilden_player().keySet()){
			if( getGilden_player().get(n).equalsIgnoreCase(name)){
				l.add(n);
			}
		}
		for(UUID n : l){
			getGilden_player().remove(n);
		}
		
		if(getTyp()==GildenType.SKY){
			sky.getGilden_world().removeIsland(name);
		}
		
		super.mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+" WHERE gilde='" + name.toLowerCase() + "'");
		super.mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+"_data WHERE gilde='" + name.toLowerCase() + "'");
		super.mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+"_user WHERE gilde='" + name.toLowerCase() + "'");
		super.gilden_data.remove(name);
		super.gilden_tag.remove(name);
		super.gilden_data_musst_saved.remove(name);
	}
	
	public void TeleportToHome(Player p){
		if(!isPlayerInGilde(p)){
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
			return;
		}
		String g = getPlayerGilde(p);
		
		if(getTyp()==GildenType.SKY){
			p.teleport(getSky().getGilden_world().getIslandHome(g));
			p.sendMessage(Text.PREFIX.getText()+Text.GILDE_TELEPORTET.getText());
		}else{
			String w = getString(Stats.WORLD, g, getTyp());
			int x = getInt(Stats.LOC_X, g, super.getTyp());
			int y = getInt(Stats.LOC_Y, g, super.getTyp());
			int z = getInt(Stats.LOC_Z, g, super.getTyp());
			if(Bukkit.getWorld(w)==null)return;
			if(x==0&&y==0&&z==0&&g.equalsIgnoreCase("0"))return;
			Location loc = new Location(Bukkit.getWorld(w),x,y,z);
			p.teleport(loc);
			p.sendMessage(Text.PREFIX.getText()+Text.GILDE_TELEPORTET.getText());	
		}
	}

}

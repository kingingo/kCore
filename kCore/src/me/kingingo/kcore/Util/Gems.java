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
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.GIVE_GEMS;
import me.kingingo.kcore.Packet.Packets.PLAYER_ONLINE;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

public class Gems implements Listener{
	@Getter
	private MySQL mysql;
	@Getter
	private HashMap<UUID,Integer> gems = new HashMap<>();
	private ArrayList<UUID> change_gems = new ArrayList<>();
	private HashMap<String,Long> give_gems_time = new HashMap<>();
	private HashMap<String,Integer> give_gems = new HashMap<>();
	private CalendarType holiday;
	private ItemStack item;
	@Getter
	@Setter
	private boolean join_Check=true;
	
	public Gems(MySQL mysql){
		this.mysql=mysql;
		this.holiday=Calendar.getHoliday();
		this.item=UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aGems-Bottle");
		this.mysql.Update("CREATE TABLE IF NOT EXISTS gems_list(name varchar(30),gems int,uuid varchar(60))");
		Bukkit.getPluginManager().registerEvents(this, mysql.getInstance());
	}
	
	public void SaveAll(){
		for(UUID uuid : gems.keySet()){
			if(change_gems.contains(uuid))addGems(uuid, 0);
		}
		gems.clear();
		change_gems.clear();
	}
	
	public void refreshScoreboard(Player player){
		Score score = UtilScoreboard.searchScore(player.getScoreboard(), "gems");
		if(score!=null){
			UtilScoreboard.setScore(player.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot(), getGems(player));
		}
	}
	
//	public void einlösen(ItemStack item){
//		if(UtilItem.ItemNameEquals(this.item,item)&&item.hasItemMeta()&&!item.getItemMeta().getLore().isEmpty()){
//			try{
//				Integer i = Integer.valueOf(item.getItemMeta().getLore().get(2));
//			}catch(NumberFormatException e){
//				System.err.println("[Gems] Einlösen FAIL!!!");
//			}
//		}
//	}
	
	public ItemStack getGemsBottle(int coins){
		ItemStack i = this.item.clone();
		i=UtilItem.SetDescriptions(i, new String[]{"§eWenn du diesen Gems-Bottle","§eeinlöst erhälst du","§d"+coins});
		return i;
	}
	
	public void CreateAccount(UUID uuid,String name){
		mysql.Update("INSERT INTO gems_list (name,gems,uuid) values ('"+name+"','0','"+uuid+"');");
	}
	
	public Integer getGems(Player p){
		return getGems(UtilPlayer.getRealUUID(p));
	}
	
	public Integer getGems(UUID uuid){
		if(gems.containsKey(uuid))return gems.get(uuid);
		int d = -999;
		try{
			ResultSet rs = mysql.Query("SELECT gems FROM gems_list WHERE uuid='" + uuid + "'");
			
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
		
		gems.put(uuid, d);
		return d;
	}
	
	public void giveGems(PacketManager packetManager,String player,int gems){
		if(UtilPlayer.isOnline(player)){
			addGemsWithScoreboardUpdate(Bukkit.getPlayer(player), true, gems);
		}else{
			give_gems_time.put(player, System.currentTimeMillis()+TimeSpan.SECOND*9);
			give_gems.put(player, gems);
			packetManager.SendPacket("BG", new PLAYER_ONLINE(player, packetManager.getC().getName(), "gems", "null"));
		}
	}
	
	@EventHandler
	public void update(UpdateEvent ev){
		if(ev.getType()==UpdateType.SLOW){
			if(!give_gems_time.isEmpty()){
				String p;
				for(int i = 0; i<give_gems_time.size(); i++){
					p=(String)give_gems_time.keySet().toArray()[i];
					if(give_gems_time.get(p) < System.currentTimeMillis()){
						give_gems_time.remove(p);
						addGems(UtilPlayer.getUUID(p, mysql), give_gems.get(p));
						give_gems.remove(p);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof PLAYER_ONLINE){
			PLAYER_ONLINE packet = (PLAYER_ONLINE)ev.getPacket();
			
			if(packet.getReason().equalsIgnoreCase("gems")&&!packet.getServer().equalsIgnoreCase("null")&&give_gems.containsKey(packet.getPlayer())){
				give_gems_time.remove(packet.getPlayer());
				
				if(packet.getServer().contains("loginhub")){
					addGems(UtilPlayer.getUUID(packet.getPlayer(), mysql), give_gems.get(packet.getPlayer()));
				}else{
					ev.getPacketManager().SendPacket(packet.getServer(), new GIVE_GEMS(packet.getPlayer(), give_gems.get(packet.getPlayer())));
				}
				
				give_gems.remove(packet.getPlayer());
			}
		}else if(ev.getPacket() instanceof GIVE_GEMS){
			GIVE_GEMS packet = (GIVE_GEMS)ev.getPacket();
			
			if(UtilPlayer.isOnline(packet.getPlayer())){
				addGemsWithScoreboardUpdate(Bukkit.getPlayer(packet.getPlayer()), true, packet.getGems());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(gems.containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			if(change_gems.contains(UtilPlayer.getRealUUID(ev.getPlayer())))addGems(ev.getPlayer(),true,0);
			gems.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(gems.containsKey(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())))gems.remove(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
		if(join_Check) getGems(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
	}
	
	public boolean delGems(Player p,boolean save,Integer coins,GameType typ){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		
		if(!save){
			int c = getGems(UtilPlayer.getRealUUID(p));
			if(c<coins)return false;
			int co=c-coins;
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "GEMS_DEL",coins));
		}else{
			int c = getGems(UtilPlayer.getRealUUID(p));
			if(c<coins)return false;
			int co=c-coins;
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			change_gems.remove(UtilPlayer.getRealUUID(p));
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "GEMS_DEL",coins));
		}
		return true;
	}
	
	public void addGems(Player p,boolean save,Integer coins,GameType typ){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		
		if(!save){
			int c = getGems(p);
			int co=c+coins;
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "GEMS_ADD",coins));
		}else{
			int c = getGems(p);
			int co=c+coins;
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			change_gems.remove(UtilPlayer.getRealUUID(p));
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "GEMS_ADD",coins));
		}
	}
	
	public UUID inList(UUID uuid){
		for(int i = 0; i < gems.size(); i++){
			if(((UUID)gems.keySet().toArray()[i])==uuid){
				return ((UUID)gems.keySet().toArray()[i]);
			}
		}
		return null;
	}
	
	public void delGems(UUID uuid,Integer coi){
		change_gems.remove(uuid);
		int c = getGems(uuid);
		int co=c+coi;
		mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+uuid+"'");
	}
	
	public void addGems(UUID uuid,Integer coi){
		change_gems.remove(uuid);
		int c = getGems(uuid);
		int co=c+coi;
		mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+uuid+"'");
	}
	
	public boolean delGems(Player p,boolean save,Integer coins){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		if(!save){
			int c = getGems(p);
			if(c<coins)return false;
			int co=c-coins;
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_DEL",coins));
		}else{
			int c = getGems(p);
			if(c<coins)return false;
			int co=c-coins;
			change_gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_DEL",coins));
		}
		return true;
	}
	
	public boolean delGemsWithScoreboardUpdate(Player p,boolean save,Integer coins){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		
		if(!save){
			int c = getGems(p);
			if(c<coins)return false;
			int co=c-coins;
			
			Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
			if(score!=null){
				UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
				UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
			}
			
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_DEL",coins));
		}else{
			int c = getGems(p);
			if(c<coins)return false;
			int co=c-coins;
			
			Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
			if(score!=null){
				UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
				UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
			}
			
			change_gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_DEL",coins));
		}
		return true;
	}
	
	public void addGemsWithScoreboardUpdate(Player p,boolean save,Integer coins){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		
		if(!save){
			int c = getGems(p);
			int co=c+coins;
			
			if(p.getScoreboard()!=null&&p.getScoreboard().getObjective(DisplaySlot.SIDEBAR)!=null){
				Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
				if(score!=null){
					UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
					UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
				}
			}
			
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_ADD",coins));
		}else{
			int c = getGems(p);
			int co=c+coins;

			if(p.getScoreboard()!=null&&p.getScoreboard().getObjective(DisplaySlot.SIDEBAR)!=null){
				Score score = UtilScoreboard.searchScore(p.getScoreboard(), String.valueOf(c));
				if(score!=null){
					UtilScoreboard.setScore(p.getScoreboard(), ""+co, score.getObjective().getDisplaySlot(), score.getScore());
					UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
				}
			}
			
			change_gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_ADD",coins));
		}
	}
	
	public void addGems(Player p,boolean save,Integer coins){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		
		if(!save){
			int c = getGems(p);
			int co=c+coins;
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_ADD",coins));
		}else{
			int c = getGems(p);
			int co=c+coins;
			change_gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.remove(UtilPlayer.getRealUUID(p));
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_ADD",coins));
		}
	}
	
}

package me.kingingo.kcore.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
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
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

public class Gems extends kListener{
	@Getter
	private MySQL mysql;
	@Getter
	private HashMap<UUID,Integer> gems = new HashMap<>();
	private ArrayList<UUID> change_gems = new ArrayList<>();
	private HashMap<String,Long> give_gems_time = new HashMap<>();
	private HashMap<String,Integer> give_gems = new HashMap<>();
	private ItemStack item;
	@Getter
	@Setter
	private boolean join_Check=true;
	@Getter
	@Setter
	private boolean async=false;
	
	public Gems(MySQL mysql){
		super(mysql.getInstance(),"Gems");
		this.mysql=mysql;
		this.item=UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aGems-Bottle");
		this.mysql.Update("CREATE TABLE IF NOT EXISTS gems_list(name varchar(30),gems int,uuid varchar(60))");
	}
	
	public void SaveAll(){
		for(UUID uuid : gems.keySet()){
			if(change_gems.contains(uuid))
				mysql.Update("UPDATE `gems_list` SET gems='"+gems.get(uuid)+"' WHERE uuid='"+uuid+"'");
				
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
		if(isAsync()){
			mysql.asyncUpdate("INSERT INTO gems_list (name,gems,uuid) SELECT '" +name.toLowerCase()+"','0','"+UtilPlayer.getRealUUID(name,uuid)+"' FROM DUAL WHERE NOT EXISTS (SELECT uuid FROM gems_list WHERE uuid='" +UtilPlayer.getRealUUID(name, uuid)+"');");
		}else{
			mysql.Update("INSERT INTO gems_list (name,gems,uuid) SELECT '" +name.toLowerCase()+"','0','"+UtilPlayer.getRealUUID(name,uuid)+"' FROM DUAL WHERE NOT EXISTS (SELECT uuid FROM gems_list WHERE uuid='" +UtilPlayer.getRealUUID(name, uuid)+"');");
		}
	}
	
	public Integer getGems(Player p){
		return getGems(UtilPlayer.getRealUUID(p),p.getName());
	}
	
	public Integer getGems(UUID uuid,String name){
		if(gems.containsKey(UtilPlayer.getRealUUID(name,uuid)))return gems.get(UtilPlayer.getRealUUID(name,uuid));
		int d = -999;
		try{
			ResultSet rs = mysql.Query("SELECT gems FROM gems_list WHERE uuid='" + UtilPlayer.getRealUUID(name,uuid) + "'");
			
			while(rs.next()){
				d = rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){	
			UtilException.catchException(err, "Gems", Bukkit.getIp(), mysql);
		}
		
		if(d==-999){
			CreateAccount(uuid,name);
			d=0;
			gems.put(UtilPlayer.getRealUUID(name,uuid), d);
		}else{
			gems.put(UtilPlayer.getRealUUID(name,uuid), d);
		}
		
		return d;
	}
	
	public void giveGems(PacketManager packetManager,String player,int gems){
		player=player.toLowerCase();
		if(UtilPlayer.isOnline(player)){
			addGemsWithScoreboardUpdate(Bukkit.getPlayer(player), true, gems);
		}else{
			if(give_gems.containsKey(player)){
				gems+=give_gems.get(player);
				give_gems.remove(player);
				give_gems_time.remove(player);
			}

			give_gems_time.put(player, System.currentTimeMillis()+TimeSpan.SECOND*9);
			give_gems.put(player, gems);
			packetManager.SendPacket("BG", new PLAYER_ONLINE(player, packetManager.getClient().getName(), "gems", "null"));
			Log("Prüft ob der Spieler "+player+" Online ist für seine "+gems+" Gems!");
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
						addGems(UtilPlayer.getUUID(p, mysql),p, give_gems.get(p));
						Log("Der Spieler "+p+" ist nicht online und erhaelt nun die "+give_gems.get(p)+" Gems!");
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
					addGems(UtilPlayer.getUUID(packet.getPlayer(), mysql),packet.getPlayer(), give_gems.get(packet.getPlayer()));
					Log("Der Spieler "+packet.getPlayer()+" befindet sich auf dem LoginHub und erhaelt sie hier direkt!");
				}else{
					ev.getPacketManager().SendPacket(packet.getServer(), new GIVE_GEMS(packet.getPlayer(), give_gems.get(packet.getPlayer())));
					Log("Der Spieler "+packet.getPlayer()+" befindet sich auf dem "+packet.getServer()+" und es wird ein Benachrichtigung dahin geschickt!");
				}
				
				give_gems.remove(packet.getPlayer());
			}
		}else if(ev.getPacket() instanceof GIVE_GEMS){
			GIVE_GEMS packet = (GIVE_GEMS)ev.getPacket();
			
			if(UtilPlayer.isOnline(packet.getPlayer())){
				addGemsWithScoreboardUpdate(Bukkit.getPlayer(packet.getPlayer()), true, packet.getGems());
				Log("Der Spieler "+packet.getPlayer()+" soll "+packet.getGems()+" Gems erhalten!");
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(gems.containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			if(change_gems.contains(UtilPlayer.getRealUUID(ev.getPlayer()))){
				if(isAsync()){
					mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+gems.get(UtilPlayer.getRealUUID(ev.getPlayer()))+"' WHERE uuid='"+UtilPlayer.getRealUUID(ev.getPlayer())+"'");
				}else{
					mysql.Update("UPDATE `gems_list` SET gems='"+gems.get(UtilPlayer.getRealUUID(ev.getPlayer()))+"' WHERE uuid='"+UtilPlayer.getRealUUID(ev.getPlayer())+"'");
				}
			}
			change_gems.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
			gems.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(gems.containsKey(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())))gems.remove(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
		if(join_Check) getGems(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()),ev.getName());
	}
	
	public boolean delGems(Player p,boolean save,Integer coins,GameType typ){
		if(!change_gems.contains(UtilPlayer.getRealUUID(p)))change_gems.add(UtilPlayer.getRealUUID(p));
		
		if(!save){
			int c = getGems(p);
			if(c<coins)return false;
			int co=c-coins;
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			p.sendMessage(Language.getText(p, "PREFIX_GAME",typ.name())+Language.getText(p, "GEMS_DEL",coins));
		}else{
			int c = getGems(p);
			if(c<coins)return false;
			int co=c-coins;
			this.gems.put(UtilPlayer.getRealUUID(p), co);
			change_gems.remove(UtilPlayer.getRealUUID(p));
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
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
			
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			
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
	
	public void delGems(UUID uuid,String name,Integer coi){
		change_gems.remove(UtilPlayer.getRealUUID(name, uuid));
		int c = getGems(UtilPlayer.getRealUUID(name, uuid),name);
		int co=c+coi;
		this.gems.remove(UtilPlayer.getRealUUID(name, uuid));
		if(isAsync()){
			mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}else{
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}
	}
	
	public void addGems(UUID uuid,String name,Integer coi){
		change_gems.remove(UtilPlayer.getRealUUID(name, uuid));
		int c = getGems(UtilPlayer.getRealUUID(name, uuid),name);
		int co=c+coi;
		this.gems.remove(UtilPlayer.getRealUUID(name, uuid));
		if(isAsync()){
			mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}else{
			mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(name, uuid)+"'");
		}
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
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
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
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
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
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
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
			if(isAsync()){
				mysql.asyncUpdate("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}else{
				mysql.Update("UPDATE `gems_list` SET gems='"+co+"' WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
			}
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "GEMS_ADD",coins));
		}
	}
	
}

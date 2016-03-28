package eu.epicpvp.kcore.Listener.AntiCrashListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Util.UtilException;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class AntiCrashListener extends kListener {
	private final Map<String, Long> switchavg;
	private final Map<String, Long> hitsavg;

	private final Map<String, Integer> switchcount;
	private final Map<String, Integer> hitscount;

	private final Map<String, Long> switchlast;
	private final Map<String, Long> hitslast;
	
	private final ArrayList<String> kick;
	private ClientWrapper client;
	private MySQL mysql;

	public AntiCrashListener(ClientWrapper client ,MySQL mysql) {
		super(mysql.getInstance(), "AntiCrashListener");
		this.hitsavg = new HashMap<>();
		this.switchavg = new HashMap<>();

		this.switchcount = new HashMap<>();
		this.hitscount = new HashMap<>();

		this.switchlast = new HashMap<>();
		this.hitslast = new HashMap<>();
		
		this.kick=new ArrayList<>();
		
		this.mysql=mysql;
		this.client=client;
	}
	

	String iname;
	Long iavg;
	Long ilast;
	Integer ii;
	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent e) {
		if(this.kick.contains(e.getPlayer().getName())){
			e.setCancelled(true);
		}else{
			iname = e.getPlayer().getName();
			iavg = (Long) this.switchavg.get(iname);
			ilast = (Long) this.switchlast.get(iname);
			ii = (Integer) this.switchcount.get(iname);
			if (ii == null)
				ii = Integer.valueOf(0);
			if (iavg == null)
				iavg = Long.valueOf(0L);
			if (ilast == null)
				ilast = Long.valueOf(0L);
			if (ii.intValue() > 200) {
				if ((iavg != null) && (iavg.longValue() / 200L < 50000L)) {
					this.kick.add(e.getPlayer().getName());
					LoadedPlayer loadedplayer = this.client.getPlayerAndLoad( UtilPlayer.getRealUUID(e.getPlayer()) );
					loadedplayer.kickPlayer("\u00A75cStop!");
					Log("IP: "+e.getPlayer().getAddress().getAddress().getHostAddress()+" Real-UUID:"+UtilPlayer.getRealUUID(e.getPlayer())+" UUID:"+e.getPlayer().getUniqueId());
					Log("Spieler "+iname+" wurde wegen Item Change Crash gekickt!");
					UtilException.catchException(client.getHandle().getName(), Bukkit.getServer().getIp(), this.mysql, "Spieler "+name+" wurde wegen Animation Crash gekickt! "+" IP: "+e.getPlayer().getAddress().getAddress().getHostAddress()+" Real-UUID:"+UtilPlayer.getRealUUID(e.getPlayer())+" UUID:"+e.getPlayer().getUniqueId());
				}else{
					this.switchcount.remove(iname);
					this.switchavg.remove(iname);
				}
			} else {
				if (ilast.longValue() != 0L) {
					this.switchavg.put(
							iname,
							Long.valueOf(iavg.longValue()
									+ (System.nanoTime() - ilast.longValue())));
				}
				this.switchcount.put(iname, Integer.valueOf(ii.intValue() + 1));
				this.switchlast.put(iname, Long.valueOf(System.nanoTime()));
			}
		}
	}

	String name;
	Long avg;
	Long last;
	Integer i;
	@EventHandler
	public void onAnimation(PlayerAnimationEvent e) {
		if(kick.contains(e.getPlayer().getName())){
			e.setCancelled(true);
		}else{
			name = e.getPlayer().getName();
			avg = (Long) this.hitsavg.get(name);
			last = (Long) this.hitslast.get(name);
			i = (Integer) this.hitscount.get(name);
			if (i == null)
				i = Integer.valueOf(0);
			if (avg == null)
				avg = Long.valueOf(0L);
			if (last == null)
				last = Long.valueOf(0L);
			if (i.intValue() > 200) {
				if ((avg != null) && (avg.longValue() / 200L < 50000L)) {
					this.kick.add(e.getPlayer().getName());
					LoadedPlayer loadedplayer = this.client.getPlayerAndLoad( UtilPlayer.getRealUUID(e.getPlayer()) );
					loadedplayer.kickPlayer("\u00A75cStop!");
					Log("IP: "+e.getPlayer().getAddress().getAddress().getHostAddress()+" Real-UUID:"+UtilPlayer.getRealUUID(e.getPlayer())+" UUID:"+e.getPlayer().getUniqueId());
					Log("Spieler "+name+" wurde wegen Animation Crash gekickt!");
					UtilException.catchException(client.getHandle().getName(), Bukkit.getServer().getIp(), this.mysql, "Spieler "+name+" wurde wegen Animation Crash gekickt! "+" IP: "+e.getPlayer().getAddress().getAddress().getHostAddress()+" Real-UUID:"+UtilPlayer.getRealUUID(e.getPlayer())+" UUID:"+e.getPlayer().getUniqueId());
				}else{
					this.hitscount.remove(name);
					this.hitsavg.remove(name);
				}
			} else {
				if (last.longValue() != 0L) {
					this.hitsavg.put(
							name,
							Long.valueOf(avg.longValue()
									+ (System.nanoTime() - last.longValue())));
				}
				this.hitscount.put(name, Integer.valueOf(i.intValue() + 1));
				this.hitslast.put(name, Long.valueOf(System.nanoTime()));
			}
		}
	}

//	@EventHandler
//	public void packet(PacketListenerReceiveEvent ev){
//		if(ev.getPacket() instanceof PacketPlayInSetCreativeSlot){
//			kPacketPlayInSetCreativeSlot c = new kPacketPlayInSetCreativeSlot((PacketPlayInSetCreativeSlot)ev.getPacket());
//			
//			if(c.getItem()!=null&&c.getItem().hasTag()&&!c.getItem().getTag().isEmpty()){
//				Log("NBT: "+c.getItem().getTag().isEmpty());
//				Log("NBT: "+c.getItem().getEnchantments().size());
//				Log("NBT: "+c.getItem().getTag().c().size());
//				
//				for(String s : c.getItem().getTag().c()){
//					Log("N: "+s);
//					
//				}
//				
//				c.getItem().setTag(new NBTTagCompound());
//			}
//		}
//	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.switchavg.remove(e.getPlayer().getName());
		this.switchcount.remove(e.getPlayer().getName());
		this.switchlast.remove(e.getPlayer().getName());
		this.hitsavg.remove(e.getPlayer().getName());
		this.hitscount.remove(e.getPlayer().getName());
		this.hitslast.remove(e.getPlayer().getName());
		this.kick.remove(e.getPlayer().getName());
	}
}
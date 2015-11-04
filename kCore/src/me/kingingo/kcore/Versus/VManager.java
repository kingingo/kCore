package me.kingingo.kcore.Versus;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.ARENA_STATUS;
import me.kingingo.kcore.UpdateAsync.UpdateAsyncType;
import me.kingingo.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VManager extends kListener{

	private HashMap<String,ARENA_STATUS> server;
	private HashMap<Integer,ArrayList<Rule>> rules;
	@Setter
	private UpdateAsyncType updateSpeed;
	@Getter
	private HashMap<Player, VersusType> wait_list;
	
	public VManager(JavaPlugin instance,UpdateAsyncType updateSpeed){
		super(instance,"VManager");
		this.server=new HashMap<>();
		this.rules=new HashMap<>();
		this.updateSpeed=updateSpeed;
		this.wait_list=new HashMap<>();
	}
	
	public void addPlayer(Player player,VersusType type){
		if(this.wait_list.containsKey(player))this.wait_list.remove(player);
		this.wait_list.put(player, type);
	}
	
	public void addRule(Rule rule,RulePriority priority){
		if(!rules.containsKey(priority.getI()))rules.put(priority.getI(), new ArrayList<Rule>());
		rules.get(priority.getI()).add(rule);
	}
	
	/**
	 * Verteilt die Spieler
	 * @param ev
	 */
	ArrayList<Player> players;
	boolean b=false;
	VersusType type;
	@EventHandler
	public void Update(UpdateAsyncEvent ev){
		if(ev.getType()==updateSpeed&&!server.isEmpty()&&!this.wait_list.isEmpty()){
			if(players==null)this.players=new ArrayList<>();
			
			for(ARENA_STATUS arena : this.server.values()){
				if(arena.getState()==GameState.LobbyPhase){
					players.clear();
					
					for(int i = arena.getTeams(); i >= 2 ; i--){
						type=(VersusType)VersusType.byInt( i );
						
						if(this.wait_list.containsValue(type)){
							for(Player player : this.wait_list.keySet()){
								if(this.wait_list.get(player)==type){
									if(RuleCheck(player, arena,players))players.add(player);
								}
							}
						}
						
						
					}
				}
			}
		}
	}
	
	public boolean RuleCheck(Player player,ARENA_STATUS status,ArrayList<Player> players){
		this.b=true;
		for(int i=RulePriority.HIGHEST.getI(); i<=RulePriority.LOWEST.getI(); i++){
			for(Rule rule : this.rules.get(i)){
				if(!rule.onRule(player, status, players)){
					this.b=false;
					break;
				}
			}
			if(!this.b)break;
		}
		return b;
	}
	
	/**
	 * Ordnet den Status der Arenen ein
	 * @param ev
	 */
	@EventHandler
	public void receive(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof ARENA_STATUS){
			ARENA_STATUS s = (ARENA_STATUS)ev.getPacket();
			if(server.containsKey(s.getServer()+s.getArena())){
				server.get(s.getServer()+s.getArena()).Set(s.toString());
				s=null;
			}else{
				server.put(s.getServer()+s.getArena(), s);
			}
		}
	}
	
	/**
	 * Entfernt die Spieler von der Liste wenn sie Leften!
	 * @param ev
	 */
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(this.wait_list.containsKey(ev.getPlayer()))this.wait_list.remove(ev.getPlayer());
	}
	
}

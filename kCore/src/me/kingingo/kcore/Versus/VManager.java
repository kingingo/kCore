package me.kingingo.kcore.Versus;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.Team;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.ARENA_SETTINGS;
import me.kingingo.kcore.Packet.Packets.ARENA_STATUS;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.UpdateAsync.UpdateAsyncType;
import me.kingingo.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import me.kingingo.kcore.Util.UtilBG;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.CustomTimingsHandler;

public class VManager extends kListener{

	private HashMap<String,ARENA_STATUS> server;
	private HashMap<Integer,ArrayList<Rule>> rules;
	@Setter
	private UpdateAsyncType updateSpeed;
	@Getter
	private HashMap<Player, VersusType> wait_list;
	@Getter
	private CustomTimingsHandler timings;
	private PacketManager packetManager;
	private StatsManager statsManager;
	
	public VManager(PacketManager packetManager,StatsManager statsManager,UpdateAsyncType updateSpeed){
		super(packetManager.getInstance(),"VManager");
		this.server=new HashMap<>();
		this.rules=new HashMap<>();
		this.updateSpeed=updateSpeed;
		this.wait_list=new HashMap<>();
		this.timings=new CustomTimingsHandler("VManager");
		this.packetManager=packetManager;
		this.statsManager=statsManager;
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
	HashMap<Team,ArrayList<Player>> players;
	int players_size = 0;
	int team;
	int team_size;
	ARENA_SETTINGS settings;
	VersusType type;
	boolean br;
	Player owner;
	@EventHandler
	public void Update(UpdateAsyncEvent ev){
		if(ev.getType()==updateSpeed&&!server.isEmpty()&&!this.wait_list.isEmpty()){
			this.timings.startTiming();
			if(this.players==null){
				this.players=new HashMap<>();
				for(Team t : VersusType._TEAMx6.getTeam())players.put(t, new ArrayList<Player>());
			}
				
			for(ARENA_STATUS arena : this.server.values()){
				if(arena.getState()==GameState.LobbyPhase){
					for(Team t : players.keySet())players.get(t).clear();
					
					for(int i = arena.getTeams(); i >= 2 ; i--){
						this.type=(VersusType)VersusType.byInt( i );
						this.team=0;
						this.br=false;
						this.players_size=0;
						
						for(Player player : this.wait_list.keySet()){
							if(this.wait_list.get(player)==this.type)this.players_size++;
						}
						
						if(this.players_size<this.type.getTeam().length){
							break;
						}
						
						if(this.wait_list.containsValue(type)){
							for(Player player : this.wait_list.keySet()){
								if(this.wait_list.get(player)==this.type){
									if(this.owner==null){
										this.owner=player;
										arena.setMin_team(statsManager.getInt(Stats.TEAM_MIN, this.owner));
										arena.setMax_team(statsManager.getInt(Stats.TEAM_MAX, this.owner));
										
										if(arena.getMin_team()*this.type.getTeam().length>this.players_size){
											br=true;
											break;
										}
										
										arena.setKit(this.owner.getName());
									}
									if(this.owner==player || RuleCheck(owner, player, arena,this.players)){
										this.players.get(this.type.getTeam()[this.team]).add(player);
										this.team++;
										if(this.type.getTeam().length==this.team){
											this.team=0;
										}
									}
								}
							}
							
							if(br)break;
							
							for(Team t : this.type.getTeam()){
								this.team_size=this.players.get(t).size();
								for(Team t1 : this.type.getTeam()){
									if(this.players.get(t1).size() < this.team_size){
										this.team_size=-1;
										break;
									}
								}
								
								if(this.team_size!=-1)break;
							}
							
							arena.setState(GameState.Laden);
							this.settings=new ARENA_SETTINGS(this.type, arena.getArena(), arena.getKit(), this.owner, Team.BLACK, arena.getMin_team(), arena.getMax_team());
							for(Team t : this.type.getTeam()){
								if(this.players.get(t).size()>this.team_size){
									for(int a = 0; a<(this.players.get(t).size()-this.team_size); a++){
										if(this.players.get(t).get(0)==this.owner){
											a--;
											continue;
										}
										this.players.get(t).remove(0);
									}
								}
								
								for(Player player : this.players.get(t)){
									this.settings.setTeam(t);
									this.settings.setPlayer(player.getName());
									this.packetManager.SendPacket(arena.getServer(), this.settings);
								}
							}
							
							for(Team t : this.type.getTeam()){
								for(Player player : this.players.get(t)){
									arena.setOnline(arena.getOnline()+1);
									UtilBG.sendToServer(player, arena.getServer(), this.packetManager.getInstance());
								}
								this.players.get(t).clear();
							}
							break;
						}
					}
					
					if(this.wait_list.isEmpty())break;
				}
			}
			this.timings.stopTiming();
		}
	}

	boolean b=false;
	public boolean RuleCheck(Player owner,Player player,ARENA_STATUS status,HashMap<Team,ArrayList<Player>> players){
		this.b=true;
		for(int i=RulePriority.HIGHEST.getI(); i<=RulePriority.LOWEST.getI(); i++){
			for(Rule rule : this.rules.get(i)){
				if(!rule.onRule(owner,player, status, players)){
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

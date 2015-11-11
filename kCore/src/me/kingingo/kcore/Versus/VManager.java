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
import me.kingingo.kcore.Util.UtilDebug;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.CustomTimingsHandler;

public class VManager extends kListener{

	private HashMap<String,ARENA_STATUS> server;
	private HashMap<Integer,ArrayList<Rule>> rules;
	@Setter
	private UpdateAsyncType updateSpeed;
	@Getter
	private HashMap<VersusType, ArrayList<Player>> wait_list;
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
		for(RulePriority prio : RulePriority.values())this.rules.put(prio.getI(), new ArrayList<Rule>());
		for(VersusType type : VersusType.values())this.wait_list.put(type, new ArrayList<Player>());
	}
	
	public boolean addPlayer(Player player,VersusType type){
		if(type==removePlayer(player))return false;
		this.wait_list.get(type).add(player);
		return true;
	}
	
	public VersusType removePlayer(Player player){
		for(VersusType t : this.wait_list.keySet()){
			if(this.wait_list.get(t).contains(player)){
				this.wait_list.get(t).remove(player);
				return t;
			}
		}
		return null;
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
	int team_remove;
	ARENA_SETTINGS settings;
	VersusType type;
	boolean br;
	boolean ba;
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
					if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Found Arena: "+arena.getArena(),"Server: "+arena.getServer(),"Teams: "+arena.getTeams()});
					for(Team t : players.keySet())players.get(t).clear();
					
					for(int i = arena.getTeams(); i >= 2 ; i--){
						this.type=(VersusType)VersusType.byInt( i );
						this.team=0;
						this.owner=null;
						this.ba=false;
						this.team_size=-2;
						this.br=false;
						this.players_size=this.wait_list.get(type).size();
						
						if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Type: "+type.name(),"Waiter: "+this.players_size});
						
						if(this.players_size<this.type.getTeam().length){
							continue;
						}
						
						if(this.wait_list.containsKey(type)&&!this.wait_list.get(type).isEmpty()){
							for(Player player : this.wait_list.get(type)){
									if(this.owner==null){
										if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", "Owner: "+player.getName());
										this.owner=player;
										
										if(type==VersusType._TEAMx2){
											arena.setMin_team(1);
											arena.setMax_team(1);
										}else{
											arena.setMin_team(statsManager.getInt(Stats.TEAM_MIN, this.owner));
											arena.setMax_team(statsManager.getInt(Stats.TEAM_MAX, this.owner));
										}
										
										if(arena.getMin_team()*this.type.getTeam().length>this.players_size){
											br=true;
											break;
										}
										
										arena.setKit(this.owner.getName());
										if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Min: "+arena.getMin_team(),"Max: "+arena.getMax_team()});
									}
									if(this.owner==player || RuleCheck(this.owner, player, type, arena,this.players)){
										this.players.get(this.type.getTeam()[this.team]).add(player);
										if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Member found: "+player.getName(),"Team: "+this.type.getTeam()[this.team].Name()});
										this.team++;
										if(this.type.getTeam().length==this.team){
											this.team=0;
										}
										
										//Prüft ob die Teams voll genug sind!
										for(Team t : this.type.getTeam()){
											if( players.get(t).size() >= arena.getMax_team() ){
												ba=true;
											}else{
												ba=false;
												break;
											}
										}
										
										if(ba){
											break;
										}
									}else{
										if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Rule NOT "+player.getName()});
									}
							}
							
							if(br)continue;
							
							for(Team t : this.type.getTeam()){
								if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"SEARCH Team: "+t.name(),this.players.get(t).size()+"<"+arena.getMin_team()});
								if(this.players.get(t).size()<arena.getMin_team()){
									if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Too less player","Team: "+t.name(),this.players.get(t).size()+"<"+arena.getMin_team()});
									br=true;
									break;
								}
							}
							
							if(br){
								continue;
							}
							
							for(Team t : this.type.getTeam()){
								this.team_size=this.players.get(t).size();
								if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Team: "+t.Name(),"Size: "+this.team_size});
								for(Team t1 : this.type.getTeam()){
									if(this.players.get(t1).size() < this.team_size){
										if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"littler Team: "+t1.Name(),"Size: "+this.players.get(t1).size()});
										this.team_size=-1;
										break;
									}
								}
								
								if(this.team_size!=-1)break;
							}
							
							if(this.team_size<=0){
								if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", "LITTLER BY "+this.team_size);
								break;
							}
							
							if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", "Team Size: "+this.team_size);
							
							
							arena.setState(GameState.Laden);
							this.settings=new ARENA_SETTINGS(this.type, arena.getArena(), arena.getKit(), this.owner, Team.BLACK, arena.getMin_team(), arena.getMax_team());
							if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", "Create Settings");
							
							for(Team t : this.type.getTeam()){
								if(this.players.get(t).size()>this.team_size){
									this.team_remove=this.players.get(t).size()-this.team_size;
									if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Team SIZE LOWER","Team Remove:"+this.team_remove});
									for(int a = 0; a<this.players.get(t).size(); a++){
										if(this.players.get(t).get(a)==this.owner){
											continue;
										}
										this.team_remove--;
										this.players.get(t).remove(a);
										
										if(team_remove==0){
											if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", new String[]{"Team NOW LOWER","SIZE:"+this.players.get(t).size(),"SIZE1:"+this.team_size});
											break;
										}
									}
								}
								
								for(Player player : this.players.get(t)){
									this.settings.setTeam(t);
									this.settings.setPlayer(player.getName());
									this.packetManager.SendPacket(arena.getServer(), this.settings);
								}
							}
							if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", "Settings send");
							
							for(Team t : this.type.getTeam()){
								for(Player player : this.players.get(t)){
									this.wait_list.get(type).remove(player);
									arena.setOnline(arena.getOnline()+1);
									UtilBG.sendToServer(player, arena.getServer(), this.packetManager.getInstance());
								}
								this.players.get(t).clear();
							}
							
							if(UtilDebug.isDebug())UtilDebug.debug("UpdateAsyncEvent", "Game Start!");
							break;
						}
					}
					
					if(isEmpty())break;
				}
			}
			this.timings.stopTiming();
		}
	}
	
	public boolean isEmpty(){
		for(VersusType type : this.wait_list.keySet()){
			if(!this.wait_list.get(type).isEmpty()&&this.wait_list.get(type).size()>=2){
				return false;
			}
		}
		return true;
	}

	boolean b=false;
	public boolean RuleCheck(Player owner,Player player,VersusType type,ARENA_STATUS status,HashMap<Team,ArrayList<Player>> players){
		this.b=true;
		for(int u=RulePriority.HIGHEST.getI(); u<RulePriority.LOWEST.getI(); u++){
			for(Rule rule : this.rules.get(u)){
				if(!rule.onRule(owner,player, type, status, players)){
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
		removePlayer(ev.getPlayer());
	}
	
}

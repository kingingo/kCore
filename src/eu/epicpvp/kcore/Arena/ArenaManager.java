package eu.epicpvp.kcore.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.epicpvp.datenclient.client.connection.PacketListener;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameState;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import eu.epicpvp.kcore.Arena.BestOf.GameRoundBestOf;
import eu.epicpvp.kcore.Enum.Team;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Packets.PacketArenaSettings;
import eu.epicpvp.kcore.Packets.PacketArenaStatus;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilBG;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;

public class ArenaManager extends kListener  {

	/*
	 * Game Rounds
	 */
	@Getter
	private HashMap<ArenaType, HashMap<Integer,GameRound>> rounds;
	@Getter
	private HashMap<UUID,Integer> rounds_player;
	@Getter
	@Setter
	private int round_counter;
	/*
	 * ArenaManager Settings
	 */
	@Getter
	private HashMap<String,PacketArenaStatus> server;
	@Getter
	private HashMap<Integer,ArrayList<Rule>> rules;
	@Setter
	private UpdateAsyncType updateSpeed;

	/*
	 * Wait list
	 */
	@Getter
	private HashMap<ArenaType, ArrayList<Player>> wait_list;

	private StatsManager statsManager;
	@Getter
	private GameType t;

	public ArenaManager(StatsManager statsManager,GameType t,UpdateAsyncType updateSpeed){
		super(statsManager.getInstance(),"ArenaManager:"+t.getShortName());
		this.t=t;
		this.server=new HashMap<>();
		this.rules=new HashMap<>();
		this.updateSpeed=updateSpeed;
		this.wait_list=new HashMap<>();
		this.statsManager=statsManager;
		this.rounds=new HashMap<>();
		this.rounds_player=new HashMap<>();
		this.round_counter=0;
		for(RulePriority prio : RulePriority.values())this.rules.put(prio.getI(), new ArrayList<Rule>());
		for(ArenaType type : ArenaType.values())this.wait_list.put(type, new ArrayList<Player>());
		for(ArenaType type : ArenaType.values())this.rounds.put(type, new HashMap<Integer,GameRound>());

		PacketArenaSettings.register();
		PacketArenaStatus.register();

		UtilServer.getClient().getHandle().getHandlerBoss().addListener(new PacketListener() {

			@Override
			public void handle(Packet packet) {
				if(packet instanceof PacketArenaStatus){
					PacketArenaStatus arena = (PacketArenaStatus) packet;

					if(arena.getType()==getT()){
						if(!server.containsKey(arena.getServer()+arena.getArena()))logMessage("ADD "+arena.getServer()+" "+arena.getArena());

						server.remove(arena.getServer()+arena.getArena());
						server.put(arena.getServer()+arena.getArena(), arena);
						arena=null;
					}
				}
			}
		});
	}

	public boolean addPlayer(Player player,ArenaType type){
		if(type==removePlayer(player))return false;
		this.wait_list.get(type).add(player);
		return true;
	}

	public ArenaType removePlayer(Player player){
		for(ArenaType t : this.wait_list.keySet()){
			if(this.wait_list.get(t).contains(player)){
				this.wait_list.get(t).remove(player);
				return t;
			}
		}
		return null;
	}

	public boolean delRound(Player player,boolean withMsg){
		if(this.rounds_player.containsKey(player.getUniqueId())){
			int c = this.rounds_player.get(player.getUniqueId());

			for(ArenaType type : ArenaType.values()){
				if(this.rounds.get(type).containsKey(c)){
					for(UUID p : this.rounds.get(type).get(c).getPlayers()){
						if(withMsg&&UtilPlayer.isOnline(p)){
							Bukkit.getPlayer(p).sendMessage(TranslationHandler.getText(Bukkit.getPlayer(p), "PREFIX")+TranslationHandler.getText(Bukkit.getPlayer(p), "HUB_VERSUS_1VS1_CANCEL"));
						}
						this.rounds_player.remove(p);
					}
					this.rounds.get(type).get(c).remove();
					this.rounds.get(type).remove(c);
					break;
				}
			}
			return true;
		}
		return false;
	}

	public boolean addRound(GameRound round){
		for(UUID player : round.getPlayers())if(this.rounds_player.containsKey(player))return false;
		for(UUID player : round.getPlayers())this.rounds_player.put(player, this.round_counter);
		this.rounds.get(round.getType()).put(this.round_counter, round);
		this.round_counter++;
		return true;
	}

	public void addRule(Rule rule,RulePriority priority){
		if(!rules.containsKey(priority.getI()))rules.put(priority.getI(), new ArrayList<Rule>());
		rules.get(priority.getI()).add(rule);
	}

	/**
	 * Verteilt die Spieler
	 * @param ev
	 */
	ArrayList<String> list;
	PacketArenaStatus arena;
	HashMap<Team,ArrayList<Player>> players;
	int players_size = 0;
	int team;
	int team_size;
	int team_remove;
	PacketArenaSettings settings;
	ArenaType type;
	boolean br;
	boolean ba;
	Player owner;
	GameRound round;
	int id;
	@EventHandler
	public void clean(UpdateAsyncEvent ev){
			if(UpdateAsyncType.MIN_16==ev.getType()){
				try{
					getRounds().clear();
					for(ArenaType type : ArenaType.values())getRounds().put(type, new HashMap<Integer,GameRound>());
					getRounds_player().clear();
					setRound_counter(0);

					getServer().clear();
					getWait_list().clear();
					for(ArenaType type : ArenaType.values())getWait_list().put(type, new ArrayList<Player>());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
	}

	@EventHandler
	public void Update(UpdateAsyncEvent ev){
		if(ev.getType()==updateSpeed&&!server.isEmpty()&&!this.wait_list.isEmpty()){
			try{
				if(this.players==null){
					this.players=new HashMap<>();
					for(Team t : ArenaType._TEAMx6.getTeam())players.put(t, new ArrayList<Player>());
				}

				if(list==null)list=new ArrayList<>();
				list.clear();
				for(String arena : this.server.keySet())this.list.add(arena);

				if(list.isEmpty()){
					if(UtilDebug.isDebug()){
						logMessage("LISTE IS EMPTY!!!!");
					}
				}

				if(UtilDebug.isDebug())logMessage("RUN... "+server.size());
				for(int r = 0; r<this.server.size(); r++){
					if(list.isEmpty()){
						break;
					}
					arena = (PacketArenaStatus)this.server.get( list.get(UtilMath.randomInteger(list.size())) );
					this.list.remove(arena.getServer()+arena.getArena());

					if(UtilDebug.isDebug())logMessage("SERVER "+arena.getServer()+" "+arena.getArena()+" "+arena.getState().name());
					if(arena.getState()==GameState.LobbyPhase){
						if(UtilDebug.isDebug())logMessage("SERVER1 "+arena.getServer()+" "+arena.getArena()+" "+arena.getState().name());

						for(Team t : players.keySet())players.get(t).clear();

						if(UtilDebug.isDebug())logMessage("arena: "+arena.getTeams());
						for(int i = arena.getTeams(); i >= 2 ; i--){
							if(UtilDebug.isDebug())logMessage("I: "+i);
							this.type=(ArenaType)ArenaType.byInt( i );
							this.team=0;
							this.owner=null;
							this.ba=false;
							this.team_size=-2;
							this.br=false;

							if(UtilDebug.isDebug()){
								logMessage("ROUNDS: "+this.rounds.containsKey(type));
								logMessage("EMPTY: "+this.rounds.get(type));
							}

							if(this.rounds.containsKey(type)&&!this.rounds.get(type).isEmpty()){
								this.id=(Integer)this.rounds.get(type).keySet().toArray()[0];
								this.round=(GameRound)this.rounds.get(type).get(id);
								if(UtilDebug.isDebug()&&this.round.getOwner()!=null&&UtilPlayer.isOnline(this.round.getOwner())&&Bukkit.getPlayer(this.round.getOwner()).getName().equalsIgnoreCase("kingingo")){
									logMessage("Owner: kingingo");
								}


								if(UtilPlayer.isOnline(this.round.getOwner())){
									this.owner=Bukkit.getPlayer(this.round.getOwner());

									if(UtilDebug.isDebug()&&this.owner.getName().equalsIgnoreCase("kingingo")){
										logMessage("Online");
									}

									for(UUID player : this.round.getPlayers()){
										if(UtilPlayer.isOnline(player)){
											this.players.get(this.type.getTeam()[this.team]).add(Bukkit.getPlayer(player));
											this.team++;
											if(this.type.getTeam().length==this.team){
												this.team=0;
											}
										}else{
											ba=true;
											break;
										}
									}

									if(!ba){
										if(type==ArenaType._TEAMx2){
											arena.setMin_team(1);
											arena.setMax_team(1);
										}else{
											arena.setMin_team(statsManager.getInt(this.owner, StatsKey.TEAM_MIN));
											arena.setMax_team(statsManager.getInt(this.owner, StatsKey.TEAM_MAX));
										}
										arena.setKit(this.owner.getName());
										arena.setState(GameState.Laden);

										this.settings=new PacketArenaSettings(this.type, arena.getArena(), arena.getKit(), this.owner, Team.BLACK, arena.getMin_team(), arena.getMax_team());

										for(Team t : this.type.getTeam()){
											for(Player player : this.players.get(t)){
												this.settings.setTeam(t);
												this.settings.setPlayer(player.getName());

												UtilServer.getClient().sendPacket(arena.getServer(), this.settings);
											}
										}

										for(Team t : this.type.getTeam()){
											for(Player player : this.players.get(t)){
												this.rounds_player.remove(player.getUniqueId());
												arena.setOnline(arena.getOnline()+1);
												logMessage("Send "+player.getName()+" to "+arena.getServer()+" "+arena.getArena());
												UtilBG.sendToServer(player, arena.getServer(), statsManager.getInstance());
											}
											this.players.get(t).clear();
										}

										if(UtilDebug.isDebug()&&this.owner.getName().equalsIgnoreCase("kingingo")){
											logMessage("ALLES GUT!");
										}

										if(!(this.round instanceof GameRoundBestOf))this.round.remove();
										this.rounds.get(type).remove(this.id);
										break;
									}else{
										if(UtilDebug.isDebug()&&this.owner.getName().equalsIgnoreCase("kingingo")){
											logMessage("MITSPIELER OFFLINE");
										}
									}

									if(!(this.round instanceof GameRoundBestOf))this.round.remove();
									this.rounds.get(type).remove(this.id);

									this.team=0;
									this.owner=null;
									this.ba=false;
									this.team_size=-2;
									this.br=false;
								}
							}

							if(!this.wait_list.containsKey(type)){
								System.err.println("TYPE: "+t.getShortName());
								System.err.println(" TYPE1:"+type);
								continue;
							}

							this.players_size=this.wait_list.get(type).size();

							if(this.players_size<this.type.getTeam().length){
								continue;
							}

							if(this.wait_list.containsKey(type)&&!this.wait_list.get(type).isEmpty()){
								for(Player player : this.wait_list.get(type)){
										if(!player.isOnline())continue;
										if(this.owner==null){
											this.owner=player;

											if(type==ArenaType._TEAMx2){
												arena.setMin_team(1);
												arena.setMax_team(1);
											}else{
												arena.setMin_team(statsManager.getInt(this.owner, StatsKey.TEAM_MIN));
												arena.setMax_team(statsManager.getInt(this.owner, StatsKey.TEAM_MAX));
											}

											if(arena.getMin_team()*this.type.getTeam().length>this.players_size){
												br=true;
												break;
											}

											arena.setKit(this.owner.getName());
										}
										if(this.owner==player || RuleCheck(this.owner, player, type, arena,this.players)){
											this.players.get(this.type.getTeam()[this.team]).add(player);
											this.team++;
											if(this.type.getTeam().length==this.team){
												this.team=0;
											}

											//Pr§§§ft ob die Teams voll genug sind!
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
										}
								}

								if(br)continue;

								for(Team t : this.type.getTeam()){
									if(this.players.get(t).size()<arena.getMin_team()){
										br=true;
										break;
									}
								}

								if(br){
									continue;
								}

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

								if(this.team_size<=0){
									break;
								}

								arena.setState(GameState.Laden);
								this.settings=new PacketArenaSettings(this.type, arena.getArena(), arena.getKit(), this.owner, Team.BLACK, arena.getMin_team(), arena.getMax_team());

								for(Team t : this.type.getTeam()){
									if(this.players.get(t).size()>this.team_size){
										this.team_remove=this.players.get(t).size()-this.team_size;
										for(int a = 0; a<this.players.get(t).size(); a++){
											if(this.players.get(t).get(a)==this.owner){
												continue;
											}
											this.team_remove--;
											this.players.get(t).remove(a);

											if(team_remove==0){
												break;
											}
										}
									}

									for(Player player : this.players.get(t)){
										this.settings.setTeam(t);
										this.settings.setPlayer(player.getName());
										UtilServer.getClient().sendPacket(arena.getServer(), this.settings);
									}
								}

								for(Team t : this.type.getTeam()){
									for(Player player : this.players.get(t)){
										this.wait_list.get(type).remove(player);
										arena.setOnline(arena.getOnline()+1);
										UtilBG.sendToServer(player, arena.getServer(), statsManager.getInstance());
									}
									this.players.get(t).clear();
								}

								break;
							}
						}

						if(isEmpty())break;
					}
				}

			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	public boolean isEmpty(){
		for(ArenaType type : this.wait_list.keySet()){
			if(!this.wait_list.get(type).isEmpty()&&this.wait_list.get(type).size()>=2){
				return false;
			}
			if(!this.rounds.get(type).isEmpty()){
				return false;
			}
		}
		return true;
	}

	boolean b=false;
	public boolean RuleCheck(Player owner,Player player,ArenaType type,PacketArenaStatus status,HashMap<Team,ArrayList<Player>> players){
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
	 * Entfernt die Spieler von der Liste wenn sie Leften!
	 * @param ev
	 */
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		removePlayer(ev.getPlayer());
		delRound(ev.getPlayer(),false);
	}

}

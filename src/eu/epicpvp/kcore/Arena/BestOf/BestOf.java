package eu.epicpvp.kcore.Arena.BestOf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.datenclient.client.connection.PacketListener;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import eu.epicpvp.kcore.Arena.ArenaManager;
import eu.epicpvp.kcore.Command.Admin.CommandLocations;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Packets.PacketArenaWinner;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.Title;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class BestOf extends kListener{

	private HashMap<GameType,ArenaManager> games;
	private HashMap<UUID,GameRoundBestOf> players;
	private HashMap<GameRoundBestOf,Long> wait_list;
	@Getter
	private Location spawn;
	private ItemStack item;
	@Getter
	private HashMap<UUID,String> players_name;

	public BestOf(JavaPlugin instance){
		super(instance,"BestOf");
		UtilServer.setBestOf(this);
		this.games=new HashMap<>();
		this.players=new HashMap<>();
		this.wait_list=new HashMap<>();
		this.players_name=new HashMap<>();
		this.spawn=CommandLocations.getLocation("BestOf");
		this.item=UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY, 1,(byte)14), "§cBestOf Runde beenden");

		System.out.println("test: "+(UtilServer.getClient().getHandle()==null));
		System.out.println("test: "+(UtilServer.getClient().getHandle().getHandlerBoss()==null));

		PacketArenaWinner.register();
		UtilServer.getClient().getHandle().getHandlerBoss().addListener(new PacketListener() {

			@Override
			public void handle(Packet packet) {
				if(packet instanceof PacketArenaWinner){
					PacketArenaWinner win = (PacketArenaWinner)packet;

					if(players.containsKey(win.getWinner())){
						players.get(win.getWinner()).win(win.getWinner());
					}
				}
			}
		});
	}

	public void addGame(ArenaManager arenaManager){
		games.put(arenaManager.getT(), arenaManager);
	}

	public void removeBestOf(UUID player){
		if(players.containsKey(player)){
			GameRoundBestOf round = players.get(player);
			this.players.remove(round.getPlayers()[0]);
			this.players.remove(round.getPlayers()[1]);
			this.wait_list.remove(round);
			players_name.remove(round.getPlayers()[0]);
			players_name.remove(round.getPlayers()[1]);

			if(UtilPlayer.isOnline(round.getPlayers()[0])){
				Bukkit.getPlayer(round.getPlayers()[0]).teleport(CommandLocations.getLocation("spawn"));
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().clear();
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().setItem(0, UtilItem.RenameItem(new ItemStack(Material.CHEST), TranslationHandler.getText(Bukkit.getPlayer(round.getPlayers()[0]), "HUB_ITEM_CHEST")));
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().setItem(1,UtilItem.RenameItem(new ItemStack(Material.FISHING_ROD), "§aBestOf"));
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().setItem(5,UtilItem.RenameItem(new ItemStack(Material.BOW), "§aSurvivalGames 1vs1"));
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().setItem(6,UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "§aBedWars 1vs1"));
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().setItem(8,UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§aVersus 1vs1"));
				Bukkit.getPlayer(round.getPlayers()[0]).getInventory().setItem(7,UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§aSkyWars 1vs1"));

				for(Player p : UtilServer.getPlayers()){
					p.showPlayer(Bukkit.getPlayer(round.getPlayers()[0]));
					Bukkit.getPlayer(round.getPlayers()[0]).showPlayer(p);
				}
			}

			if(UtilPlayer.isOnline(round.getPlayers()[1])){
				Bukkit.getPlayer(round.getPlayers()[1]).teleport(CommandLocations.getLocation("spawn"));
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().clear();
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().setItem(0, UtilItem.RenameItem(new ItemStack(Material.CHEST), TranslationHandler.getText(Bukkit.getPlayer(round.getPlayers()[1]), "HUB_ITEM_CHEST")));
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().setItem(1,UtilItem.RenameItem(new ItemStack(Material.FISHING_ROD), "§aBestOf"));
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().setItem(5,UtilItem.RenameItem(new ItemStack(Material.BOW), "§aSurvivalGames 1vs1"));
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().setItem(6,UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "§aBedWars 1vs1"));
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().setItem(8,UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§aVersus 1vs1"));
				Bukkit.getPlayer(round.getPlayers()[1]).getInventory().setItem(7,UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§aSkyWars 1vs1"));

				for(Player p : UtilServer.getPlayers()){
					p.showPlayer(Bukkit.getPlayer(round.getPlayers()[1]));
					Bukkit.getPlayer(round.getPlayers()[1]).showPlayer(p);
				}
			}

			round.remove();
		}
	}

	public void sendHologramm(GameRoundBestOf round){
		for(UUID u : round.getPlayers())if(UtilPlayer.isOnline(u))round.getNameTagMessage().sendToPlayer(Bukkit.getPlayer(u));
	}

	public void createBestOf(Player player, Player target){
		if(this.players.containsKey(player.getUniqueId()))removeBestOf(player.getUniqueId());
		if(this.players.containsKey(target.getUniqueId()))removeBestOf(target.getUniqueId());

		this.players_name.put(player.getUniqueId(), player.getName());
		this.players_name.put(target.getUniqueId(), target.getName());

		for(UUID p : this.players.keySet()){
			if(UtilPlayer.isOnline(p)){
				Bukkit.getPlayer(p).hidePlayer(player);
				Bukkit.getPlayer(p).hidePlayer(target);
				target.hidePlayer(Bukkit.getPlayer(p));
				player.hidePlayer(Bukkit.getPlayer(p));
			}
		}

		GameRoundBestOf round = new GameRoundBestOf(player, target);

		this.players.put(player.getUniqueId(), round);
		this.players.put(target.getUniqueId(), round);
		player.teleport(this.spawn);
		target.teleport(this.spawn);
		player.getInventory().clear();
		player.getInventory().addItem(this.item);
		target.getInventory().clear();
		target.getInventory().addItem(this.item);
		this.wait_list.put(round, System.currentTimeMillis());
	}

	public GameType random(){
		switch(UtilMath.randomInteger(4)){
		case 0:return GameType.Versus;
		case 1:return GameType.SurvivalGames1vs1;
		case 2:return GameType.BedWars1vs1;
		case 3:return GameType.SkyWars1vs1;
		}
		return GameType.Versus;
	}

	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.RIGHT)&&ev.getPlayer().getGameMode()!=GameMode.CREATIVE){
			if(ev.getPlayer().getItemInHand().getType()==this.item.getType()){
				if(this.players.containsKey(ev.getPlayer().getUniqueId())){
					removeBestOf(ev.getPlayer().getUniqueId());
				}
			}
		}
	}

	ArrayList<GameRoundBestOf> list;
	@EventHandler
	public void updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SLOW || this.wait_list.isEmpty())return;
		if(list==null)list=new ArrayList<>();

		for(GameRoundBestOf round : this.wait_list.keySet()){

			if((System.currentTimeMillis()-this.wait_list.get(round)) > TimeSpan.SECOND*5){
				if(round.getOwner().toString().equalsIgnoreCase("3e6028c4-c078-4835-bb3a-33c516f69bd8")){
					logMessage("1: "+(System.currentTimeMillis()-this.wait_list.get(round)));
				}
				if(round!=null&&round.canNextRound()){
					if(round.getOwner().toString().equalsIgnoreCase("3e6028c4-c078-4835-bb3a-33c516f69bd8")){
						logMessage("2: NEXT ");
					}
					GameType type = round.nextRound();
					if(type!=GameType.NONE){
						this.games.get((type==null?random():type)).addRound(round);
					}else{
						UUID winner = round.getWinner();

						if(winner==null){
							String t = UtilServer.getUserData().getConfig(Bukkit.getPlayer(round.getOwner())).getString("BestOf.Round.0");

							if(t.equalsIgnoreCase("random")){
								this.games.get(random()).addRound(round);
							}else{
								this.games.get(GameType.get(t)).addRound(round);
							}
						}else{
							if(UtilPlayer.isOnline(winner)){
								Title title = new Title("§a§lBestOf WINNER", "§c§l"+Bukkit.getPlayer(winner).getName());
								for(UUID player : round.getPlayers()){
									if(UtilPlayer.isOnline(player)){
										title.send(Bukkit.getPlayer(player));
									}
								}

								removeBestOf(round.getOwner());
							}
						}
					}
					list.add(round);
				}else if((System.currentTimeMillis()-this.wait_list.get(round)) > TimeSpan.SECOND*15){
					if(round.getOwner().toString().equalsIgnoreCase("3e6028c4-c078-4835-bb3a-33c516f69bd8")){
						logMessage("2: NOT FOUND ");
					}
					removeBestOf(round.getOwner());
					list.add(round);
				}else{
					System.err.println("[ERROR]: "+this.players_name.get(round.getPlayers()[0])+" / "+this.players_name.get(round.getPlayers()[1]));
					System.err.println("[ERROR]: "+round.canNextRound());
					System.err.println("[ERROR]: "+UtilPlayer.isOnline(round.getPlayers()[0]));
					System.err.println("[ERROR]: "+UtilPlayer.isOnline(round.getPlayers()[1]));
					System.err.println("[ERROR]: "+(System.currentTimeMillis()-this.wait_list.get(round)));
					System.err.println("[ERROR]: "+(round==null));
				}
			}
		}

		for(GameRoundBestOf r : list)this.wait_list.remove(r);
		list.clear();
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PlayerJoinEvent ev){
		if(this.players.containsKey(ev.getPlayer().getUniqueId())){
			ev.getPlayer().teleport(this.spawn);
			sendHologramm(this.players.get(ev.getPlayer().getUniqueId()));
			ev.getPlayer().getInventory().clear();
			ev.getPlayer().getInventory().addItem(this.item);

			for(UUID p : this.players.keySet()){
				if(UtilPlayer.isOnline(p)){
					if(this.players.get(ev.getPlayer().getUniqueId()).getPlayers()[0].toString().equalsIgnoreCase(p.toString()))continue;
					if(this.players.get(ev.getPlayer().getUniqueId()).getPlayers()[1].toString().equalsIgnoreCase(p.toString()))continue;
					Bukkit.getPlayer(p).hidePlayer(ev.getPlayer());
					ev.getPlayer().hidePlayer(Bukkit.getPlayer(p));
				}
			}

			new Title("§c"+this.players.get(ev.getPlayer().getUniqueId()).getRound()+"/"+this.players.get(ev.getPlayer().getUniqueId()).getTypes().length +" Runde"," ").send(ev.getPlayer());

			if(this.players.get(ev.getPlayer().getUniqueId()).canNextRound()){
				this.wait_list.put(this.players.get(ev.getPlayer().getUniqueId()), System.currentTimeMillis());
			}
		}
	}
}

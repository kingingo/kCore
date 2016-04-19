package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spigotmc.AsyncCatcher;

import dev.wolveringer.client.threadfactory.ThreadFactory;
import eu.epicpvp.kcore.Permission.Events.PlayerLoadPermissionEvent;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class PermissionManager{
	private static PermissionManager manager;

	public static PermissionManager getManager() {
		return manager;
	}

	public static void setManager(PermissionManager manager) {
		PermissionManager.manager = manager;
		Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
		Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.PLAYER_LIST);
	}

	@Getter
	private ArrayList<Group> groups = new ArrayList<>();
	@Getter
	private HashMap<Integer, PermissionPlayer> user = new HashMap<>();
	@Getter
	protected JavaPlugin instance;
	@Getter
	protected PermissionChannelHandler handler;
	@Getter
	private GroupTyp type;
	
	public PermissionManager(JavaPlugin instance,GroupTyp type) {
		this.instance = instance;
		this.type=type;
		this.handler = new PermissionChannelHandler(this);
		Bukkit.getMessenger().registerIncomingPluginChannel(instance, "permission", handler);
		Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "permission");
		UtilServer.setPermissionManager(this);
	}
	
	public Scoreboard getScoreboard(){
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		for(Player p : UtilServer.getPlayers()){
			
			if(board.getTeam(getPermissionPlayer(p).getGroups().get(0).getName()) == null){
				UtilScoreboard.addTeam(board, getPermissionPlayer(p).getGroups().get(0).getName(), null,  getPermissionPlayer(p).getGroups().get(0).getPrefix());
			}
			UtilScoreboard.addPlayerToTeam(board, getPermissionPlayer(p).getGroups().get(0).getName(), p);
		}
		
		return board;
	}

	public void setTabList(Player player) {
		setTabList(player, true);
	}
	
	public void setTabList(Player player,boolean callEvent){
		try{
			AsyncCatcher.catchOp("");
		}catch(Exception e){
			Bukkit.getScheduler().runTask(instance, new Runnable() { //Call sync
				@Override
				public void run() {
					setTabList(player, callEvent);
				}
			});
			return;
		}
		if(getPermissionPlayer(player)==null){
			System.err.println("PermissionPlayer from "+player.getName()+" == NULL ["+getClass().getName()+"]");
			return;
		}
		
		if(getPermissionPlayer(player).getGroups().isEmpty()){
			System.err.println(player.getName()+" has not any groups ["+getClass().getName()+"]");
			return;
		}
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		String prefix = getPrefix(player);
		String suffix = "";
		String groupName = getTabGroup(player);
		
		UtilScoreboard.addTeam(player.getScoreboard(), groupName, null, prefix,suffix);
		UtilScoreboard.addPlayerToTeam(player.getScoreboard(), groupName, player);
		
		for(Player p : UtilServer.getPlayers()){
			if(p.equals(player))
				continue;
			if(getPermissionPlayer(p) == null){
				System.err.println("Cant find permissionplayer for "+p.getName()+" ["+getClass().getName()+"]");
				continue;
			}
			String tabGroup = getTabGroup(p);
			String pp = getPrefix(p);
			Team t1 = UtilScoreboard.setTeamPrefix(player.getScoreboard(), tabGroup, pp); //Atomaticly create if not exist
			Team t2 = UtilScoreboard.setTeamPrefix(p.getScoreboard(), getTabGroup(player), prefix);//Atomaticly create if not exist
			if(UtilServer.getClient().getPlayer(p.getName()) != null)
				UtilScoreboard.addPlayerToTeam(player.getScoreboard(), t1, p);
			if(UtilServer.getClient().getPlayer(player.getName()) != null)
				UtilScoreboard.addPlayerToTeam(p.getScoreboard(), t2, player);
		}
		Bukkit.getPluginManager().callEvent(new PlayerSetScoreboardEvent(player));
		if(callEvent){
			Bukkit.getPluginManager().callEvent(new PlayerLoadPermissionEvent(this, getPermissionPlayer(player)));
		}
	}
	
	public void unloadPlayer(Player player){
		user.remove(UtilPlayer.getPlayerId(player));
	}
	
	public void loadPlayer(Player player,int playerId) {
		if (!user.containsKey(playerId))
			new PermissionPlayer(player,this, playerId);
	}

	public PermissionPlayer getPermissionPlayer(Player player){
		int playerId = UtilPlayer.getPlayerId(player);
		PermissionPlayer pplayer = getPermissionPlayer(playerId);
		return pplayer;
	}
	
	public PermissionPlayer getPermissionPlayer(int playerId) {
		return user.get(playerId);
	}

	public boolean hasPermission(Player player, String permission) {
		return hasPermission(UtilPlayer.getPlayerId(player), permission);
	}
	
	public boolean hasPermission(Player player, PermissionType teamMessage) {
		return hasPermission(UtilPlayer.getPlayerId(player), teamMessage.getPermissionToString());
	}
	
	public boolean hasPermissionType(Player player, PermissionType teamMessage) {
		return hasPermission(UtilPlayer.getPlayerId(player), teamMessage.getPermissionToString());
	}
	
	public boolean hasPermission(Player player, PermissionType teamMessage,boolean message) {
		return hasPermission(player, teamMessage.getPermissionToString(), message);
	}
	
	public String getTabGroup(Player player){
		String name = player.getName();
		PermissionPlayer pplayer = getPermissionPlayer(player);
		if(pplayer.getGroups().size() != 0)
			name = String.format("%03d", 999-pplayer.getGroups().get(0).getImportance()) + name;
		else
			name = "000"+name;
		if(name.length()>16)
			name = name.substring(0, 15);
		return name;
	}
	
	public String getPrefix(Player player){
		PermissionPlayer pplayer = getPermissionPlayer(player);
		if(pplayer.getGroups().size() == 0)
			return "§6§m";
		String prefix = pplayer.getGroups().get(0).getPrefix();
		if(prefix.length()>16)
			prefix = prefix.substring(0,16);
		return prefix;
	}
	
	public boolean hasPermission(Player player, String permission, boolean message) {
		boolean perm = hasPermission(UtilPlayer.getPlayerId(player), permission);
		if (message && !perm) ;
			//player.sendMessage(Language.getText(player, "PREFIX") + "??cYou don't have permission to do that."); //TODO fix error
		return perm;
	}

	public boolean hasPermission(int playerId, PermissionType permission) {
		return hasPermission(playerId, permission.getPermissionToString());
	}

	public boolean hasPermission(int playerId, String permission) {
		if(!user.containsKey(playerId)) return false;
		return user.get(playerId).hasPermission(permission) || user.get(playerId).hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString());
	}

	public void addPermission(Player player,PermissionType type){
		getPermissionPlayer(player).addPermission(type.getPermissionToString());
	}
	
	public Group getGroup(String name) {
		for(Group g : groups)
			if(g.getName().equalsIgnoreCase(name))
				return g;
		return null;
	}
	
	public Group loadGroup(String name){
		Group g = getGroup(name);
		if(g == null){
			groups.add(g = new Group(this, name));
		}
		if(!g.isLoaded())
			g.reload();
		return g;
	}

	public void unloadGroup(Group g){
		groups.remove(g);
	}
	
	protected void updatePlayer(int playerId) {
		if(user.containsKey(playerId))
			user.remove(playerId);
			if(UtilPlayer.isOnline(playerId)){
				ThreadFactory.getFactory().createThread(new Runnable() {
					@Override
					public void run() {
						loadPlayer(UtilPlayer.searchExact(playerId),playerId);
						setTabList(UtilPlayer.searchExact(playerId), false);
					}
				}).start();
			}
	}
	
	protected void updateGroup(String group){
		Bukkit.getScheduler().runTask(manager.getInstance(), new Runnable() {
			@Override
			public void run() {
				groups.remove(getGroup(group));
				if(Bukkit.getOnlinePlayers().size() > 0)
					loadGroup(group);
				for(PermissionPlayer p : new ArrayList<PermissionPlayer>(user.values()))
					updatePlayer(p.getPlayerId());
			}
		});
	}
}

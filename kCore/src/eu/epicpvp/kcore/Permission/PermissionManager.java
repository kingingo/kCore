package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Permission.Listener.PermissionListener;
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
	}

	@Getter
	private ArrayList<Group> groups = new ArrayList<>();
	@Getter
	private HashMap<UUID, PermissionPlayer> user = new HashMap<>();
	@Getter
	protected JavaPlugin instance;
	@Getter
	protected PermissionChannelHandler handler;
	
	public PermissionManager(JavaPlugin plugin) {
		this.instance = plugin;
		this.handler = new PermissionChannelHandler(this);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "permission", handler);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "permission");
		new PermissionListener(this);
	}

	public Scoreboard getScoreboard(){
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		for(Group g : getGroups()){
			UtilScoreboard.addTeam(board, g.getName(), g.getPrefix());
		}
		
		for(Player player : UtilServer.getPlayers()){
			board.getTeam(getPlayer(player).getGroups().get(0).getName()).addPlayer(player);
		}
		return board;
	}
	
	public void setTabList(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		
		for(Group group : getGroups())UtilScoreboard.addTeam(player.getScoreboard(), group.getName(), group.getPrefix());
		
		for(Player p : UtilServer.getPlayers()){
			if(p.getScoreboard().getTeam(getPlayer(player).getGroups().get(0).getName())!=null){
				UtilScoreboard.addTeam(player.getScoreboard(), getPlayer(player).getGroups().get(0).getName(), getPlayer(player).getGroups().get(0).getPrefix());
			}
			
			UtilScoreboard.addPlayerToTeam(player.getScoreboard(), getPlayer(p).getGroups().get(0).getName(), p);
			UtilScoreboard.addPlayerToTeam(p.getScoreboard(), getPlayer(player).getGroups().get(0).getName(), player);
		}
	}
	
	public void loadPlayer(Player p,UUID player) {
		if (!user.containsKey(player))
			user.put(player, new PermissionPlayer(p,this, player));
	}

	public PermissionPlayer getPlayer(Player player){
		return getPlayer(UtilPlayer.getRealUUID(player));
	}
	
	public PermissionPlayer getPlayer(UUID player) {
		return user.get(player);
	}

	public boolean hasPermission(Player player, String permission) {
		return hasPermission(player.getUniqueId(), permission);
	}
	
	public boolean hasPermission(Player player, PermissionType teamMessage) {
		return hasPermission(player.getUniqueId(), teamMessage.getPermissionToString());
	}
	
	public boolean hasPermissionType(Player player, PermissionType teamMessage) {
		return hasPermission(player.getUniqueId(), teamMessage.getPermissionToString());
	}
	
	public boolean hasPermission(Player player, PermissionType teamMessage,boolean message) {
		return hasPermission(player, teamMessage.getPermissionToString(), message);
	}
	
	public String getPrefix(Player player){
		return getPlayer(player).getGroups().get(0).getPrefix();
	}
	
	public boolean hasPermission(Player player, String permission, boolean message) {
		boolean perm = hasPermission(player.getUniqueId(), permission);
		if (message && !perm) ;
			//player.sendMessage(Language.getText(player, "PREFIX") + "??cYou don't have permission to do that."); //TODO fix error
		return perm;
	}

	public boolean hasPermission(UUID uuid, PermissionType permission) {
		return hasPermission(uuid, permission.getPermissionToString());
	}

	public boolean hasPermission(UUID uuid, String permission) {
		if(!user.containsKey(uuid))
			return false;
		return user.get(uuid).hasPermission(permission);
	}

	public void addPermission(Player player,PermissionType type){
		getPlayer(player).addPermission(type.getPermissionToString());
	}
	
	public Group getGroup(String name) {
		for(Group g : groups)
			if(g.getName().equalsIgnoreCase(name))
				return g;
		return null;
	}
	
	public Group loadGroup(String name){
		if(getGroup(name) == null){
			groups.add(new Group(this, name));
		}
		return getGroup(name);
	}
}

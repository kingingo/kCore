package me.kingingo.kcore.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Language.Listener.LanguageListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Language {
	
	@Getter
	private static HashMap<LanguageType,HashMap<String,String>> list;
	private static LanguageListener listener;
	@Getter
	private static HashMap<UUID,LanguageType> languages;
	@Getter
	@Setter
	private static MySQL mysql;
	
	public static void loadLanguage(LanguageType type){
		try{				
			ResultSet rs = mysql.Query("SELECT type,name,msg FROM language WHERE type='"+type.getDef()+"'");
			 while (rs.next()){
				 if(LanguageType.get(rs.getString(1))==null)continue;
				 if(!list.containsKey(LanguageType.get(rs.getString(1))))list.put(LanguageType.get(rs.getString(1)), new HashMap<String,String>());
				 list.get(LanguageType.get(rs.getString(1))).put(rs.getString(2), rs.getString(3));
			 }
			 rs.close();
		}catch (SQLException e){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
		}
		addAll(type);	
	}
	
	public static void updateLanguage(Player player,LanguageType type){
		languages.remove(UtilPlayer.getRealUUID(player));
		languages.put(UtilPlayer.getRealUUID(player), type);
		mysql.Update("UPDATE language_user SET language='"+type.getDef()+"' WHERE UUID='" + UtilPlayer.getRealUUID(player) + "'");
	}
	
	public static void load(MySQL mysql){
		setMysql(mysql);
		if(listener==null)listener=new LanguageListener(mysql.getInstance());
		list=new HashMap<>();
		languages=new HashMap<>();
		mysql.Update("CREATE TABLE IF NOT EXISTS language(type varchar(30),name varchar(30),msg varchar(30))");
		mysql.Update("CREATE TABLE IF NOT EXISTS language_user(uuid varchar(100),language varchar(100))");
		loadLanguage(LanguageType.GERMANY);
	}

	public static LanguageType getLanguage(Player player){
		return languages.get(UtilPlayer.getRealUUID(player));
	}
	
	public static void sendText(Player player,String name,Object[] input){
		player.sendMessage( toText(list.get(getLanguage(player)).get(name), input) );
	}
	
	public static void sendText(Player player,String name,Object input){
		player.sendMessage( toText(list.get(getLanguage(player)).get(name), input) );
	}
	
	public static String getText(String name,Object input){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name);
			return "";
		}
		return toText(list.get(LanguageType.GERMANY).get(name), input);
	}
	
	public static String getText(String name,Object[] input){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name);
			return "";
		}
		return toText(list.get(LanguageType.GERMANY).get(name), input);
	}
	
	public static String getText(String name){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name);
			return "";
		}
		return list.get(LanguageType.GERMANY).get(name);
	}
	
	public static String getText(Player player,String name){
		if(!list.get(getLanguage(player)).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name+" "+getLanguage(player).getDef());
			return "";
		}
		return list.get(getLanguage(player)).get(name);
	}
	
	public static String getText(Player player,String name,Object[] input){
		if(!list.get(getLanguage(player)).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name+" "+getLanguage(player).getDef());
			return "";
		}
		return toText(list.get(getLanguage(player)).get(name), input);
	}
	
	public static String getText(Player player,String name,Object input){
		if(!list.get(getLanguage(player)).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name+" "+getLanguage(player).getDef());
			return "";
		}
		return toText(list.get(getLanguage(player)).get(name), input);
	}
	
	public static String toText(String msg,Object input){
		return (msg.contains("{INPUT0}") ? msg.replaceAll("\\{INPUT0\\}", String.valueOf(input)) : msg);
	}
	
	public static String toText(String msg,Object[] input){
		for(int i = 0 ; i < input.length ; i++){
			msg=msg.replaceFirst("\\{INPUT"+i+"\\}", String.valueOf(input[i]));
		}
		return msg;
	}
	
	public static void addAll(LanguageType type){
		if(type==LanguageType.ENGLISH){
			if(!list.containsKey(LanguageType.ENGLISH))list.put(LanguageType.ENGLISH, new HashMap<String,String>());
			add(LanguageType.ENGLISH, "TREASURE_CHEST_TIME_AWAY", "§cThe time has expired!");
			add(LanguageType.ENGLISH, "VERSUS_ADDED", "§aYou where added to the match making queue!");
			add(LanguageType.ENGLISH, "VERSUS_PLACE", "§7You are at position §e{INPUT0} in the queue.");
			add(LanguageType.ENGLISH, "TRACKING_RANGE", "§7The player tracking range ist now §e{INPUT0}§7.");
			add(LanguageType.ENGLISH, "NO_CHARAKTER", "§cThere are invalid characters in your name!");
			add(LanguageType.ENGLISH, "STATS_PREFIX", "§b[===========§6 §lSTATS §b===========]");
			add(LanguageType.ENGLISH, "STATS_PREFIXBY", "§b[=========== §6§l{INPUT0}s stats§b ===========]");
			add(LanguageType.ENGLISH, "STATS_KILLS", "§6Kills : §7");
			add(LanguageType.ENGLISH, "STATS_DEATHS", "§6Deaths : §7");
			add(LanguageType.ENGLISH, "STATS_MONEY", "§6Money : §7");
			add(LanguageType.ENGLISH, "STATS_KDR", "§6KDR : §7");
			add(LanguageType.ENGLISH, "STATS_GILDE", "§6Gild : §7");
			add(LanguageType.ENGLISH, "STATS_RANKING", "§6Rank : §7");
			add(LanguageType.ENGLISH, "PREMIUM_PET", "§cBuy §6Premium§c to activate this feature!");
			add(LanguageType.ENGLISH, "TREASURE_CHEST_TOO_NEAR", "§cYou are too close to another TreasureChest!");
			add(LanguageType.ENGLISH, "CHAT_MESSAGE_BLOCK", "§cDo not use that kind of language!");
			add(LanguageType.ENGLISH, "SPIELER_ENTFERNT_COMPASS", "§7Player §a{INPUT0}§7 is §e{INPUT1}§7 blocks away from you!");
			add(LanguageType.ENGLISH, "AUßERHALB_DER_MAP", "§cYou are out of the map!");
			add(LanguageType.ENGLISH, "COINS_DEL_PLAYER", "§7Removed §c{INPUT1}§7 coins from §a{INPUT0}§7!");
			add(LanguageType.ENGLISH, "COINS_ADD_PLAYER", "§7Gave §a{INPUT1}§7 coins to §a{INPUT0}§7!");
			add(LanguageType.ENGLISH, "CMD_MUTE", "§cCommands disabled!");
			add(LanguageType.ENGLISH, "CMD_UNMUTE", "§aCommands enabled!");
			add(LanguageType.ENGLISH, "PVP_MUTE", "§aPvP disabled!");
			add(LanguageType.ENGLISH, "PVP_UNMUTE", "§cPvP enabled!");
			add(LanguageType.ENGLISH, "CHAT_MUTE", "§cChat muted!");
			add(LanguageType.ENGLISH, "CHAT_UNMUTE", "§aChat unmuted!");
			add(LanguageType.ENGLISH, "MORE_HAND", "§aYou now have 64x.");
			add(LanguageType.ENGLISH, "MORE_INV", "§aAll your items were rounded to 64x!");
			add(LanguageType.ENGLISH, "ENDERGAMES_TELEPORT", "§7You switched positions with §a{INPUT0}§7!");
			add(LanguageType.ENGLISH, "KIT_SHOP_ADD", "§aYou selected the kit§e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "KIT_SHOP_NO_MONEY", "§cYou do not have enough §7{INPUT0}§c!");
			add(LanguageType.ENGLISH, "KIT_SHOP_BUYED_KIT", "§aYou successfully purchased §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "PLAYER_IS_OFFLINE", "§e{INPUT0}§c is currently offline!");
			add(LanguageType.ENGLISH, "FIGHT_START_IN", "§7You can start fighting in §e{INPUT0}§7 seconds!");
			add(LanguageType.ENGLISH, "FIGHT_START", "§aYou can now fight!");
			add(LanguageType.ENGLISH, "GAME_END_IN", "§cThe game ends in §e{INPUT0}§c ");
			add(LanguageType.ENGLISH, "GAME_END", "§cThe game ended.");
			add(LanguageType.ENGLISH, "RESTART_IN", "§4§lServer restart in  §e§l{INPUT0}§4§l seconds!");
			add(LanguageType.ENGLISH, "RESTART", "§4§lThe server restarts now!");
			add(LanguageType.ENGLISH, "SERVER_FULL", "§cServer is full!");
			add(LanguageType.ENGLISH, "SERVER_NOT_LOBBYPHASE", "§cThis server is currently not in lobby mode!");
			add(LanguageType.ENGLISH, "SERVER_FULL_WITH_PREMIUM", "§cThis server is full! §cBuy §6Premium§c to join full servers! &7[&6shop.EpicPvP.de&7]");
			add(LanguageType.ENGLISH, "KILL_BY", "§a{INPUT0}§7 was killed by §e{INPUT1}§7!");
			add(LanguageType.ENGLISH, "DEATH", "§a{INPUT0}§7 died!");
			add(LanguageType.ENGLISH, "GAME_EXCLUSION", "§c{INPUT0} was excluded!");
			add(LanguageType.ENGLISH, "PERK_NOT_BOUGHT", "§cYou did not purchase any perks yet! §6Shop.EpicPvP.de");
			add(LanguageType.ENGLISH, "VOTE_TEAM_ADD", "§aYou joined team §7{INPUT0}§a");
			add(LanguageType.ENGLISH, "VOTE_TEAM_MIN_PLAYER", "§cThere have to be atleast {INPUT0} players online!");
			add(LanguageType.ENGLISH, "VOTE_TEAM_REMOVE", "§aYou left team §7{INPUT0}§a!");
			add(LanguageType.ENGLISH, "VOTE_TEAM_FULL", "§7{INPUT0}§c is full!");
			add(LanguageType.ENGLISH, "VOTE_MIN", "§cThere have to be atleast §e{INPUT0}§c players online for this game to start!");
			add(LanguageType.ENGLISH, "KICKED_BY_PREMIUM", "§cYou were kicked so a premium player can join! Want that too? §6shop.EpicPvP.de");
			add(LanguageType.ENGLISH, "PREFIX_GAME", "§6{INPUT0} §8» 7");
			add(LanguageType.ENGLISH, "PREFIX", "§6EpicPvP §8» §7");
			add(LanguageType.ENGLISH, "TEAM_WIN", "§aTeam {INPUT0}§a won the game!");
			add(LanguageType.ENGLISH, "TEAM_OUT", "§7{INPUT0}§c has fallen!");
			add(LanguageType.ENGLISH, "RESTART_FROM_ADMIN", "§cThe server was restarted by an administrator!");
			add(LanguageType.ENGLISH, "SCHUTZZEIT_END_IN", "§7PvP enables in §e{INPUT0}§7 seconds!");
			add(LanguageType.ENGLISH, "SCHUTZZEIT_END", "§aPvP is now enabled, you can now fight!");
			add(LanguageType.ENGLISH, "GAME_START", "§aGame started!");
			add(LanguageType.ENGLISH, "GAME_START_IN", "§7The game starts in §e{INPUT0}§7 seconds!");
			add(LanguageType.ENGLISH, "DEATHMATCH_START", "§aThe deathmatch started!");
			add(LanguageType.ENGLISH, "DEATHMATCH_START_IN", "§7The deathmatch starts in §e{INPUT0}§7 seconds!");
			add(LanguageType.ENGLISH, "DEATHMATCH_END", "§cThe deathmatch ended!");
			add(LanguageType.ENGLISH, "GAME_WIN", "§7{INPUT0}§a won the game!");
			add(LanguageType.ENGLISH, "SURVIVAL_GAMES_DISTRICT_WIN", "§aDistrict §e{INPUT0}§a with players §7{INPUT1}§a and §7{INPUT2}§a won!");
			add(LanguageType.ENGLISH, "DEATHMATCH_END_IN", "§cThe deathmatch ends in §e{INPUT0}§c seconds!");
			add(LanguageType.ENGLISH, "LOGIN_MESSAGE", "§cPlease log in with: §e/Login <Password>");
			add(LanguageType.ENGLISH, "KICK_BY_FALSE_PASSWORD", "§cYou entered a wrong password §e{INPUT0}§c times");
			add(LanguageType.ENGLISH, "LOGIN_ACCEPT", "§aYou successfully logged in!");
			add(LanguageType.ENGLISH, "LOGIN_DENY", "§cWrong password!");
			add(LanguageType.ENGLISH, "REGISTER_ACCEPT", "§aRegisterd successfully!");
			add(LanguageType.ENGLISH, "REGISTER_MESSAGE", "§cPlease register using: §e/Register <Password>");
			add(LanguageType.ENGLISH, "SPECTATOR_CHAT_CANCEL", "§cSpectators ca not chat!");
			add(LanguageType.ENGLISH, "TTT_WIN", "§aThe §7{INPUT0}§a won the game!");
			add(LanguageType.ENGLISH, "TTT_DEATH", "§a{INPUT0}§7 died an was §e{INPUT1}§7!");
			add(LanguageType.ENGLISH, "TTT_TESTER_WAS_USED", "§cThe tester is currently unavailable!");
			add(LanguageType.ENGLISH, "TTT_TRAITOR_CHAT", "§7To use the §cTraitor-Chat §ctype: §c/tc [Message]§7!");
			add(LanguageType.ENGLISH, "TTT_TESTER_USED", "§cThe tester is currently in use!");
			add(LanguageType.ENGLISH, "TTT_TESTER_TIME", "§cYou allready used the tester, please come back later!");
			add(LanguageType.ENGLISH, "TTT_TRAITOR_SHOP", "§cYou do not have enough traitor points!");
			add(LanguageType.ENGLISH, "TTT_PASSE_USE", "§aYou used a §6{INPUT0}§a pass! You have §e{INPUT1}§a left!");
			add(LanguageType.ENGLISH, "TTT_PASSE_KEINE", "§cYou do not have any {INPUT0}§c passes!");
			add(LanguageType.ENGLISH, "TTT_PASSE_LOBBYPHASE", "§cThe lobby phase is over...");
			add(LanguageType.ENGLISH, "TTT_PASSE_MAX_USED", "§cThere were allready to many §e{INPUT0}§c passes used!");
			add(LanguageType.ENGLISH, "TTT_SHOP", "§cYou do not have enough points!");
			add(LanguageType.ENGLISH, "TTT_SHOP_BUYED", "§aYou successfully purchased an item!");
			add(LanguageType.ENGLISH, "TTT_NPC_CLICKED", "§7This is §a{INPUT0}§7 he was §e{INPUT1}§7.");
			add(LanguageType.ENGLISH, "TTT_DNA_TEST", "§7The DNA test shows that §a{INPUT0}§7 was killed by §e{INPUT1}§7.");
			add(LanguageType.ENGLISH, "TTT_TRAITOR_SHOP_RADAR_CHANGE", "§aYou selected §a{INPUT0}§7!");
			add(LanguageType.ENGLISH, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH", "§cIt is to late for this player!");
			add(LanguageType.ENGLISH, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT", "§aYou successfully revived §7{INPUT0}§a!");
			add(LanguageType.ENGLISH, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER", "§aYou were revived by §7{INPUT0}§a!");
			add(LanguageType.ENGLISH, "TTT_TESTER_JOIN", "§a{INPUT0}§7 entered the tester!");
			add(LanguageType.ENGLISH, "TTT_LEICHE_IDENTIFIZIERT", "§a{INPUT0}s§7corps was found, he was §e{INPUT1}§7!");
			add(LanguageType.ENGLISH, "TTT_IS_NOW", "§7You are §c{INPUT0}§7.");
			add(LanguageType.ENGLISH, "COINS_ADD", "§aYou recieved §e{INPUT0}§a coins!");
			add(LanguageType.ENGLISH, "COINS_DEL", "§e{INPUT0}§c coisn have been deducted from you!");
			add(LanguageType.ENGLISH, "GAME_AUSGESCHIEDEN", "§a{INPUT0}§c was removed from the game!");
			add(LanguageType.ENGLISH, "CAVEWARS_SPIDER_DEATH", "§7Team {INPUT0}s§7 sheep was slain by §a{INPUT1}§7!");
			add(LanguageType.ENGLISH, "SHEEPWARS_SHEEP_DEATH", "§7Team {INPUT0}§7 lost their sheep!");
			add(LanguageType.ENGLISH, "BEDWARS_BED_BROKE", "§7Team {INPUT0}§7 lost their bed!");
			add(LanguageType.ENGLISH, "GILDE_MONEY_DEPOSIT", "§a{INPUT0}§7 deposited §a{INPUT1} Epics§7!");
			add(LanguageType.ENGLISH, "GILDE_MONEY_LIFTED", "§a{INPUT0}§7 has withdrew§c {INPUT1} Epics§7!");
			add(LanguageType.ENGLISH, "GILDE_NOT_ENOUGH_MONEY", "§cThere is not enough money on the guild account.");
			add(LanguageType.ENGLISH, "GILDE_CREATE", "§aYou successfully founded §6{INPUT0}§a! The guild-home was set at your current position.");
			add(LanguageType.ENGLISH, "GILDE_PLAYER_OFFLINE", "§e{INPUT0}§c is currently offline!");
			add(LanguageType.ENGLISH, "GILDE_PREFIX", "§6Guild §8» §7");
			add(LanguageType.ENGLISH, "GILDE_PLAYER_ENTRE", "§a{INPUT0}§7 joined the guild!");
			add(LanguageType.ENGLISH, "GILDE_NAME_LENGTH_MIN", "§cThe guild name has to be longer than §e{INPUT0}§c characters!");
			add(LanguageType.ENGLISH, "GILDE_NAME_LENGTH_MAX", "§cThe guild name has to be shorter than §e{INPUT0}§c characters!");
			add(LanguageType.ENGLISH, "GILDE_EXIST", "§cThis guild allready exists!");
			add(LanguageType.ENGLISH, "GILDE_EXIST_NOT", "§cThis guild does not exist!");
			add(LanguageType.ENGLISH, "GILDE_DELETE", "§cThe guild was deleted!");
			add(LanguageType.ENGLISH, "GILDE_EINLADEN", "§aYou invited §e{INPUT0}§a to the guild!");
			add(LanguageType.ENGLISH, "GILDE_STATS_PREFIX", "§b[=========== §6§lGuilds §b===========]");
			add(LanguageType.ENGLISH, "GILDE_STATS_PREFIXBY", "§b[=========== §6§l{INPUT0}s informations§b ===========]");
			add(LanguageType.ENGLISH, "GILDE_EILADUNG", "§aYou were invited to join the guild §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "GILDE_CLOSED", "§cThis guild was closed!");
			add(LanguageType.ENGLISH, "GILDE_KICK_PLAYER", "§e{INPUT0}§c was kicked from guild!");
			add(LanguageType.ENGLISH, "GILDE_SETHOME", "§aGuilde home set!");
			add(LanguageType.ENGLISH, "GILDE_SETISLAND", "§aGuild island created!");
			add(LanguageType.ENGLISH, "GILDEN_NAME", "§cThere is an invalid character in your guild name!");
			add(LanguageType.ENGLISH, "GILDE_TELEPORT_CANCELLED", "§cTeleportation cancled!");
			add(LanguageType.ENGLISH, "GILDE_TELEPORTET", "§aTeleportation successfull!");
			add(LanguageType.ENGLISH, "GILDE_HOME", "§7Teleporting in §e{INPUT0}§7 ...");
			add(LanguageType.ENGLISH, "GILDE_PLAYER_NICHT_EINGELADEN", "§cYou have not been invited into a guild!");
			add(LanguageType.ENGLISH, "GILDE_PLAYER_JOIN", "§a{INPUT0}§7 joined the server!");
			add(LanguageType.ENGLISH, "FRIEND_HIT", "§cYou really want to kill your friend?");
			add(LanguageType.ENGLISH, "FRIEND_PREFIX", "§6Friend §8» §7");
			add(LanguageType.ENGLISH, "FRIEND_EXIST", "§cYou are allready friends with §a{INPUT0}§c!");
			add(LanguageType.ENGLISH, "FRIEND_SEND", "§aYou send §e{INPUT0}§a a friend request!");
			add(LanguageType.ENGLISH, "FRIEND_NOT", "§cYou are not friends with §e{INPUT0}§c!");
			add(LanguageType.ENGLISH, "FRIEND_DEL", "§cYour friendship with §e{INPUT0}§c ended!");
			add(LanguageType.ENGLISH, "FRIEND_DEL_IN", "§aIn §6{INPUT0}§a seconds your friendship with §e{INPUT1}§a ends!");
			add(LanguageType.ENGLISH, "FRIEND_GET", "§aYou recieved a friend request from §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "FRIEND_NOW", "§aYou are now friends with §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "FRIEND_ASK_NOT", "§aYou do not have any friend request right now.");
			add(LanguageType.ENGLISH, "FRIEND_IS_FRIEND", "§aYou are allready friends with §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "FRIEND_YOURE_SELF", "§cFor ever alone?");
			add(LanguageType.ENGLISH, "NEULING_SCHUTZ", "§e{INPUT0}§c is a freshman! You ca not attak him!");
			add(LanguageType.ENGLISH, "NEULING_SCHUTZ_YOU", "§cYou are still a freshman so you cannot other players! Type §7/neuling§c to deactivate this protection.");
			add(LanguageType.ENGLISH, "NEULING_END", "§aYou are no longer a Freshman!");
			add(LanguageType.ENGLISH, "NEULING_CMD", "§cYou are not a Freshman.");
			add(LanguageType.ENGLISH, "ANTI_LOGOUT_FIGHT", "§cYou are now fighting, DO NOT log out!");
			add(LanguageType.ENGLISH, "ANTI_LOGOUT_FIGHT_END", "§aYou are no longer fighting!");
			add(LanguageType.ENGLISH, "TELEPORT_VERZÖGERUNG", "§7Teleporting in §e{INPUT0}");
			add(LanguageType.ENGLISH, "TELEPORT", "§aSuccessfully teleported!");
			add(LanguageType.ENGLISH, "TELEPORT_HERE", "§e{INPUT0}§a was teleported successfully!");
			add(LanguageType.ENGLISH, "ACCEPT", "§aYou accepted!");
			add(LanguageType.ENGLISH, "DENY", "§cYou denied!");
			add(LanguageType.ENGLISH, "ACCEPT_FROM", "§e{INPUT0}§a accepeted the teleport request!");
			add(LanguageType.ENGLISH, "DENY_FROM", "§e{INPUT0}§e denied the teleport request!");
			add(LanguageType.ENGLISH, "NO_ANFRAGE", "§cYou do not have a pending request.");
			add(LanguageType.ENGLISH, "ME", "me");
			add(LanguageType.ENGLISH, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", "§e{INPUT0}§a is asking you to teleport to them. Use §7/tpaccept§a or §7/tpdeny");
			add(LanguageType.ENGLISH, "TELEPORT_ANFRAGE_EMPFÄNGER", "§e{INPUT0}§a is asking to teleport to you. Use §7/tpaccept§a or §7/tpdeny");
			add(LanguageType.ENGLISH, "TELEPORT_ANFRAGE_SENDER", "§aSend teleport request to §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "WARP_EXIST", "§cThis warp does not exist.");
			add(LanguageType.ENGLISH, "WARP_SET", "§aWarp §e{INPUT0}§a set!");
			add(LanguageType.ENGLISH, "KIT_USE", "§aYou selected kit §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "KIT_SET", "§aKit §e{INPUT0}§a saved!");
			add(LanguageType.ENGLISH, "KIT_EXIST", "§cThis kit does not exist!");
			add(LanguageType.ENGLISH, "KIT_DEL", "§aKit §e{INPUT0}§a removed!");
			add(LanguageType.ENGLISH, "KIT_DELAY", "§cYou can use the kit again in §e{INPUT0}§c!");
			add(LanguageType.ENGLISH, "CLEARINVENTORY", "§7Your inventory was cleared!");
			add(LanguageType.ENGLISH, "CLEARINVENTORY_OTHER", "§7Your inventory was cleared by §a{INPUT0}§7!");
			add(LanguageType.ENGLISH, "CLEARINVENTORY_FROM_OTHER", "§aYou cleared §e{INPUT0}s§a inventory!");
			add(LanguageType.ENGLISH, "CLEARINVENTORY_ALL", "§aYou cleared the inventory of all players!");
			add(LanguageType.ENGLISH, "NO_ITEM_IN_HAND", "§cYou do not have an item in your hand!");
			add(LanguageType.ENGLISH, "ITEM_RENAME", "§aItem renamed!");
			add(LanguageType.ENGLISH, "NO_ANSWER_PARTNER", "§cYou have noone to respond to.");
			add(LanguageType.ENGLISH, "SOCIALSPY_ON", "§aSocialspy activated!");
			add(LanguageType.ENGLISH, "SOCIALSPY_OFF", "§cSocialspy deactivated!");
			add(LanguageType.ENGLISH, "MONEY_NO_DOUBLE", "§cNot a valid number or 0!");
			add(LanguageType.ENGLISH, "MONEY_ENOUGH_MONEY", "§cYou do not have enough Epics!");
			add(LanguageType.ENGLISH, "MONEY_SEND_TO", "§aYou send §e{INPUT0}§7 {INPUT1}§a Epics!");
			add(LanguageType.ENGLISH, "MONEY_RECEIVE_FROM", "§aYou recieved §7{INPUT1} Epics§a from §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "FALLDOWN_BREWITEM_BLOCKED", "§cYou can use this item in §e{INPUT0}§c!");
			add(LanguageType.ENGLISH, "VANISH_AN", "§aVanish activated!");
			add(LanguageType.ENGLISH, "VANISH_AUS", "§cVanish deactivated!");
			add(LanguageType.ENGLISH, "UNKNOWN_CMD", "§7Much command. Such no. Many confuse. Wow.");
			add(LanguageType.ENGLISH, "NO_PERMISSION", "§cMany permissions, Such denied, Much no, Wow.");
			add(LanguageType.ENGLISH, "WHEREIS_TEXT", "§7You are currently on the §e{INPUT0}§7 server.");
			add(LanguageType.ENGLISH, "FALLDOWN_NICHT_GENUG_POWER", "§cYou do not have enough power!");
			add(LanguageType.ENGLISH, "SKYBLOCK_HAVE_ISLAND", "§cYou allready own an island!");
			add(LanguageType.ENGLISH, "SKYBLOCK_NO_ISLAND", "§cYou do not own an island!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_NO", "§cYou currently do not have a party going!");
			add(LanguageType.ENGLISH, "SKYBLOCK_REMOVE_ISLAND", "§cYour island was deleted!");
			add(LanguageType.ENGLISH, "SKYBLOCK_CHANGE_BIOME", "§aYou islands biome was changed to §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "SKYBLOCK_CREATE_ISLAND", "§aIsland created!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PLAYER_KICK", "§aYou kicked §e{INPUT0}§a from your island!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PLAYER_KICKED", "§cYou were kicked from the island by §e{INPUT0}s§c!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PLAYER_NOT_ON_YOUR_ISLAND", "§e{INPUT0}§a is not on your island!");
			add(LanguageType.ENGLISH, "NO_BEFEHL", "§cThis command does not exist!");
			add(LanguageType.ENGLISH, "WARPS_EMPTY","§cThere are no warps yet!");
			add(LanguageType.ENGLISH,"NOT_ENOUGH_EXP","§cYou do not have enough exp!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_EINLADEN_IS_IN", "§cThis player is allready in a party.");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_IN", "§cYou are allready in a party. Use §7/party verlassen §cto leave!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_EINLADEN_NO", "§cYou were not invited to a party.");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_ENTER", "§aYou joined the party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_ENTER_BY", "§e{INPUT0}§a joined the party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_SIZE", "§cYou can only invite §e{INPUT0}§c players to your party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_VOLL", "§cThe party is full!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_EINLADEN_INVITE", "§aYou were invited to a skyblock party by §e{INPUT0}§a. To join use §7/party annehmen");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_EINLADEN_IS", "§e{INPUT0}§c is allready invited to this party.");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_ERSTELLT", "§aSkyblock-party successfully created!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_NO_OWNER", "§cYou are not the host of this party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_EINLADEN", "§aYou invited §e{INPUT0}§a to the party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_VERLASSEN", "§aYou left the party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_KICKEN", "§aYou kicked §e{INPUT0}§a from the party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_SCHLIEßEN", "§cParty was closed!");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_PLAYER_NOT", "§cThis player is not in your party!");
			add(LanguageType.ENGLISH, "SKYBLOCK_TELEPORT_HOME", "§aYou were teleported t you island!"); 
			add(LanguageType.ENGLISH, "SKYBLOCK_PREFIX", "§[=========== §6§lSkyBlock §b===========]");
			add(LanguageType.ENGLISH, "SKYBLOCK_PARTY_PREFIX", "§b[=========== §6§lSkyBlock-Party §b===========]");
			add(LanguageType.ENGLISH, "SKYBLOCK_REMOVE_ISLAND_ONE", "§cYou can only delete your island once a day or create a guild island as an §bUltra §c ranked member!");
			add(LanguageType.ENGLISH, "SIGN_SHOP_NO_ITEM_ON_INV", "§cYou do not have this in your inventory or not enough of it!");
			add(LanguageType.ENGLISH, "SIGN_SHOP_VERKAUFT_", "§7You sold §6{INPUT0}§7 times §e{INPUT1}:{INPUT2}§7 and recieved §a{INPUT3}§7 Epics!");
			add(LanguageType.ENGLISH, "SIGN_SHOP_VERKAUFT", "§7You sold §6{INPUT0}§7 times §e{INPUT1}§7 and recieved §a{INPUT2}§7 Epics!");
			add(LanguageType.ENGLISH, "SIGN_SHOP_DELAY", "§cYou can only sell something every §e{INPUT0}§c seconds!");
			add(LanguageType.ENGLISH, "SIGN_SHOP_GET", "§7You bought §6{INPUT0}§7 times §e{INPUT1}§7 and paid §a{INPUT2} Epics §7!");
			add(LanguageType.ENGLISH, "SIGN_SHOP_GET_", "§7You bought §6{INPUT0}§7 times §e{INPUT1}:{INPUT2}§7 and paid §a{INPUT3} Epics §7!");
			add(LanguageType.ENGLISH, "kFLY_NOT_ON", "§cFlymode is not activated");
			add(LanguageType.ENGLISH, "NO_INTEGER", "§cThis is not a number!");
			add(LanguageType.ENGLISH, "kFLY_SPEED", "§aFly speed was changed to §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "kFLY_ON", "§aFlymode activated!");
			add(LanguageType.ENGLISH, "kFLY_OFF", "§cFlymode deactivated!");
			add(LanguageType.ENGLISH, "kFLY_PVP_FLAG", "§cYou are not allowed to fly in the PvP-area!");
			add(LanguageType.ENGLISH, "SPAWN_SET", "§aSpawnpoint set!");
			add(LanguageType.ENGLISH, "SPAWN_TELEPORT", "§aYou teleported to spawn!");
			add(LanguageType.ENGLISH, "GIVEALL", "§a{INPUT0}§7 gave all players §6{INPUT1}§7 times §e{INPUT2}§7!");
			add(LanguageType.ENGLISH, "REPAIR_HAND", "§aThe selected item was repaired!");
			add(LanguageType.ENGLISH, "REPAIR_ALL", "§aRepaired all items!");
			add(LanguageType.ENGLISH, "FEED", "§aYou are saturated!");
			add(LanguageType.ENGLISH, "FEED_ALL", "§7Your hunger was saturated by §a{INPUT0}§7!");
			add(LanguageType.ENGLISH, "FEED_OTHER", "§aYou saturated §e{INPUT0}s§a hunger!");
			add(LanguageType.ENGLISH, "HEAL", "§aYou are back to full health!");
			add(LanguageType.ENGLISH, "HEAL_ALL", "§e{INPUT0}§a is back to full health!");
			add(LanguageType.ENGLISH, "HEAL_OTHER", "§aYou healed §e{INPUT0}§a!");
			add(LanguageType.ENGLISH, "DAY", "§aIt is now 4:55 am.");
			add(LanguageType.ENGLISH, "NIGHT", "§aIt is now 6:34 pm.");
			add(LanguageType.ENGLISH, "SUN", "§aThe sun is now shineing.");
			add(LanguageType.ENGLISH, "USE_ENDERPEARL_TIME", "§cYou can use enderpearls again in §e{INPUT0}§c!");
			add(LanguageType.ENGLISH, "USE_BEFEHL_TIME", "§cYou can use this command again in §e{INPUT0}§c!");
			add(LanguageType.ENGLISH, "NO_RANG", "§cMany permissions, such denied, much no, Wow.");
			add(LanguageType.ENGLISH, "HOME_SET", "§7Home §e{INPUT0}§7 saved!");
			add(LanguageType.ENGLISH, "HOME_EXIST", "§cThis home does not exist.");
			add(LanguageType.ENGLISH, "HOME_DEL", "§cHome §e{INPUT0}§c removed!");
			add(LanguageType.ENGLISH, "HOME_SKYBLOCK_DELETE", "§e{INPUT0}s§a homes removed!");
			add(LanguageType.ENGLISH, "HOME_MAX", "§cYou can only set §e{INPUT0}§c homes!");
			add(LanguageType.ENGLISH, "HOME_QUESTION", "§a{INPUT0}§7 is asking to set a home. §e/homeaccept §7or §e/homedeny");
			add(LanguageType.ENGLISH, "HOME_ISLAND", "§aA request to set a home was send!");
			add(LanguageType.ENGLISH, "WARP_DEL", "§aWarp §e{INPUT0}§a removed!");
			add(LanguageType.ENGLISH, "WARP_EXIST", "§cThis warp does not exist!");
			add(LanguageType.ENGLISH, "GILDE_PLAYER_LEAVE", "§a{INPUT0}§7 left the server!");
			add(LanguageType.ENGLISH, "VOTE_THX", "§aThank you for §a§lVoting§a! You recieved your reward!");
			add(LanguageType.ENGLISH, "ANIT_LOGOUT_FIGHT_CMD", "§cYou cannot use §e/gilden home§c in combat!");
			add(LanguageType.ENGLISH,"XMAS_DOOR","§aYou opened the door and recieved §e{INPUT0}§a coins!");
			add(LanguageType.ENGLISH,"XMAS_DAY","§cIst heute der {INPUT0}te?");
			add(LanguageType.ENGLISH,"LOGIN_FAIL","§cInvaild character!");
			add(LanguageType.ENGLISH,"LOAD_PLAYER_DATA","§7Loading your player information...");
			add(LanguageType.ENGLISH,"PET_MUST_BUYED_IN_SHOP","§cYou have to purchase this pet! §6Shop.EpicPvP.de");
			add(LanguageType.ENGLISH,"GAME_TIME_CHANGE","§aTime changed to {INPUT0}!");
		}
		
		if(type==LanguageType.GERMANY){
			if(!list.containsKey(LanguageType.GERMANY))list.put(LanguageType.GERMANY, new HashMap<String,String>());
	        add(LanguageType.GERMANY, "TREASURE_CHEST_TIME_AWAY", "§cDie Zeit ist abgelaufen!");
	        add(LanguageType.GERMANY, "VERSUS_ADDED", "§aDu wurdest zur Warteliste hinzugefuegt!");
	        add(LanguageType.GERMANY, "VERSUS_PLACE", "§7Du befindest dich auf Platz §e{INPUT0}");
	        add(LanguageType.GERMANY, "TRACKING_RANGE", "§7Die Player Tracking Range ist nun §e{INPUT0}§7.");
	        add(LanguageType.GERMANY, "NO_CHARAKTER", "§cEs sind ungueltige Zeichen im Namen!");
	        add(LanguageType.GERMANY, "STATS_PREFIX", "§b[===========§6 §lSTATS §b===========]");
	        add(LanguageType.GERMANY, "STATS_PREFIXBY", "§b[=========== §6§lStats von {INPUT0}§b ===========]");
	        add(LanguageType.GERMANY, "STATS_KILLS", "§6Kills : §7");
	        add(LanguageType.GERMANY, "STATS_DEATHS", "§6Tode : §7");
	        add(LanguageType.GERMANY, "STATS_MONEY", "§6Geld : §7");
	        add(LanguageType.GERMANY, "STATS_KDR", "§6KDR : §7");
	        add(LanguageType.GERMANY, "STATS_GILDE", "§6Gilde : §7");
	        add(LanguageType.GERMANY, "STATS_RANKING", "§6Ranking : §7");
	        add(LanguageType.GERMANY, "PREMIUM_PET", "§cKaufe dir §6Premium§c um diese Funktion nutzen zu koennen!");
	        add(LanguageType.GERMANY, "TREASURE_CHEST_TOO_NEAR", "§cDu bist zu nah an einer anderen TreasureChest!");
	        add(LanguageType.GERMANY, "CHAT_MESSAGE_BLOCK", "§cBitte unterlasse solche Woerter im Chat!");
	        add(LanguageType.GERMANY, "SPIELER_ENTFERNT_COMPASS", "§7Der Spieler §a{INPUT0}§7 ist §e{INPUT1}§7 Bloecke weit von dir enfternt!");
	        add(LanguageType.GERMANY, "AUßERHALB_DER_MAP", "§cDu bist außerhalb der Map!");
	        add(LanguageType.GERMANY, "COINS_DEL_PLAYER", "§7Dem Spieler §a{INPUT0}§7 wurden §c{INPUT1}§7 entfernt!");
	        add(LanguageType.GERMANY, "COINS_ADD_PLAYER", "§7Dem Spieler §a{INPUT0}§7 wurden §a{INPUT1}§7 hinzugefuegt!");
	        add(LanguageType.GERMANY, "CMD_MUTE", "§cDie Commands wurden gesperrt!");
	        add(LanguageType.GERMANY, "CMD_UNMUTE", "§aDie Commands wurde entsperrt!");
	        add(LanguageType.GERMANY, "PVP_MUTE", "§aPvP wurde deaktiviert!");
	        add(LanguageType.GERMANY, "PVP_UNMUTE", "§cPvP wurde aktiviert!");
	        add(LanguageType.GERMANY, "CHAT_MUTE", "§cDer Chat wurde gesperrt!");
	        add(LanguageType.GERMANY, "CHAT_UNMUTE", "§aDer Chat wurde entsperrt!");
	        add(LanguageType.GERMANY, "MORE_HAND", "§aDu hast nun 64x.");
	        add(LanguageType.GERMANY, "MORE_INV", "§aDeine Items wurden auf 64x aufgerundet!");
	        add(LanguageType.GERMANY, "ENDERGAMES_TELEPORT", "§7Du hast deine Position mit §a{INPUT0}§7 gewechselt!");
	        add(LanguageType.GERMANY, "KIT_SHOP_ADD", "§aDu hast das Kit §e{INPUT0}§a gewaehlt!");
	        add(LanguageType.GERMANY, "KIT_SHOP_NO_MONEY", "§cDu hast nicht genug §7{INPUT0}§c!");
	        add(LanguageType.GERMANY, "KIT_SHOP_BUYED_KIT", "§aDu hast erfolgreich das Kit §e{INPUT0}§a gekauft!");
	        add(LanguageType.GERMANY, "PLAYER_IS_OFFLINE", "§cDer Spieler §e{INPUT0}§c ist Offline!");
	        add(LanguageType.GERMANY, "FIGHT_START_IN", "§7Ihr koennt in §e{INPUT0}§7 Sekunden Kaempfen!");
	        add(LanguageType.GERMANY, "FIGHT_START", "§aIhr koennt nun Kaempfen!");
	        add(LanguageType.GERMANY, "GAME_END_IN", "§cDas Spiel endet in §e{INPUT0}§c ");
	        add(LanguageType.GERMANY, "GAME_END", "§cDas Spiel wurde beendet.");
	        add(LanguageType.GERMANY, "RESTART_IN", "§4§lDer Server Restartet in §e§l{INPUT0}§4§l Sekunden!");
	        add(LanguageType.GERMANY, "RESTART", "§4§lDer Server Restartet jetzt!");
	        add(LanguageType.GERMANY, "SERVER_FULL", "§cDer Server ist voll!");
	        add(LanguageType.GERMANY, "SERVER_NOT_LOBBYPHASE", "§cDieser Server ist aktuell nicht in der Lobbyphase!");
	        add(LanguageType.GERMANY, "SERVER_FULL_WITH_PREMIUM", "§cDer Server ist voll! Kaufe dir Premium auf §6shop.EpicPvP.de§c um auf volle Servern zu joinen!");
	        add(LanguageType.GERMANY, "KILL_BY", "§a{INPUT0}§7 wurde von §e{INPUT1}§7 getoetet!");
	        add(LanguageType.GERMANY, "DEATH", "§a{INPUT0}§7 ist gestorben!");
	        add(LanguageType.GERMANY, "GAME_EXCLUSION", "§c{INPUT0} wurde vom Spiel ausgeschlossen!");
	        add(LanguageType.GERMANY, "PERK_NOT_BOUGHT", "§cDu hast noch keine Perks gekauft! §6Shop.EpicPvP.de");
	        add(LanguageType.GERMANY, "VOTE_TEAM_ADD", "§aDu bist nun in Team §7{INPUT0}§a");
	        add(LanguageType.GERMANY, "VOTE_TEAM_MIN_PLAYER", "§cEs muessen min. {INPUT0} Spieler online sein!");
	        add(LanguageType.GERMANY, "VOTE_TEAM_REMOVE", "§aDu hast §7{INPUT0}§a verlassen!");
	        add(LanguageType.GERMANY, "VOTE_TEAM_FULL", "§7{INPUT0}§c ist voll!");
	        add(LanguageType.GERMANY, "VOTE_MIN", "§cEs muessen mindestens §e{INPUT0}§c Spieler online sein damit das Spiel starten kann!");
	        add(LanguageType.GERMANY, "KICKED_BY_PREMIUM", "§cDu wurdest vom Server gekickt damit ein Premium User spielen kann! Du moechtest auch? §6shop.EpicPvP.de");
	        add(LanguageType.GERMANY, "PREFIX_GAME", "§6{INPUT0}§8» §7");
	        add(LanguageType.GERMANY, "PREFIX", "§6EpicPvP §8» §7");
	        add(LanguageType.GERMANY, "TEAM_WIN", "§aDas Team {INPUT0}§a hat das Spiel Gewonnen!");
	        add(LanguageType.GERMANY, "TEAM_OUT", "§7{INPUT0}§c ist gefallen!");
	        add(LanguageType.GERMANY, "RESTART_FROM_ADMIN", "§cDer Server wurde von einem Administrator restartet!");
	        add(LanguageType.GERMANY, "SCHUTZZEIT_END_IN", "§7Die Schutzzeit endet in §e{INPUT0}§7 Sekunden!");
	        add(LanguageType.GERMANY, "SCHUTZZEIT_END", "§aDie Schutzzeit ist nun zuende, ihr koennt euch bekriegen!");
	        add(LanguageType.GERMANY, "GAME_START", "§aDas Spiel ist gestartet!");
	        add(LanguageType.GERMANY, "GAME_START_IN", "§7Das Spiel startet in §e{INPUT0}§7 Sekunden!");
	        add(LanguageType.GERMANY, "DEATHMATCH_START", "§aDas Deathmatch startet");
	        add(LanguageType.GERMANY, "DEATHMATCH_START_IN", "§7Das Deathmatch startet in §e{INPUT0}§7 Sekunden!");
	        add(LanguageType.GERMANY, "DEATHMATCH_END", "§cDas Deathmatch ist zu Ende!");
	        add(LanguageType.GERMANY, "GAME_WIN", "§aDer Spieler §7{INPUT0}§a hat das Spiel gewonnen!");
	        add(LanguageType.GERMANY, "SURVIVAL_GAMES_DISTRICT_WIN", "§aDas District §e{INPUT0}§a mit den Spielern §7{INPUT1}§a und §7{INPUT2}§a hat Gewonnen!");
	        add(LanguageType.GERMANY, "DEATHMATCH_END_IN", "§cDas Deathmatch endet in §e{INPUT0}§c Sekunden");
	        add(LanguageType.GERMANY, "LOGIN_MESSAGE", "§cBitte logge dich ein: §e/Login <Password>");
	        add(LanguageType.GERMANY, "KICK_BY_FALSE_PASSWORD", "§cDu hast §e{INPUT0}§c mal dein Password falsch eingegeben");
	        add(LanguageType.GERMANY, "LOGIN_ACCEPT", "§aDu hast dich erfolgreich eingeloggt!");
	        add(LanguageType.GERMANY, "LOGIN_DENY", "§cDas Password ist falsch!");
	        add(LanguageType.GERMANY, "REGISTER_ACCEPT", "§aRegister erfolgreich!");
	        add(LanguageType.GERMANY, "REGISTER_MESSAGE", "§cRegistriere dich bitte: §e/Register <Password>");
	        add(LanguageType.GERMANY, "SPECTATOR_CHAT_CANCEL", "§cDu kannst als Spectator nicht im Chat schreiben!");
	        add(LanguageType.GERMANY, "TTT_WIN", "§aDie §7§l{INPUT0}§a haben diese Runde gewonnen!");
	        add(LanguageType.GERMANY, "TTT_DEATH", "§7Der Spieler §a{INPUT0}§7 ist gestorben und war §e{INPUT1}§7!");
	        add(LanguageType.GERMANY, "TTT_TESTER_WAS_USED", "§cDer Tester ist im moment nicht Freigegeben!");
	        add(LanguageType.GERMANY, "TTT_TRAITOR_CHAT", "§7Um den §cTraitor-Chat§7 zu benutzten §c/tc [Nachricht]§7!");
	        add(LanguageType.GERMANY, "TTT_TESTER_USED", "§cDer Tester ist momentan nicht frei!");
	        add(LanguageType.GERMANY, "TTT_TESTER_TIME", "§cDu warst schon im Tester kommer spaeter wieder!");
	        add(LanguageType.GERMANY, "TTT_TRAITOR_SHOP", "§cDu hast nicht genug Traitor-Punkte!");
	        add(LanguageType.GERMANY, "TTT_PASSE_USE", "§aDu hast einen §6{INPUT0}§a Pass benutzt! Du hast jetzt noch §e{INPUT1}§a!");
	        add(LanguageType.GERMANY, "TTT_PASSE_KEINE", "§cDu besitzt keine {INPUT0}§c Paesse!");
	        add(LanguageType.GERMANY, "TTT_PASSE_LOBBYPHASE", "§cDie Lobby Phase ist vorbei...");
	        add(LanguageType.GERMANY, "TTT_PASSE_MAX_USED", "§cEs wurden schon zu viele §e{INPUT0}§c Paesse benutzt!");
	        add(LanguageType.GERMANY, "TTT_SHOP", "§cDu hast nicht genug Punkte!");
	        add(LanguageType.GERMANY, "TTT_SHOP_BUYED", "§aDu hast erfolgreich ein Item erworben!");
	        add(LanguageType.GERMANY, "TTT_NPC_CLICKED", "§7Das ist §a{INPUT0}§7 er war ein §e{INPUT1}§7.");
	        add(LanguageType.GERMANY, "TTT_DNA_TEST", "§7Der DNA-TEST hat ergeben, dass der Spieler §a{INPUT0}§7 von §e{INPUT1}§7 getoetet wurde.");
	        add(LanguageType.GERMANY, "TTT_TRAITOR_SHOP_RADAR_CHANGE", "§aDu hast den Spieler §a{INPUT0}§7 ausgewaehlt!");
	        add(LanguageType.GERMANY, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH", "§cBei diesem Spieler ist es zu spaet!");
	        add(LanguageType.GERMANY, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT", "§aDu hast erfolgreich §7{INPUT0}§a wiederbelebt!");
	        add(LanguageType.GERMANY, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER", "§aDu wurdest von §7{INPUT0}§a wiederbelebt!");
	        add(LanguageType.GERMANY, "TTT_TESTER_JOIN", "§a{INPUT0}§7 ist den Tester beigetreten!");
	        add(LanguageType.GERMANY, "TTT_LEICHE_IDENTIFIZIERT", "§7Die Leiche von §a{INPUT0}§7 wurde gefunden er war ein §e{INPUT1}§7!");
	        add(LanguageType.GERMANY, "TTT_IS_NOW", "§7Du bist ein §c{INPUT0}§7.");
	        add(LanguageType.GERMANY, "COINS_ADD", "§aDu hast §e{INPUT0}§7 Coins erhalten!");
	        add(LanguageType.GERMANY, "COINS_DEL", "§cDir wurden §e{INPUT0}§c Coins abgezogen!");
	        add(LanguageType.GERMANY,"GILDE_PLAYER_IS_NOT_IN_GILDE","§cDu bist in keiner Gilde.");
	        add(LanguageType.GERMANY, "GAME_AUSGESCHIEDEN", "§a{INPUT0}§c wurde aus dem Spiel ausgeschlossen!");
	        add(LanguageType.GERMANY, "CAVEWARS_SPIDER_DEATH", "§7Das Schaf von Team {INPUT0}§7 wurde von §a{INPUT1}§7 getoetet!");
	        add(LanguageType.GERMANY, "SHEEPWARS_SHEEP_DEATH", "§7Team {INPUT0}§7 hat ihr Schaf verloren!");
	        add(LanguageType.GERMANY, "BEDWARS_BED_BROKE", "§7Team {INPUT0}§7 hat ihr Bett verloren!");
	        add(LanguageType.GERMANY, "GILDE_PLAYER_GO_OUT", "§b{INPUT0} §chat die Gilde verlassen.");
	        add(LanguageType.GERMANY, "GILDE_OWNER_NOT", "§cDu bist nicht der Owner der Gilde.");
	        add(LanguageType.GERMANY,"GILDE_PLAYER_IS_IN_GILDE","§cDu bist bereits in einer Gilde.");
	        add(LanguageType.GERMANY,"GILDE_PLAYER_IS_IN_GILDE1","§c{INPUT0} ist bereits in einer Gilde.");
	        add(LanguageType.GERMANY, "GILDE_MONEY_DEPOSIT", "§a{INPUT0}§7 hat §a{INPUT1} Epics§7 eingezahlt!");
	        add(LanguageType.GERMANY, "GILDE_MONEY_LIFTED", "§a{INPUT0}§7 hat§c {INPUT1} Epics§7 abgehoben.");
	        add(LanguageType.GERMANY, "GILDE_NOT_ENOUGH_MONEY", "§cEs ist nicht genug auf dem Gilden-Konto.");
	        add(LanguageType.GERMANY, "GILDE_CREATE", "§aDu hast erfolgreich die Gilde §6{INPUT0}§a gegruendet! Dein Gilden Home wurde auf deine aktuelle Position gesetzt.");
	        add(LanguageType.GERMANY, "GILDE_PLAYER_OFFLINE", "§cDer Spieler §e{INPUT0}§c ist offline.");
	        add(LanguageType.GERMANY, "GILDE_PREFIX", "§6Gilde §8» §7");
	        add(LanguageType.GERMANY, "GILDE_PLAYER_ENTRE", "§a{INPUT0}§7 ist der Gilde begetreten!");
	        add(LanguageType.GERMANY, "GILDE_NAME_LENGTH_MIN", "§cDer Gildenname muss laenger als §e{INPUT0}§c Zeichen sein!");
	        add(LanguageType.GERMANY, "GILDE_NAME_LENGTH_MAX", "§cDer Gildenname muss kleiner als §e{INPUT0}§c Zeichen sein!");
	        add(LanguageType.GERMANY, "GILDE_EXIST", "§cDer Gildenname wurde bereits benutzt!");
	        add(LanguageType.GERMANY, "GILDE_EXIST_NOT", "§cDiese Gilde exestiert nicht!");
	        add(LanguageType.GERMANY, "GILDE_DELETE", "§cDu hast die Gilde geloescht!");
	        add(LanguageType.GERMANY, "GILDE_EINLADEN", "§aDu hast §e{INPUT0}§a in die Gilde eingeladen!");
	        add(LanguageType.GERMANY, "GILDE_STATS_PREFIX", "§b[=========== §6§lGilden §b===========]");
	        add(LanguageType.GERMANY, "GILDE_STATS_PREFIXBY", "§b[=========== §6§lGilden Info von {INPUT0}§b ===========]");
	        add(LanguageType.GERMANY, "GILDE_EILADUNG", "§aDu wurdest in die Gilde §e{INPUT0}§a eingeladen!");
	        add(LanguageType.GERMANY, "GILDE_CLOSED", "§cDie Gilde wurde geschlossen!");
	        add(LanguageType.GERMANY, "GILDE_KICK_PLAYER", "§cDer Spieler §e{INPUT0}§c wurde aus der Gilde gekickt!");
	        add(LanguageType.GERMANY, "GILDE_SETHOME", "§aDu hast das Gildenhome gesetzt!");
	        add(LanguageType.GERMANY, "GILDE_SETISLAND", "§aDu hast eine Gildenisland erstellt!");
	        add(LanguageType.GERMANY, "GILDEN_NAME", "§cDu hast ein ungueltiges Zeichen in deinem Gildennamen!");
	        add(LanguageType.GERMANY, "GILDE_TELEPORT_CANCELLED", "§cDie Teleportation wurde abgebrochen!");
	        add(LanguageType.GERMANY, "GILDE_TELEPORTET", "§aDie Teleportation wurde durchgefuehrt!");
	        add(LanguageType.GERMANY, "GILDE_HOME", "§7Du wirst in §e{INPUT0}§7 Teleportiert...");
	        add(LanguageType.GERMANY, "GILDE_PLAYER_NICHT_EINGELADEN", "§cDu wurdest in keine Gilde eingeladen!");
	        add(LanguageType.GERMANY, "GILDE_PLAYER_JOIN", "§a{INPUT0}§7 hat den Server betreten!");
	        add(LanguageType.GERMANY,"GILDE_IS_NOT_IN_THE_GILD","§cDer Spieler §a{INPUT0}§c ist nicht in deiner Gilde!");
	        add(LanguageType.GERMANY, "FRIEND_HIT", "§cWillst du deinen Kameraden toeten?");
	        add(LanguageType.GERMANY, "FRIEND_PREFIX", "§6Freund §8» §7");
	        add(LanguageType.GERMANY, "FRIEND_EXIST", "§cDu bist bereits mit dem Spieler §a{INPUT0}§c befreundet!");
	        add(LanguageType.GERMANY, "FRIEND_SEND", "§aDu hast dem Spieler §e{INPUT0}§a eine Freundschaftsanfrage gesendet!");
	        add(LanguageType.GERMANY, "FRIEND_NOT", "§cDu bist nicht mit §e{INPUT0}§c befreundet!");
	        add(LanguageType.GERMANY, "FRIEND_DEL", "§cDeine Freundschaft zu §e{INPUT0}§c wurde beendet!");
	        add(LanguageType.GERMANY, "FRIEND_DEL_IN", "§aIn §6{INPUT0}§a Sekunden wird die Freundschaft mit §e{INPUT1}§a aufgeloest!");
	        add(LanguageType.GERMANY, "FRIEND_GET", "§aDu hast von §e{INPUT0}§a eine Freundschaftsanfrage erhalten!");
	        add(LanguageType.GERMANY, "FRIEND_NOW", "§aDu bist nun mit §e{INPUT0}§a befreundet!");
	        add(LanguageType.GERMANY, "FRIEND_ASK_NOT", "§aDu hast momentan keine Freundschaftsanfragen.");
	        add(LanguageType.GERMANY, "FRIEND_IS_FRIEND", "§aDu bist bereits mit §e{INPUT0}§a befreundet!");
	        add(LanguageType.GERMANY, "FRIEND_YOURE_SELF", "§cFor ever alone?");
	        add(LanguageType.GERMANY, "NEULING_SCHUTZ", "§cDer Spieler §e{INPUT0}§c ist noch im Neulingsschutz!");
	        add(LanguageType.GERMANY, "NEULING_SCHUTZ_YOU", "§cDu bist momentan noch im Neulingsschutz benutzte §7/neuling§c um den Schutz zu deaktivieren.");
	        add(LanguageType.GERMANY, "NEULING_END", "§aDu besitzt nun kein Neulingsschutz mehr!");
	        add(LanguageType.GERMANY, "NEULING_CMD", "§cDu bist kein Neuling.");
	        add(LanguageType.GERMANY, "ANTI_LOGOUT_FIGHT", "§cDu bist nun im Kampf, logge dich nicht aus!");
	        add(LanguageType.GERMANY, "ANTI_LOGOUT_FIGHT_END", "§aDu bist nun nicht mehr im Kampf!");
	        add(LanguageType.GERMANY, "TELEPORT_VERZÖGERUNG", "§7Du wirst in §e{INPUT0}§7 teleportiert.");
	        add(LanguageType.GERMANY, "TELEPORT", "§aDu wurdest erfolgreich teleportiert!");
	        add(LanguageType.GERMANY, "TELEPORT_HERE", "§e{INPUT0}§a wurde erfolgreich teleportiert!");
	        add(LanguageType.GERMANY, "ACCEPT", "§aDu hast die Anfrage angenommen!");
	        add(LanguageType.GERMANY, "DENY", "§cDu hast die Anfrage abgelehnt!");
	        add(LanguageType.GERMANY, "ACCEPT_FROM", "§e{INPUT0}§a hat die Anfrage angenommen!");
	        add(LanguageType.GERMANY, "DENY_FROM", "§e{INPUT0}§e hat die Anfrage abgelehnt!");
	        add(LanguageType.GERMANY, "NO_ANFRAGE", "§cDu hast keine Anfrage erhalten.");
	        add(LanguageType.GERMANY, "ME", "mir");
	        add(LanguageType.GERMANY, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", "§aDer Spieler §e{INPUT0}§a fragt, ob du dich zu ihn telepotieren moechtest. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny");
	        add(LanguageType.GERMANY, "TELEPORT_ANFRAGE_EMPFÄNGER", "§aDer Spieler §e{INPUT0}§a fragt, ob er sich zu dir telepotieren darf. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny");
	        add(LanguageType.GERMANY, "TELEPORT_ANFRAGE_SENDER", "§aDu hast §e{INPUT0}§a eine Teleport Anfrage gesendet!");
	        add(LanguageType.GERMANY, "WARP_EXIST", "§cDer Warp existiert nicht.");
	        add(LanguageType.GERMANY, "WARP_SET", "§aDer Warp §e{INPUT0}§a wurde gespeichert!");
	        add(LanguageType.GERMANY, "KIT_USE", "§aDu hast das Kit §e{INPUT0}§a ausgewaehlt!");
	        add(LanguageType.GERMANY, "KIT_SET", "§aDas Kit §e{INPUT0}§a wurde gespeichert!");
	        add(LanguageType.GERMANY, "KIT_EXIST", "§cDieses Kit existiert nicht!");
	        add(LanguageType.GERMANY, "KIT_DEL", "§aDas Kit §e{INPUT0}§a wurde entfernt!");
	        add(LanguageType.GERMANY, "KIT_DELAY", "§cDu kannst das Kit erst in §e{INPUT0}§c wieder benutzen!");
	        add(LanguageType.GERMANY, "CLEARINVENTORY", "§7Dein Inventar wurde geleert!");
	        add(LanguageType.GERMANY, "CLEARINVENTORY_OTHER", "§7Dein Inventar wurde von §a{INPUT0}§7 geleert!");
	        add(LanguageType.GERMANY, "CLEARINVENTORY_FROM_OTHER", "§aDu hast das Inventar von §e{INPUT0}§a geleert");
	        add(LanguageType.GERMANY, "CLEARINVENTORY_ALL", "§aDu hast das Inventar von allen Spielern geleert!");
	        add(LanguageType.GERMANY, "NO_ITEM_IN_HAND", "§cDu hast kein Item in der Hand!");
	        add(LanguageType.GERMANY, "ITEM_RENAME", "§aDas Item wurde umbenannt!");
	        add(LanguageType.GERMANY, "NO_ANSWER_PARTNER", "§cDu hast niemanden zum antworten.");
	        add(LanguageType.GERMANY, "SOCIALSPY_ON", "§aDu hast Socialspy aktiviert!");
	        add(LanguageType.GERMANY, "SOCIALSPY_OFF", "§cDu hast Socialspy deaktiviert!");
	        add(LanguageType.GERMANY, "MONEY_NO_DOUBLE", "§cEntweder ist das keine Zahl oder 0!");
	        add(LanguageType.GERMANY, "MONEY_ENOUGH_MONEY", "§cDu hast nicht genug Epics!");
	        add(LanguageType.GERMANY, "MONEY_SEND_TO", "§aDu hast dem Spieler §e{INPUT0}§7 {INPUT1}§a Epics gesendet!");
	        add(LanguageType.GERMANY, "MONEY_RECEIVE_FROM", "§aDu hast von §e{INPUT0}§7 {INPUT1}§a Epics erhalten!");
	        add(LanguageType.GERMANY, "FALLDOWN_BREWITEM_BLOCKED", "§cDu kannst erst in §e{INPUT0}§c dein Item benutzten!");
	        add(LanguageType.GERMANY, "VANISH_AN", "§aVanish wurde aktiviert!");
	        add(LanguageType.GERMANY, "VANISH_AUS", "§cVanish wurde deaktiviert!");
	        add(LanguageType.GERMANY, "UNKNOWN_CMD", "§7Much command. Such no. Many confuse. Wow.");
	        add(LanguageType.GERMANY, "NO_PERMISSION", "§cMany permissions, Such denied, Much no, Wow.");
	        add(LanguageType.GERMANY, "WHEREIS_TEXT", "§7Du befindest dich momentan auf dem §e{INPUT0}§7 Server.");
	        add(LanguageType.GERMANY, "FALLDOWN_NICHT_GENUG_POWER", "§cDu hast nicht genug Kraft!");
	        add(LanguageType.GERMANY, "SKYBLOCK_HAVE_ISLAND", "§cDu hast bereits eine Insel!");
	        add(LanguageType.GERMANY, "SKYBLOCK_NO_ISLAND", "§cDu besitzt keine Insel!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_NO", "§cAktuell hast du keine Party am laufen!");
	        add(LanguageType.GERMANY, "SKYBLOCK_REMOVE_ISLAND", "§cDeine Insel wurde geloescht!");
	        add(LanguageType.GERMANY, "SKYBLOCK_CHANGE_BIOME", "§aDas Biom deiner Inesel wurde zu §e{INPUT0}§a geaendert!");
	        add(LanguageType.GERMANY, "SKYBLOCK_CREATE_ISLAND", "§aDeine Insel wurde erstellt!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PLAYER_KICK", "§aDu hast den Spieler §e{INPUT0}§a von deiner Insel gekickt!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PLAYER_KICKED", "§cDu wurdest von §e{INPUT0}s§c Insel gekickt!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PLAYER_NOT_ON_YOUR_ISLAND", "§aDer Spieler §e{INPUT0}§a ist nicht auf deiner Insel!");
	        add(LanguageType.GERMANY, "NO_BEFEHL", "§cDieser Befehl exestiert nicht!");
	        add(LanguageType.GERMANY, "WARPS_EMPTY","§cEs exestieren noch keine Warps!");
	        add(LanguageType.GERMANY,"NOT_ENOUGH_EXP","§cDu hast nicht genug Exp!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_EINLADEN_IS_IN", "§cDer Spieler ist bereits in einer Party.");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_IN", "§cDu bist bereits in einer Party. Tippe §7/party verlassen §cum sie wieder zu verlassen!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_EINLADEN_NO", "§cDu wurdest in keine Party eingeladen.");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_ENTER", "§aDu bist der Party beigetreten!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_ENTER_BY", "§aDer Spieler §e{INPUT0}§a ist der Party beigetreten!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_SIZE", "§cDu kannst maximal §e{INPUT0}§c Spieler in deine Party einladen!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_VOLL", "§cDie Party ist voll!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_EINLADEN_INVITE", "§aDu wurdest von §e{INPUT0}§a in seine Skyblock-Part eingeladen. Wenn du annehmen moechtest §7/party annehmen");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_EINLADEN_IS", "§cDer Spieler §e{INPUT0}§c ist bereits zur Party eingeladen.");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_ERSTELLT", "§aDeine Skyblock-Party wurde erfolgreich erstellt!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_NO_OWNER", "§cDu bist nicht der Owner dieser Party!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_EINLADEN", "§aDu hast §e{INPUT0}§a in die Party eingeladen!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_VERLASSEN", "§aDu hast die Party verlassen!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_KICKEN", "§aDu hast §e{INPUT0}§a aus der Party gekickt!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_SCHLIEßEN", "§cDie Party wurde geschlossen!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_PLAYER_NOT", "§cDieser Spieler ist nicht in der Party!");
	        add(LanguageType.GERMANY, "SKYBLOCK_TELEPORT_HOME", "§aDu wurdest zu deiner Insel teleportiert!");
	        add(LanguageType.GERMANY, "SKYBLOCK_PREFIX", "§[=========== §6§lSkyBlock §b===========]");
	        add(LanguageType.GERMANY, "SKYBLOCK_PARTY_PREFIX", "§b[=========== §6§lSkyBlock Party §b===========]");
	        add(LanguageType.GERMANY, "SKYBLOCK_REMOVE_ISLAND_ONE", "§cDu kannst nur einmal am Tag deine Insel loeschen oder als §bUltra §ceine Gilden Insel erstellen!");
	        add(LanguageType.GERMANY, "SIGN_SHOP_NO_ITEM_ON_INV", "§cDu hast dieses Item nicht im Inventar oder zu wenig davon!");
	        add(LanguageType.GERMANY, "SIGN_SHOP_VERKAUFT_", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}:{INPUT2}§7 Verkauft und hast §a{INPUT3}§7 Epics erhalten!");
	        add(LanguageType.GERMANY, "SIGN_SHOP_VERKAUFT", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}§7 verkauft und hast §a{INPUT2} Epics §7erhalten!");
	        add(LanguageType.GERMANY, "SIGN_SHOP_DELAY", "§cDu kannst nur alle §e{INPUT0}§c Sekunden was verkaufen!");
	        add(LanguageType.GERMANY, "SIGN_SHOP_GET", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}§7 bekommen dir wurden §a{INPUT2} Epics §7abgezogen!");
	        add(LanguageType.GERMANY, "SIGN_SHOP_GET_", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}:{INPUT2}§7 bekommen dir wurden §a{INPUT3} Epics §7abgezogen!");
	        add(LanguageType.GERMANY, "kFLY_NOT_ON", "§cDer Flugmodus ist nicht aktiviert!");
	        add(LanguageType.GERMANY, "NO_INTEGER", "§cDas ist keine Zahl!");
	        add(LanguageType.GERMANY, "kFLY_SPEED", "§aFlug Geschwindigkeit wurde auf §e{INPUT0}§a geaendert!");
	        add(LanguageType.GERMANY, "kFLY_ON", "§aDer Flugmodus wurde aktiviert!");
	        add(LanguageType.GERMANY, "kFLY_OFF", "§cDer Flugmodus wurde deaktiviert!");
	        add(LanguageType.GERMANY, "kFLY_PVP_FLAG", "§cDu darfst im PvP-Bereich nicht fliegen!");
	        add(LanguageType.GERMANY, "SPAWN_SET", "§aDer Spawn-Punkt wurde gespeichert!");
	        add(LanguageType.GERMANY, "SPAWN_TELEPORT", "§aDu wurdest zum Spawn teleportiert!");
	        add(LanguageType.GERMANY, "GIVEALL", "§a{INPUT0}§7 hat jedem Spieler §6{INPUT1}§7 mal §e{INPUT2}§7 gegeben!");
	        add(LanguageType.GERMANY, "REPAIR_HAND", "§aDas Item in deiner Hand wurde repariert!");
	        add(LanguageType.GERMANY, "REPAIR_ALL", "§aAlle Items in deinem Inventory wurden repariert!");
	        add(LanguageType.GERMANY, "FEED", "§aDu hast nun kein Hunger mehr!");
	        add(LanguageType.GERMANY, "FEED_ALL", "§7Der Spieler §a{INPUT0}§7 hat deinen Hunger gestillt!");
	        add(LanguageType.GERMANY, "FEED_OTHER", "§aDu hast den Hunger von §e{INPUT0}§a gestillt!");
	        add(LanguageType.GERMANY, "HEAL", "§aDu hast nun wieder volles Leben!");
	        add(LanguageType.GERMANY, "HEAL_ALL", "§aDer Spieler §e{INPUT0}§a hat nun wieder volles Leben!");
	        add(LanguageType.GERMANY, "HEAL_OTHER", "§aDu hast §e{INPUT0}s§a geheilt!");
	        add(LanguageType.GERMANY, "DAY", "§aEs ist nun 4:55 Uhr.");
	        add(LanguageType.GERMANY, "NIGHT", "§aEs ist nun 18:34 Uhr.");
	        add(LanguageType.GERMANY, "SUN", "§aJetzt scheint die Sonne.");
	        add(LanguageType.GERMANY, "USE_ENDERPEARL_TIME", "§cDu kannst die Enderperle erst in §e{INPUT0}§c wieder benutzten!");
	        add(LanguageType.GERMANY, "USE_BEFEHL_TIME", "§cDu kannst den Befehl erst in §e{INPUT0}§c wieder benutzen!");
	        add(LanguageType.GERMANY, "NO_RANG", "§cMany permissions, such denied, much no, Wow.");
	        add(LanguageType.GERMANY, "HOME_SET", "§7Das Home §e{INPUT0}§7 wurde gespeichert!");
	        add(LanguageType.GERMANY, "HOME_EXIST", "§cDieses Home exestiert nicht.");
	        add(LanguageType.GERMANY, "HOME_DEL", "§cDas Home §e{INPUT0}§c wurde geloescht");
	        add(LanguageType.GERMANY, "HOME_SKYBLOCK_DELETE", "§aDie Homes von §e{INPUT0}§a wurden entfernt!");
	        add(LanguageType.GERMANY, "HOME_MAX", "§cDu darfst nur §e{INPUT0}§c Homes setzten!");
	        add(LanguageType.GERMANY, "HOME_QUESTION", "§7Der Spieler §a{INPUT0}§7 fragt, ob er ein Home setzen darf. §e/homeaccept §7oder §e/homedeny");
	        add(LanguageType.GERMANY, "HOME_ISLAND", "§aEs wurde eine Anfrage zum setzen des Homes abgesetzt!");
	        add(LanguageType.GERMANY, "WARP_DEL", "§aDer Warp §e{INPUT0}§a wurde geloescht!");
	        add(LanguageType.GERMANY, "WARP_EXIST", "§cDieser Warp exestiert nicht!");
	        add(LanguageType.GERMANY, "GILDE_PLAYER_LEAVE", "§a{INPUT0}§7 hat den Server verlassen!");
	        add(LanguageType.GERMANY, "VOTE_THX", "§aDanke fürs §a§lVoten§a du hast deine Belohnung erhalten!");
			add(LanguageType.GERMANY, "ANIT_LOGOUT_FIGHT_CMD", "§cDu kannst den Befehl §e/gilden home§c nicht im Kampf ausführen!");
			add(LanguageType.GERMANY,"XMAS_DOOR","§aDu hast das Türchen geöffnet und §e{INPUT0}§a Coins erhalten!");
			add(LanguageType.GERMANY,"XMAS_DAY","§cIst heute der {INPUT0}te?");
			add(LanguageType.GERMANY,"LOGIN_FAIL","§cUngültiges Zeichen!");
			add(LanguageType.GERMANY,"LOAD_PLAYER_DATA","§7Deine Spieler Informationen werden geladen...");
			add(LanguageType.GERMANY,"PET_MUST_BUYED_IN_SHOP","§cDu musst das Pet im Online-Store kaufen! §6Shop.EpicPvP.de");
			add(LanguageType.GERMANY,"GAME_TIME_CHANGE","§aDie Zeit wurde zu {INPUT0} geändert!");
			add(LanguageType.GERMANY,"EXP_HIS_TO_ME","Du hast von §a{INPUT0} {INPUT1}§7 Exp erhalten.");
			add(LanguageType.GERMANY,"EXP_ME_TO_HIS","Du hast §a{INPUT0} {INPUT1}§7 Exp gesendet.");
			add(LanguageType.GERMANY,"NOT_ENOUGH_COINS","§cDu hast nicht genug Coins.");
			
			add(LanguageType.GERMANY,"GILDE_CMD1","§6/gilde erstellen §8|§7 Erstellt eine neue Gilde.");
			add(LanguageType.GERMANY,"GILDE_CMD2","§6/gilde einladen [Player] §8§8|§7 Lädt einen Spieler in die Gilde ein");
			add(LanguageType.GERMANY,"GILDE_CMD3","§6/gilde annehmen §8|§7 Nimmt Einladung an.");
			add(LanguageType.GERMANY,"GILDE_CMD4","§6/gilde ranking §8|§7 Zeigt die Top Ten an Gilden.");
			add(LanguageType.GERMANY,"GILDE_CMD5","§6/gilde verlassen §8|§7 Zum Verlassen/Schließen der Gilde.");
			add(LanguageType.GERMANY,"GILDE_CMD6","§6/gilde kicken [Player] §8|§7 Kickt einen Spieler aus der Gilde.");
			add(LanguageType.GERMANY,"GILDE_CMD7","§6/gilde info [Gilde] §8|§7 Zeigt Infos über eine Gilde.");
			add(LanguageType.GERMANY,"GILDE_CMD8","§6/gilde sethome §8|§7 setzt das Gilden-Home.");
			add(LanguageType.GERMANY,"GILDE_CMD9","§6/gilde home §8|§7 Teleportiert dich zum Gilden-Home.");
			add(LanguageType.GERMANY,"GILDE_CMD10","§6/gilde island §8|§7 Teleportiert dich zur Gilden-Insel.");
			add(LanguageType.GERMANY,"GILDE_CMD11","§6/gilde createisland §8|§7 Erstellt eine Gilden-Insel.");
			add(LanguageType.GERMANY,"GILDE_CMD12","§6/gilde money abheben [Money] §8|§7 Vom Gilden-Konto abheben.");
			add(LanguageType.GERMANY,"GILDE_CMD13","§6/gilde money einzahlen [Money] §8|§7 Auf dem Gilden-Konto einzahlen.");
			add(LanguageType.GERMANY,"HOMES_EMPTY","Du hast keine Homes");
			add(LanguageType.GERMANY,"KITS_EMPTY","Du hast keine Kits");
			add(LanguageType.GERMANY,"MONEY","Dein Kontostand beträgt:§3 ");
			add(LanguageType.GERMANY,"LOOK_ON_SPAWNER","Du musst einen MobSpawner ankucken.");
			add(LanguageType.GERMANY,"MOB_TYPE_NOT_FOUND","Mob Type nicht gefunden!");
			add(LanguageType.GERMANY,"EXP_MINUS","§cDu kannst keine Minus zahlen verschicken!");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD1","§6/skyblock erstellen §8|§7 Erstelle deine Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD2","§6/skyblock entfernen §8|§7 Lösche deine Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD3","§6/skyblock home §8|§7 Teleportiere dich zu deiner Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD4","§6/skyblock fixhome §8|§7 Teleportiere dich zu deiner Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD5","§6/skyblock kick [Player] §8|§7 Kicke Spieler von deiner Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD6","§6/skyblock newisland [Player] §8|§7 Erneurt die Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD7","§6/skyblock check §8|§7 Erneuert die alten Gilden u. Islands.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD8","§6/skyblock entities [Player] §8|§7 Die Entitys werden entfernt.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD9","§6/homedelete [Player] §8|§7 Löschen von Homes auf deiner Insel.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD10","§6/homeaccept §8|§7 Annehmen von Homes.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD11","§6/homedeny §8|§7 Ablehnen von Homes.");
			add(LanguageType.GERMANY,"SKYBLOCK_CMD12","§6/party §8|§7 Party Menue.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD1","§6/party erstellen §8|§7 Erstellt eine Party.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD2","§6/party home §8|§7 Teleportiere dich zur Party.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD3","§6/party verlassen §8|§7 Party verlassen.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD4","§6/party annehmen §8|§7 Annehmen von Einladungen.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD5","§6/party einladen [Player] §8|§7 Einladen zur Party.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD6","§6/party schließen §8|§7 Schließt die Party.");
			add(LanguageType.GERMANY,"SKYBLOCK_PARTY_CMD7","§6/party kicken [Player] §8|§7 Kickt aus der Party.");
		}
	}
	
	public static void add(LanguageType type,String name,String msg){
		if(!list.get(type).containsKey(name)){
			mysql.Update("INSERT INTO language (type,name,msg) VALUES ('"+type.getDef()+"','"+name+"','"+msg+"');");
			list.get(type).put(name, msg);
		}
	}
	
}

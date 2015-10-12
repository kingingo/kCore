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
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.PLAYER_LANGUAGE_CHANGE;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilTime;

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
	
	public static void updateLanguage(Player player,LanguageType type,PacketManager packetManager){
		languages.remove(UtilPlayer.getRealUUID(player));
		languages.put(UtilPlayer.getRealUUID(player), type);
		mysql.Update("UPDATE language_user SET language='"+type.getDef()+"' WHERE UUID='" + UtilPlayer.getRealUUID(player) + "'");
		packetManager.SendPacket("BG", new PLAYER_LANGUAGE_CHANGE(UtilPlayer.getRealUUID(player), type));
	}
	
	public static void load(MySQL mysql){
		setMysql(mysql);
		if(listener==null)listener=new LanguageListener(mysql.getInstance());
		list=new HashMap<>();
		languages=new HashMap<>();
		mysql.Update("CREATE TABLE IF NOT EXISTS language(type varchar(30),name varchar(30),msg varchar(30))");
		mysql.Update("CREATE TABLE IF NOT EXISTS language_user(uuid varchar(100),language varchar(100))");
		loadLanguage(LanguageType.ENGLISH);
	}

	public static LanguageType getLanguage(Player player){
		if(player==null){
			System.out.println("[Language] Spieler == null");
			return LanguageType.ENGLISH;
		}else if(!languages.containsKey(UtilPlayer.getRealUUID(player))){
			
			String def=Language.getMysql().getString("SELECT language FROM language_user WHERE uuid='"+UtilPlayer.getRealUUID(player)+"'");
			if(!def.equalsIgnoreCase("null")){
				if(!Language.getList().containsKey(LanguageType.get(def)))Language.loadLanguage(LanguageType.get(def));
				Language.getLanguages().put(UtilPlayer.getRealUUID(player), LanguageType.get(def));
			}else{
				Language.getMysql().Update("INSERT INTO language_user (uuid,language) VALUES ('"+UtilPlayer.getRealUUID(player)+"','"+LanguageType.ENGLISH.getDef()+"');");
				Language.getLanguages().put(UtilPlayer.getRealUUID(player), LanguageType.ENGLISH);
			}
			
			System.out.println("[Language] Spieler "+player.getName()+" nicht abgespeichert");
			return LanguageType.ENGLISH;
		}
		return languages.get(UtilPlayer.getRealUUID(player));
	}
	
	public static void sendText(Player player,String name,Object[] input){
		if(player==null){
			System.out.println("[Language] Message "+name);
			System.out.println("[Language] Spieler == null");
		}
		player.sendMessage( toText(list.get(getLanguage(player)).get(name), input) );
	}
	
	public static void sendText(Player player,String name,Object input){
		if(player==null){
			System.out.println("[Language] Message "+name);
			System.out.println("[Language] Spieler == null");
		}
		player.sendMessage( toText(list.get(getLanguage(player)).get(name), input) );
	}
	
	public static String getText(String name,Object input){
		if(!list.get(LanguageType.ENGLISH).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name);
			return Language.getText("MSG_NOT_FOUND", name);
		}
		return toText(list.get(LanguageType.ENGLISH).get(name), input);
	}
	
	public static String getText(String name,Object[] input){
		if(!list.get(LanguageType.ENGLISH).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name);
			return Language.getText("MSG_NOT_FOUND", name);
		}
		return toText(list.get(LanguageType.ENGLISH).get(name), input);
	}
	
	public static String getText(String name){
		if(!list.get(LanguageType.ENGLISH).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name);
			return Language.getText("MSG_NOT_FOUND", name);
		}
		return list.get(LanguageType.ENGLISH).get(name);
	}
	
	public static String getText(Player player,String name){
		if(player==null){
			System.out.println("[Language] Message "+name);
			System.out.println("[Language] Spieler == null");
		}
		if(!list.get(getLanguage(player)).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name+" "+getLanguage(player).getDef());
			return Language.getText(player,"MSG_NOT_FOUND", name);
		}
		return list.get(getLanguage(player)).get(name);
	}
	
	public static String getText(Player player,String name,Object[] input){
		if(player==null){
			System.out.println("[Language] Message "+name);
			System.out.println("[Language] Spieler == null");
		}
		if(!list.get(getLanguage(player)).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name+" "+getLanguage(player).getDef());
			return Language.getText(player,"MSG_NOT_FOUND", name);
		}
		return toText(list.get(getLanguage(player)).get(name), input);
	}
	
	public static String getText(Player player,String name,Object input){
		if(player==null){
			System.out.println("[Language] Message "+name);
			System.out.println("[Language] Spieler == null");
		}
		if(!list.get(getLanguage(player)).containsKey(name)){
			System.out.println("[Language] Message nicht gefunden "+name+" "+getLanguage(player).getDef());
			return Language.getText(player,"MSG_NOT_FOUND", name);
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
			add(type, "TREASURE_CHEST_TIME_AWAY", "§cThe time has expired!");
			add(type, "VERSUS_ADDED", "§aYou where added to the match making queue!");
			add(type, "VERSUS_REMOVE", "§aYou removed from the list!");
			add(type, "VERSUS_PLACE", "§7You are at position §e{INPUT0} in the queue.");
			add(type, "TRACKING_RANGE", "§7The player tracking range ist now §e{INPUT0}§7.");
			add(type, "NO_CHARAKTER", "§cThere are invalid characters in your name!");
			add(type, "STATS_PREFIX", "§b[===========§6 §lSTATS §b===========]");
			add(type, "STATS_PREFIXBY", "§b[=========== §6§l{INPUT0}s stats§b ===========]");
			add(type, "STATS_KILLS", "§6Kills : §7");
			add(type, "STATS_DEATHS", "§6Deaths : §7");
			add(type, "STATS_MONEY", "§6Money : §7");
			add(type, "STATS_KDR", "§6KDR : §7");
			add(type, "STATS_GILDE", "§6Gild : §7");
			add(type, "STATS_RANKING", "§6Rank : §7");
			add(type, "PREMIUM_PET", "§cBuy §6Premium§c to activate this feature!");
			add(type, "TREASURE_CHEST_TOO_NEAR", "§cYou are too close to another TreasureChest!");
			add(type, "CHAT_MESSAGE_BLOCK", "§cDo not use that kind of language!");
			add(type, "SPIELER_ENTFERNT_COMPASS", "§7Player §a{INPUT0}§7 is §e{INPUT1}§7 blocks away from you!");
			add(type, "AUßERHALB_DER_MAP", "§cYou are out of the map!");
			add(type, "COINS_DEL_PLAYER", "§7Removed §c{INPUT1}§7 coins from §a{INPUT0}§7!");
			add(type, "COINS_ADD_PLAYER", "§7Gave §a{INPUT1}§7 coins to §a{INPUT0}§7!");
			add(type, "GEMS_DEL_PLAYER", "§7Removed §c{INPUT1}§7 coins from §a{INPUT0}§7!");
			add(type, "GEMS_ADD_PLAYER", "§7Gave §a{INPUT1}§7 coins to §a{INPUT0}§7!");
			add(type, "CMD_MUTE", "§cCommands disabled!");
			add(type, "CMD_UNMUTE", "§aCommands enabled!");
			add(type, "PVP_MUTE", "§aPvP disabled!");
			add(type, "PVP_UNMUTE", "§cPvP enabled!");
			add(type, "CHAT_MUTE", "§cChat muted!");
			add(type, "CHAT_UNMUTE", "§aChat unmuted!");
			add(type, "MORE_HAND", "§aYou now have 64x.");
			add(type, "MORE_INV", "§aAll your items were rounded to 64x!");
			add(type, "ENDERGAMES_TELEPORT", "§7You switched positions with §a{INPUT0}§7!");
			add(type, "KIT_SHOP_ADD", "§aYou selected the kit§e{INPUT0}§a!");
			add(type, "KIT_SHOP_NO_MONEY", "§cYou do not have enough §7{INPUT0}§c!");
			add(type, "KIT_SHOP_BUYED_KIT", "§aYou successfully purchased §e{INPUT0}§a!");
			add(type, "PLAYER_IS_OFFLINE", "§e{INPUT0}§c is currently offline!");
			add(type, "FIGHT_START_IN", "§7You can start fighting in §e{INPUT0}§7 seconds!");
			add(type, "FIGHT_START", "§aYou can now fight!");
			add(type, "GAME_END_IN", "§cThe game ends in §e{INPUT0}§c ");
			add(type, "GAME_END", "§cThe game ended.");
			add(type,"GAME_START_MIN_PLAYER","§cNot enough players (min. {INPUT0}) online! Restart counter!");
			add(type, "RESTART_IN", "§4§lServer restart in  §e§l{INPUT0}§4§l seconds!");
			add(type, "RESTART", "§4§lThe server restarts now!");
			add(type, "SERVER_FULL", "§cServer is full!");
			add(type, "SERVER_NOT_LOBBYPHASE", "§cThis server is currently not in lobby mode!");
			add(type, "SERVER_FULL_WITH_PREMIUM", "§cThis server is full! §cBuy §6Premium§c to join full servers! &7[&6shop.EpicPvP.de&7]");
			add(type, "KILL_BY", "§a{INPUT0}§7 was killed by §e{INPUT1}§7!");
			add(type, "DEATH", "§a{INPUT0}§7 died!");
			add(type, "GAME_EXCLUSION", "§c{INPUT0} was excluded!");
			add(type, "PERK_NOT_BOUGHT", "§cYou did not purchase any perks yet! §6Shop.EpicPvP.de");
			add(type, "VOTE_TEAM_ADD", "§aYou joined team §7{INPUT0}§a");
			add(type, "VOTE_TEAM_MIN_PLAYER", "§cThere have to be atleast {INPUT0} players online!");
			add(type, "VOTE_TEAM_REMOVE", "§aYou left team §7{INPUT0}§a!");
			add(type, "VOTE_TEAM_FULL", "§7{INPUT0}§c is full!");
			add(type, "VOTE_MIN", "§cThere have to be atleast §e{INPUT0}§c players online for this game to start!");
			add(type, "KICKED_BY_PREMIUM", "§cYou were kicked so a premium player can join! Want that too? §6shop.EpicPvP.de");
			add(type, "PREFIX_GAME", "§6{INPUT0} §8» §7");
			add(type, "PREFIX", "§6EpicPvP §8» §7");
			add(type, "TEAM_WIN", "§aTeam {INPUT0}§a won the game!");
			add(type, "TEAM_OUT", "§7{INPUT0}§c has fallen!");
			add(type, "RESTART_FROM_ADMIN", "§cThe server was restarted by an administrator!");
			add(type, "SCHUTZZEIT_END_IN", "§7PvP enables in §e{INPUT0}§7 seconds!");
			add(type, "SCHUTZZEIT_END", "§aPvP is now enabled, you can now fight!");
			add(type, "GAME_START", "§aGame started!");
			add(type, "GAME_START_IN", "§7The game starts in §e{INPUT0}§7 seconds!");
			add(type, "DEATHMATCH_START", "§aThe deathmatch started!");
			add(type, "DEATHMATCH_START_IN", "§7The deathmatch starts in §e{INPUT0}§7 seconds!");
			add(type, "DEATHMATCH_END", "§cThe deathmatch ended!");
			add(type, "GAME_WIN", "§7{INPUT0}§a won the game!");
			add(type, "SURVIVAL_GAMES_DISTRICT_WIN", "§aDistrict §e{INPUT0}§a with players §7{INPUT1}§a and §7{INPUT2}§a won!");
			add(type, "DEATHMATCH_END_IN", "§cThe deathmatch ends in §e{INPUT0}§c seconds!");
			add(type, "LOGIN_MESSAGE", "§cPlease log in with: §e/Login <Password>");
			add(type, "KICK_BY_FALSE_PASSWORD", "§cYou entered a wrong password §e{INPUT0}§c times");
			add(type, "LOGIN_ACCEPT", "§aYou successfully logged in!");
			add(type, "LOGIN_DENY", "§cWrong password!");
			add(type, "REGISTER_ACCEPT", "§aRegisterd successfully!");
			add(type, "REGISTER_MESSAGE", "§cPlease register using: §e/Register <Password>");
			add(type, "SPECTATOR_CHAT_CANCEL", "§cSpectators ca not chat!");
			add(type, "TTT_WIN", "§aThe §7{INPUT0}§a won the game!");
			add(type, "TTT_DEATH", "§a{INPUT0}§7 died an was §e{INPUT1}§7!");
			add(type, "TTT_TESTER_WAS_USED", "§cThe tester is currently unavailable!");
			add(type, "TTT_TRAITOR_CHAT", "§7To use the §cTraitor-Chat §ctype: §c/tc [Message]§7!");
			add(type, "TTT_TESTER_USED", "§cThe tester is currently in use!");
			add(type, "TTT_TESTER_TIME", "§cYou allready used the tester, please come back later!");
			add(type, "TTT_TRAITOR_SHOP", "§cYou do not have enough traitor points!");
			add(type, "TTT_PASSE_USE", "§aYou used a §6{INPUT0}§a pass! You have §e{INPUT1}§a left!");
			add(type, "TTT_PASSE_KEINE", "§cYou do not have any {INPUT0}§c passes!");
			add(type, "TTT_PASSE_LOBBYPHASE", "§cThe lobby phase is over...");
			add(type, "TTT_PASSE_MAX_USED", "§cThere were allready to many §e{INPUT0}§c passes used!");
			add(type, "TTT_SHOP", "§cYou do not have enough points!");
			add(type, "TTT_SHOP_BUYED", "§aYou successfully purchased an item!");
			add(type, "TTT_NPC_CLICKED", "§7This is §a{INPUT0}§7 he was §e{INPUT1}§7.");
			add(type, "TTT_DNA_TEST", "§7The DNA test shows that §a{INPUT0}§7 was killed by §e{INPUT1}§7.");
			add(type, "TTT_TRAITOR_SHOP_RADAR_CHANGE", "§aYou selected §a{INPUT0}§7!");
			add(type, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH", "§cIt is to late for this player!");
			add(type, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT", "§aYou successfully revived §7{INPUT0}§a!");
			add(type, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER", "§aYou were revived by §7{INPUT0}§a!");
			add(type, "TTT_TESTER_JOIN", "§a{INPUT0}§7 entered the tester!");
			add(type, "TTT_LEICHE_IDENTIFIZIERT", "§a{INPUT0}s§7corps was found, he was §e{INPUT1}§7!");
			add(type, "TTT_IS_NOW", "§7You are §c{INPUT0}§7.");
			add(type, "GEMS_ADD", "§aYou recieved §e{INPUT0}§a gems!");
			add(type, "GEMS_DEL", "§e{INPUT0}§c gems have been deducted from you!");
			add(type, "COINS_ADD", "§aYou recieved §e{INPUT0}§a coins!");
			add(type, "COINS_DEL", "§e{INPUT0}§c coins have been deducted from you!");
			add(type, "GAME_AUSGESCHIEDEN", "§a{INPUT0}§c was removed from the game!");
			add(type, "CAVEWARS_SPIDER_DEATH", "§7Team {INPUT0}s§7 sheep was slain by §a{INPUT1}§7!");
			add(type, "SHEEPWARS_SHEEP_DEATH", "§7Team {INPUT0}§7 lost their sheep!");
			add(type, "BEDWARS_BED_BROKE", "§7Team {INPUT0}§7 lost their bed!");
			add(type, "GILDE_MONEY_DEPOSIT", "§a{INPUT0}§7 deposited §a{INPUT1} Epics§7!");
			add(type, "GILDE_MONEY_LIFTED", "§a{INPUT0}§7 has withdrew§c {INPUT1} Epics§7!");
			add(type, "GILDE_NOT_ENOUGH_MONEY", "§cThere is not enough money on the guild account.");
			add(type, "GILDE_CREATE", "§aYou successfully founded §6{INPUT0}§a! The guild-home was set at your current position.");
			add(type, "GILDE_PLAYER_OFFLINE", "§e{INPUT0}§c is currently offline!");
			add(type, "GILDE_PREFIX", "§6Guild §8» §7");
			add(type, "GILDE_PLAYER_ENTRE", "§a{INPUT0}§7 joined the guild!");
			add(type, "GILDE_NAME_LENGTH_MIN", "§cThe guild name has to be longer than §e{INPUT0}§c characters!");
			add(type, "GILDE_NAME_LENGTH_MAX", "§cThe guild name has to be shorter than §e{INPUT0}§c characters!");
			add(type, "GILDE_EXIST", "§cThis guild allready exists!");
			add(type, "GILDE_EXIST_NOT", "§cThis guild does not exist!");
			add(type, "GILDE_DELETE", "§cThe guild was deleted!");
			add(type, "GILDE_EINLADEN", "§aYou invited §e{INPUT0}§a to the guild!");
			add(type, "GILDE_STATS_PREFIX", "§b[=========== §6§lGuilds §b===========]");
			add(type, "GILDE_STATS_PREFIXBY", "§b[=========== §6§l{INPUT0}s informations§b ===========]");
			add(type, "GILDE_EILADUNG", "§aYou were invited to join the guild §e{INPUT0}§a!");
			add(type, "GILDE_CLOSED", "§cThis guild was closed!");
			add(type, "GILDE_KICK_PLAYER", "§e{INPUT0}§c was kicked from guild!");
			add(type, "GILDE_SETHOME", "§aGuilde home set!");
			add(type, "GILDE_SETISLAND", "§aGuild island created!");
			add(type, "GILDEN_NAME", "§cThere is an invalid character in your guild name!");
			add(type, "GILDE_TELEPORT_CANCELLED", "§cTeleportation cancled!");
			add(type, "GILDE_TELEPORTET", "§aTeleportation successfull!");
			add(type, "GILDE_HOME", "§7Teleporting in §e{INPUT0}§7 ...");
			add(type, "GILDE_PLAYER_NICHT_EINGELADEN", "§cYou have not been invited into a guild!");
			add(type, "GILDE_PLAYER_JOIN", "§a{INPUT0}§7 joined the server!");
			add(type, "FRIEND_HIT", "§cYou really want to kill your friend?");
			add(type, "FRIEND_PREFIX", "§6Friend §8» §7");
			add(type, "FRIEND_EXIST", "§cYou are allready friends with §a{INPUT0}§c!");
			add(type, "FRIEND_SEND", "§aYou send §e{INPUT0}§a a friend request!");
			add(type, "FRIEND_NOT", "§cYou are not friends with §e{INPUT0}§c!");
			add(type, "FRIEND_DEL", "§cYour friendship with §e{INPUT0}§c ended!");
			add(type, "FRIEND_DEL_IN", "§aIn §6{INPUT0}§a seconds your friendship with §e{INPUT1}§a ends!");
			add(type, "FRIEND_GET", "§aYou recieved a friend request from §e{INPUT0}§a!");
			add(type, "FRIEND_NOW", "§aYou are now friends with §e{INPUT0}§a!");
			add(type, "FRIEND_ASK_NOT", "§aYou do not have any friend request right now.");
			add(type, "FRIEND_IS_FRIEND", "§aYou are allready friends with §e{INPUT0}§a!");
			add(type, "FRIEND_YOURE_SELF", "§cFor ever alone?");
			add(type, "NEULING_SCHUTZ", "§e{INPUT0}§c is a freshman! You ca not attak him!");
			add(type, "NEULING_SCHUTZ_YOU", "§cYou are still a freshman so you cannot other players! Type §7/neuling§c to deactivate this protection.");
			add(type, "NEULING_END", "§aYou are no longer a Freshman!");
			add(type, "NEULING_CMD", "§cYou are not a Freshman.");
			add(type, "ANTI_LOGOUT_FIGHT", "§cYou are now fighting, DO NOT log out!");
			add(type, "ANTI_LOGOUT_FIGHT_END", "§aYou are no longer fighting!");
			add(type, "TELEPORT_VERZÖGERUNG", "§7Teleporting in §e{INPUT0}");
			add(type, "TELEPORT", "§aSuccessfully teleported!");
			add(type, "TELEPORT_HERE", "§e{INPUT0}§a was teleported successfully!");
			add(type, "ACCEPT", "§aYou accepted!");
			add(type, "DENY", "§cYou denied!");
			add(type, "ACCEPT_FROM", "§e{INPUT0}§a accepeted the teleport request!");
			add(type, "DENY_FROM", "§e{INPUT0}§e denied the teleport request!");
			add(type, "NO_ANFRAGE", "§cYou do not have a pending request.");
			add(type, "ME", "me");
			add(type, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", "§e{INPUT0}§a is asking you to teleport to them. Use §7/tpaccept§a or §7/tpdeny");
			add(type, "TELEPORT_ANFRAGE_EMPFÄNGER", "§e{INPUT0}§a is asking to teleport to you. Use §7/tpaccept§a or §7/tpdeny");
			add(type, "TELEPORT_ANFRAGE_SENDER", "§aSend teleport request to §e{INPUT0}§a!");
			add(type, "WARP_EXIST", "§cThis warp does not exist.");
			add(type, "WARP_SET", "§aWarp §e{INPUT0}§a set!");
			add(type, "KIT_USE", "§aYou selected kit §e{INPUT0}§a!");
			add(type, "KIT_SET", "§aKit §e{INPUT0}§a saved!");
			add(type, "KIT_EXIST", "§cThis kit does not exist!");
			add(type, "KIT_DEL", "§aKit §e{INPUT0}§a removed!");
			add(type, "KIT_DELAY", "§cYou can use the kit again in §e{INPUT0}§c!");
			add(type, "CLEARINVENTORY", "§7Your inventory was cleared!");
			add(type, "CLEARINVENTORY_OTHER", "§7Your inventory was cleared by §a{INPUT0}§7!");
			add(type, "CLEARINVENTORY_FROM_OTHER", "§aYou cleared §e{INPUT0}s§a inventory!");
			add(type, "CLEARINVENTORY_ALL", "§aYou cleared the inventory of all players!");
			add(type, "NO_ITEM_IN_HAND", "§cYou do not have an item in your hand!");
			add(type, "ITEM_RENAME", "§aItem renamed!");
			add(type, "NO_ANSWER_PARTNER", "§cYou have noone to respond to.");
			add(type, "SOCIALSPY_ON", "§aSocialspy activated!");
			add(type, "SOCIALSPY_OFF", "§cSocialspy deactivated!");
			add(type, "MONEY_NO_DOUBLE", "§cNot a valid number or 0!");
			add(type, "MONEY_ENOUGH_MONEY", "§cYou do not have enough Epics!");
			add(type, "MONEY_SEND_TO", "§aYou send §e{INPUT0}§7 {INPUT1}§a Epics!");
			add(type, "MONEY_RECEIVE_FROM", "§aYou recieved §7{INPUT1} Epics§a from §e{INPUT0}§a!");
			add(type, "FALLDOWN_BREWITEM_BLOCKED", "§cYou can use this item in §e{INPUT0}§c!");
			add(type, "VANISH_AN", "§aVanish activated!");
			add(type, "VANISH_AUS", "§cVanish deactivated!");
			add(type, "UNKNOWN_CMD", "§7Much command. Such no. Many confuse. Wow.");
			add(type, "NO_PERMISSION", "§cMany permissions, Such denied, Much no, Wow.");
			add(type, "WHEREIS_TEXT", "§7You are currently on the §e{INPUT0}§7 server.");
			add(type, "FALLDOWN_NICHT_GENUG_POWER", "§cYou do not have enough power!");
			add(type, "SKYBLOCK_HAVE_ISLAND", "§cYou allready own an island!");
			add(type, "SKYBLOCK_NO_ISLAND", "§cYou do not own an island!");
			add(type, "SKYBLOCK_PARTY_NO", "§cYou currently do not have a party going!");
			add(type, "SKYBLOCK_REMOVE_ISLAND", "§cYour island was deleted!");
			add(type, "SKYBLOCK_CHANGE_BIOME", "§aYou islands biome was changed to §e{INPUT0}§a!");
			add(type, "SKYBLOCK_CREATE_ISLAND", "§aIsland created!");
			add(type, "SKYBLOCK_PLAYER_KICK", "§aYou kicked §e{INPUT0}§a from your island!");
			add(type, "SKYBLOCK_PLAYER_KICKED", "§cYou were kicked from the island by §e{INPUT0}s§c!");
			add(type, "SKYBLOCK_PLAYER_NOT_ON_YOUR_ISLAND", "§e{INPUT0}§a is not on your island!");
			add(type, "NO_BEFEHL", "§cThis command does not exist!");
			add(type, "WARPS_EMPTY","§cThere are no warps yet!");
			add(type,"NOT_ENOUGH_EXP","§cYou do not have enough exp!");
			add(type, "SKYBLOCK_PARTY_EINLADEN_IS_IN", "§cThis player is allready in a party.");
			add(type, "SKYBLOCK_PARTY_IN", "§cYou are allready in a party. Use §7/party verlassen §cto leave!");
			add(type, "SKYBLOCK_PARTY_EINLADEN_NO", "§cYou were not invited to a party.");
			add(type, "SKYBLOCK_PARTY_ENTER", "§aYou joined the party!");
			add(type, "SKYBLOCK_PARTY_ENTER_BY", "§e{INPUT0}§a joined the party!");
			add(type, "SKYBLOCK_PARTY_SIZE", "§cYou can only invite §e{INPUT0}§c players to your party!");
			add(type, "SKYBLOCK_PARTY_VOLL", "§cThe party is full!");
			add(type, "SKYBLOCK_PARTY_EINLADEN_INVITE", "§aYou were invited to a skyblock party by §e{INPUT0}§a. To join use §7/party annehmen");
			add(type, "SKYBLOCK_PARTY_EINLADEN_IS", "§e{INPUT0}§c is allready invited to this party.");
			add(type, "SKYBLOCK_PARTY_ERSTELLT", "§aSkyblock-party successfully created!");
			add(type, "SKYBLOCK_PARTY_NO_OWNER", "§cYou are not the host of this party!");
			add(type, "SKYBLOCK_PARTY_EINLADEN", "§aYou invited §e{INPUT0}§a to the party!");
			add(type, "SKYBLOCK_PARTY_VERLASSEN", "§aYou left the party!");
			add(type, "SKYBLOCK_PARTY_KICKEN", "§aYou kicked §e{INPUT0}§a from the party!");
			add(type, "SKYBLOCK_PARTY_SCHLIEßEN", "§cParty was closed!");
			add(type, "SKYBLOCK_PARTY_PLAYER_NOT", "§cThis player is not in your party!");
			add(type, "SKYBLOCK_TELEPORT_HOME", "§aYou were teleported t you island!"); 
			add(type, "SKYBLOCK_PREFIX", "§[=========== §6§lSkyBlock §b===========]");
			add(type, "SKYBLOCK_PARTY_PREFIX", "§b[=========== §6§lSkyBlock-Party §b===========]");
			add(type, "SKYBLOCK_REMOVE_ISLAND_ONE", "§cYou can only delete your island once a day or create a guild island as an §bUltra §c ranked member!");
			add(type, "SIGN_SHOP_NO_ITEM_ON_INV", "§cYou do not have this in your inventory or not enough of it!");
			add(type, "SIGN_SHOP_VERKAUFT_", "§7You sold §6{INPUT0}§7 times §e{INPUT1}:{INPUT2}§7 and recieved §a{INPUT3}§7 Epics!");
			add(type, "SIGN_SHOP_VERKAUFT", "§7You sold §6{INPUT0}§7 times §e{INPUT1}§7 and recieved §a{INPUT2}§7 Epics!");
			add(type, "SIGN_SHOP_DELAY", "§cYou can only sell something every §e{INPUT0}§c seconds!");
			add(type, "SIGN_SHOP_GET", "§7You bought §6{INPUT0}§7 times §e{INPUT1}§7 and paid §a{INPUT2} Epics §7!");
			add(type, "SIGN_SHOP_GET_", "§7You bought §6{INPUT0}§7 times §e{INPUT1}:{INPUT2}§7 and paid §a{INPUT3} Epics §7!");
			add(type, "kFLY_NOT_ON", "§cFlymode is not activated");
			add(type, "NO_INTEGER", "§cThis is not a number!");
			add(type, "kFLY_SPEED", "§aFly speed was changed to §e{INPUT0}§a!");
			add(type, "kFLY_ON", "§aFlymode activated!");
			add(type, "kFLY_OFF", "§cFlymode deactivated!");
			add(type, "kFLY_PVP_FLAG", "§cYou are not allowed to fly in the PvP-area!");
			add(type, "SPAWN_SET", "§aSpawnpoint set!");
			add(type, "SPAWN_TELEPORT", "§aYou teleported to spawn!");
			add(type, "GIVEALL", "§a{INPUT0}§7 gave all players §6{INPUT1}§7 times §e{INPUT2}§7!");
			add(type, "REPAIR_HAND", "§aThe selected item was repaired!");
			add(type, "REPAIR_ALL", "§aRepaired all items!");
			add(type, "FEED", "§aYou are saturated!");
			add(type, "FEED_ALL", "§7Your hunger was saturated by §a{INPUT0}§7!");
			add(type, "FEED_OTHER", "§aYou saturated §e{INPUT0}s§a hunger!");
			add(type, "HEAL", "§aYou are back to full health!");
			add(type, "HEAL_ALL", "§e{INPUT0}§a is back to full health!");
			add(type, "HEAL_OTHER", "§aYou healed §e{INPUT0}§a!");
			add(type, "DAY", "§aIt is now 4:55 am.");
			add(type, "NIGHT", "§aIt is now 6:34 pm.");
			add(type, "SUN", "§aThe sun is now shineing.");
			add(type, "USE_ENDERPEARL_TIME", "§cYou can use enderpearls again in §e{INPUT0}§c!");
			add(type, "USE_BEFEHL_TIME", "§cYou can use this command again in §e{INPUT0}§c!");
			add(type, "NO_RANG", "§cMany permissions, such denied, much no, Wow.");
			add(type, "HOME_SET", "§7Home §e{INPUT0}§7 saved!");
			add(type, "HOME_EXIST", "§cThis home does not exist.");
			add(type, "HOME_DEL", "§cHome §e{INPUT0}§c removed!");
			add(type, "HOME_SKYBLOCK_DELETE", "§e{INPUT0}s§a homes removed!");
			add(type, "HOME_MAX", "§cYou can only set §e{INPUT0}§c homes!");
			add(type, "HOME_QUESTION", "§a{INPUT0}§7 is asking to set a home. §e/homeaccept §7or §e/homedeny");
			add(type, "HOME_ISLAND", "§aA request to set a home was send!");
			add(type, "WARP_DEL", "§aWarp §e{INPUT0}§a removed!");
			add(type, "WARP_EXIST", "§cThis warp does not exist!");
			add(type, "GILDE_PLAYER_LEAVE", "§a{INPUT0}§7 left the server!");
			add(type, "VOTE_THX", "§aThank you for §a§lVoting§a! You recieved your reward!");
			add(type, "ANIT_LOGOUT_FIGHT_CMD", "§cYou cannot use §e/gilden home§c in combat!");
			add(type,"XMAS_DOOR","§aYou opened the door and recieved §e{INPUT0}§a coins!");
			add(type,"XMAS_DAY","§cIst heute der {INPUT0}te?");
			add(type,"LOGIN_FAIL","§cInvaild character!");
			add(type,"LOAD_PLAYER_DATA","§7Loading your player information...");
			add(type,"PET_MUST_BUYED_IN_SHOP","§cYou have to purchase this pet! §6Shop.EpicPvP.de");
			add(type,"GAME_TIME_CHANGE","§aTime changed to {INPUT0}!");
	        add(type, "GILDE_OWNER_NOT", "§cYou are not the owner of this guild.");
	        add(type,"GILDE_PLAYER_IS_IN_GILDE","§cYou are allready in a guild.");
	        add(type, "NOT_ENOUGH_MONEY", "§cYou do not have enough money!");
	        add(type,"GILDE_PLAYER_IS_IN_GILDE1","§c{INPUT0} is in a guild.");
	        add(type,"GILDE_CMD1","§6/guild create [Guild] §8|§7 Creates a new guild.");
			add(type,"GILDE_CMD2","§6/guild invite [Player] §8§8|§7 Invites a player to the guild.");
			add(type,"GILDE_CMD3","§6/guild accept §8|§7 Accepts a guild invite.");
			add(type,"GILDE_CMD4","§6/guild ranking §8|§7 Shows the top guilds.");
			add(type,"GILDE_CMD5","§6/guild leave §8|§7 To leave or disband a guild.");
			add(type,"GILDE_CMD6","§6/guild kick [Player] §8|§7 Kicks a player from the guild.");
			add(type,"GILDE_CMD7","§6/guild info [Guild] §8|§7 Shows informations about a guild.");
			add(type,"GILDE_CMD8","§6/guild sethome §8|§7 Sets the guild home.");
			add(type,"GILDE_CMD9","§6/guild home §8|§7 Teleports you to the guild home.");
			add(type,"GILDE_CMD10","§6/guild island §8|§7 Teleports you to the guild island.");
			add(type,"GILDE_CMD11","§6/guild createisland §8|§7 Creats a guild island.");
			add(type,"GILDE_CMD12","§6/guild money withdraw [Money] §8|§7 Withdraws money from the guild account.");
			add(type,"GILDE_CMD13","§6/gilde money deposite [Money] §8|§7 Deposits money to the guild account.");
	        add(type,"GILDE_PLAYER_IS_NOT_IN_GILDE","§cYou are not in a guild.");
			add(type,"HOMES_EMPTY","You do not have a home.");
			add(type,"KITS_EMPTY","You do not have any kits.");
			add(type,"MONEY","Account balance:§3 ");
			add(type,"LOOK_ON_SPAWNER","You have to be looking at a spawner.");
			add(type,"MOB_TYPE_NOT_FOUND","Mob type not found!");
			add(type,"EXP_MINUS","§cYou cannot send negative numbers!");
			add(type,"SKYBLOCK_CMD1","§6/skyblock create §8|§7 Creates a new island.");
			add(type,"SKYBLOCK_CMD2","§6/skyblock delete §8|§7 Deletes a island.");
			add(type,"SKYBLOCK_CMD3","§6/skyblock home §8|§7 Teleports you to your island.");
			add(type,"SKYBLOCK_CMD4","§6/skyblock fixhome §8|§7 Fixes your home.");
			add(type,"SKYBLOCK_CMD5","§6/skyblock kick [Player] §8|§7 Kicks a player from your island.");
			add(type,"SKYBLOCK_CMD6","§6/skyblock newisland [Player] §8|§7 Restarts your skyblock.");
			add(type,"SKYBLOCK_CMD7","§6/skyblock check §8|§7 Renews old guilds and islands.");
			add(type,"SKYBLOCK_CMD8","§6/skyblock entities [Player] §8|§7 Removes all entities.");
			add(type,"SKYBLOCK_CMD9","§6/homedelete [Player] §8|§7 Removes the players home.");
			add(type,"SKYBLOCK_CMD10","§6/homeaccept §8|§7 Accepts a players home on your island.");
			add(type,"SKYBLOCK_CMD11","§6/homedeny §8|§7 Denies a players home on your island.");
			add(type,"SKYBLOCK_CMD12","§6/party §8|§7 Party menu.");
			add(type,"SKYBLOCK_PARTY_CMD1","§6/party create §8|§7 Creates a party.");
			add(type,"SKYBLOCK_PARTY_CMD2","§6/party home §8|§7 Teleports you to the party.");
			add(type,"SKYBLOCK_PARTY_CMD3","§6/party leave §8|§7 To leave a party.");
			add(type,"SKYBLOCK_PARTY_CMD4","§6/party accept §8|§7 Accepts a party invite.");
			add(type,"SKYBLOCK_PARTY_CMD5","§6/party invite [Player] §8|§7 Invites a player to your party.");
			add(type,"SKYBLOCK_PARTY_CMD6","§6/party disband §8|§7 Disbands the party.");
			add(type,"SKYBLOCK_PARTY_CMD7","§6/party kick [Player] §8|§7 Kicks a player from your party.");
	        add(type, "MSG_NOT_FOUND", "§cMessage §e{INPUT0} §cnot found! Please contact an Administrator.");
	        add(type, "GILDE_PLAYER_GO_OUT", "§b{INPUT0} §cleft the guild!");
			add(type,"GAME_ENTER","§a{INPUT0} §7joined the game! §e({INPUT1}/{INPUT2})");
			add(type,"GAME_LEAVE","&a{INPUT0} §7left the game!");
			add(type, "HUB_ITEM_COMPASS", "§aGames");
			add(type, "HUB_ITEM_CHEST", "§6Shop");
			add(type, "HUB_ITEM_BUCH", "§bLanguage");
			add(type, "HUB_ITEM_NAMETAG_NICK_ON", "§5Nick §8» §aOn");
			add(type, "HUB_ITEM_NAMETAG_NICK_OFF", "§5Nick §8» §cOff");
			add(type, "HUB_ITEM_GREEN.DYE_PLAYERS_ON", "§aPlayer §8» §aOn");
			add(type, "HUB_ITEM_GRAY.DYE_PLAYERS_OFF", "§cPlayer §8» §cOff");
			add(type, "HUB_ITEM_NETHERSTAR", "§eLobby-Teleporter");
			add(type,"GAME_START_MIN_PLAYER","§cWaitmode are restartet!");
			add(type,"GAME_HOLOGRAM_SERVER","Server: {INPUT0}");
			add(type,"GAME_HOLOGRAM_MAP","Map: {INPUT0}");
			add(type,"GAME_HOLOGRAM_KILLS","Kills: {INPUT0}");
			add(type,"GAME_HOLOGRAM_DEATHS","Deaths: {INPUT0}");
			add(type,"GAME_HOLOGRAM_BEDWARS","Beds destroyed: {INPUT0}");
			add(type,"GAME_HOLOGRAM_CAVEWARS","Spider-Kills: {INPUT0}");
			add(type,"GAME_HOLOGRAM_WINS","Wins: {INPUT0}");
			add(type,"GAME_HOLOGRAM_LOSE","Lose: {INPUT0}");
			add(type,"GAME_HOLOGRAM_GAMES","Games: {INPUT0}");
			add(type,"GAME_HOLOGRAM_STATS","§a{INPUT0} §6§l Stats");
			add(type,"GAME_HOLOGRAM_POWER","Power: {INPUT0}");
			add(type,"GAME_START_MIN_PLAYER2","§cWaitmode are restartet!");
			add(type,"GAME_HOLOGRAM_KARMA","Karma: {INPUT0}");
			add(type,"GAME_HOLOGRAM_TESTS","Tests: {INPUT0}");
			add(type,"GAME_HOLOGRAM_TRAITOR_POINTS","Traitor-Points: {INPUT0}");
			add(type,"GAME_HOLOGRAM_DETECTIVE_POINTS","Detective-Points: {INPUT0}");
			add(type,"SCOREBOARD_GEMS","§a§lGems:");
			add(type,"SCOREBOARD_COINS","§e§lCoins:");
			add(type,"SCOREBOARD_RANK","§6§lRank:");
			add(type,"SCOREBOARD_FORUM","§c§lForum:");
			add(type,"SCOREBOARD_ONLINE_STORE","§a§lOnline-Store:");
			add(type,"SCOREBOARD_TS","§d§lTeamspeak:");
			add(type,"SCOREBOARD_NO_RANK","no rank");
	        add(type, "HOW_MANY_PLAYER_LEFT", "§e{INPUT0} §7players remaining!");
			add(type,"PLAYER_SET_NICK","Your new name is now: {INPUT0}");
			add(type,"PLAYER_DISALLOW_TEMPORÄR","Your nickname was temporary deactivated! You can now chat with your real name!");
			add(type,"NO_KIT","no kit");
			add(type,"NO_TEAMS_ALLOWED","§c§lTeams are not allow");
			add(type,"GAME_HOLOGRAM_SHEEP","killed Sheeps: {INPUT0}");
			add(type,"GAME_TEAM_ITEM","§bChoose a team");
			add(type,"KIT_SHOP_CHOOSE","§aChoose");
			add(type,"KIT_SHOP_BUY","§6Buy");
			add(type,"KIT_SHOP_ADMIN","§7This kit is a admin-kit");
			add(type,"KIT_SHOP_SPEZIAL1","§7This kit is a spezial-kit");
			add(type,"KIT_SHOP_SPEZIAL2","§7You can get it by a event!");
			add(type,"KIT_SHOP_PREMIUM","§7This kit is a premium-kit");
			add(type,"KIT_BACK","§cback");
	        add(type,"STATS_FAME","§6Fame: §7");
			add(type,"NOT_ENOUGH_COINS","§cYou have not enough Coins.");
			add(type,"NOT_ENOUGH_GEMS","§cYou have not enough Gems.");
			add(type,"LANGUAGE_CHANGE","§aEpicPvP will now be displayed in §eEnglish§a!");
			add(type,"HUB_VERSUS_1VS1_QUESTION","§7You invite §a{INPUT0}§7 to a 1vs1 round!");
			add(type,"HUB_VERSUS_1VS1_FROM_QUESTION","§7You was invited from §a{INPUT0}§7 to a 1vs1 round!");
			add(type,"HUB_VERSUS_1VS1_NO_FREE_ARENAS","§cPlease wait all arenas are in inGame mode!");
			add(type,"OPEN_CHEST_WITH_WEAPON","§cYou cannot open chests with a weapon in your hand!");
			add(type,"EXP_HIS_TO_ME","§7You recived §e{INPUT1} Exp§7 from §a{INPUT0}!");
			add(type,"EXP_ME_TO_HIS","§7You send §e{INPUT1} Exp§7 to §a{INPUT0}§7!");
			add(type,"GO","§a§lGO");
			add(type,"TELEPORT_TO_DEATHMATCH_ARENA","§7You will get teleported to the Deathmatch-Arena in §e{INPUT0}§7!");
		    add(type, "CAPTCHA_ENTER", "§aPlease enter the Captcha Text §7/captcha [CAPTCHA]");
		    add(type, "CAPTCHA_FALSE", "§cCaptcha is incorrect!");
		    add(type, "CAPTCHA_FIRST_ENTER", "§cPlease enter the Captcha first, before register §7/captcha [CAPTCHA]");
	        add(type, "CAPTCHA_CHANGE", "§cCaptcha changed!");
	        add(type,"EXT","You are no longer on fire!");
			add(type,"EXT_ALL","Now, no one is on fire anymore!");
			add(type,"HEAD_ITEM_EQUAL_NULL","You do not have an item in your hand!");
			add(type,"HEAD_ITEM_NOT_BLOCK","This is not a block!");
			add(type,"HEAD_HELM_NOT_NULL","Your Head is not empty?");
			add(type,"HEAD","You are now wearing this block as head!");
			add(type,"PVP_KILL","§2You killed §e{INPUT0} §2and get §e{INPUT1} §2FAME from him!");
			add(type,"PVP_DEATH","§cBecause you were killed, your fame has been reset to start value!");
			add(type,"DELIVERY_USED","§7You can use this Item in §c{INPUT0}");
			add(type,"DELIVERY_LOTTO_USED","§cCurrently in use!");
			add(type,"DELIVERY_HM_3","§6§lCLICK ME");
			add(type,"DELIVERY_HM_1","{INPUT0} Reward!");
			add(type,"DELIVERY_HM_1_MORE","{INPUT0} Rewards!");
			add(type,"DELIVERY_ITEM_1","{INPUT0} Rewards");
			add(type,"TWITTER_ACC_NOT","§cYou have no Twitter account added!");
			add(type,"TWITTER_FOLLOW_N","§cYou do not follow @EpicPvPMC any more!");
			add(type,"TWITTER_REMOVE","§cYou Twitter account has been removed!");
		}
		
		if(type==LanguageType.GERMAN){
			if(!list.containsKey(LanguageType.GERMAN))list.put(LanguageType.GERMAN, new HashMap<String,String>());
			add(type, "CAPTCHA_ENTER", "§aBitte gib den Captcha Text §7/captcha [CAPTCHA]");
	        add(type, "CAPTCHA_CHANGE", "§cDas Captcha wurde geaendert!");
		    add(type, "CAPTCHA_FALSE", "§cDas Captcha ist falsch!");
		    add(type, "CAPTCHA_FIRST_ENTER", "§cGib bitte erst das Captcha ein, bevor du dich registrierst §7/captcha [CAPTCHA]");
			add(type,"GAME_LEAVE","§a{INPUT0} §7hat das Spiel verlassen!");
			add(type,"GAME_ENTER","§a{INPUT0} §7hat das Spiel betreten! §e({INPUT1}/{INPUT2})");
			add(type, "MSG_NOT_FOUND", "§cDie Nachricht §e{INPUT0} §cwurde nicht gefunden! Bitte kontaktiere einen Administrator.");
			add(type, "NOT_ENOUGH_MONEY", "§cDu hast nicht genug Geld!");
	        add(type, "TREASURE_CHEST_TIME_AWAY", "§cDie Zeit ist abgelaufen!");
	        add(type, "VERSUS_ADDED", "§aDu wurdest zur Warteliste hinzugefuegt!");
			add(type, "VERSUS_REMOVE", "§aDu wurdest von der Warteliste entfernt!");
	        add(type, "VERSUS_PLACE", "§7Du befindest dich auf Platz §e{INPUT0}");
	        add(type, "TRACKING_RANGE", "§7Die Player Tracking Range ist nun §e{INPUT0}§7.");
	        add(type, "NO_CHARAKTER", "§cEs sind ungueltige Zeichen im Namen!");
	        add(type, "STATS_PREFIX", "§b[===========§6 §lSTATS §b===========]");
	        add(type, "STATS_PREFIXBY", "§b[=========== §6§lStats von {INPUT0}§b ===========]");
	        add(type, "STATS_KILLS", "§6Kills : §7");
	        add(type,"STATS_FAME","§6Fame: §7");
	        add(type, "STATS_DEATHS", "§6Tode : §7");
	        add(type, "STATS_MONEY", "§6Geld : §7");
	        add(type, "STATS_KDR", "§6KDR : §7");
	        add(type, "STATS_GILDE", "§6Gilde : §7");
	        add(type, "STATS_RANKING", "§6Ranking : §7");
	        add(type, "PREMIUM_PET", "§cKaufe dir §6Premium§c um diese Funktion nutzen zu koennen!");
	        add(type, "TREASURE_CHEST_TOO_NEAR", "§cDu bist zu nah an einer anderen TreasureChest!");
	        add(type, "CHAT_MESSAGE_BLOCK", "§cBitte unterlasse solche Woerter im Chat!");
	        add(type, "SPIELER_ENTFERNT_COMPASS", "§7Der Spieler §a{INPUT0}§7 ist §e{INPUT1}§7 Bloecke weit von dir enfternt!");
	        add(type, "AUßERHALB_DER_MAP", "§cDu bist außerhalb der Map!");
	        add(type, "COINS_DEL_PLAYER", "§7Dem Spieler §a{INPUT0}§7 wurden §c{INPUT1}§7 entfernt!");
	        add(type, "COINS_ADD_PLAYER", "§7Dem Spieler §a{INPUT0}§7 wurden §a{INPUT1}§7 hinzugefuegt!");
	        add(type, "GEMS_DEL_PLAYER", "§7Dem Spieler §a{INPUT0}§7 wurden §c{INPUT1}§7 entfernt!");
	        add(type, "GEMS_ADD_PLAYER", "§7Dem Spieler §a{INPUT0}§7 wurden §a{INPUT1}§7 hinzugefuegt!");
	        add(type, "CMD_MUTE", "§cDie Commands wurden gesperrt!");
	        add(type, "CMD_UNMUTE", "§aDie Commands wurde entsperrt!");
	        add(type, "PVP_MUTE", "§aPvP wurde deaktiviert!");
	        add(type, "PVP_UNMUTE", "§cPvP wurde aktiviert!");
	        add(type, "CHAT_MUTE", "§cDer Chat wurde gesperrt!");
	        add(type, "CHAT_UNMUTE", "§aDer Chat wurde entsperrt!");
	        add(type, "MORE_HAND", "§aDu hast nun 64x.");
	        add(type, "MORE_INV", "§aDeine Items wurden auf 64x aufgerundet!");
	        add(type, "ENDERGAMES_TELEPORT", "§7Du hast deine Position mit §a{INPUT0}§7 gewechselt!");
	        add(type, "KIT_SHOP_ADD", "§aDu hast das Kit §e{INPUT0}§a gewaehlt!");
	        add(type, "KIT_SHOP_NO_MONEY", "§cDu hast nicht genug §7{INPUT0}§c!");
	        add(type, "KIT_SHOP_BUYED_KIT", "§aDu hast erfolgreich das Kit §e{INPUT0}§a gekauft!");
	        add(type, "PLAYER_IS_OFFLINE", "§cDer Spieler §e{INPUT0}§c ist Offline!");
	        add(type, "FIGHT_START_IN", "§7Ihr koennt in §e{INPUT0}§7 Sekunden Kaempfen!");
	        add(type, "FIGHT_START", "§aIhr koennt nun Kaempfen!");
	        add(type, "GAME_END_IN", "§cDas Spiel endet in §e{INPUT0}§c ");
	        add(type, "GAME_END", "§cDas Spiel wurde beendet.");
	        add(type, "RESTART_IN", "§4§lDer Server Restartet in §e§l{INPUT0}§4§l Sekunden!");
	        add(type, "RESTART", "§4§lDer Server Restartet jetzt!");
	        add(type, "SERVER_FULL", "§cDer Server ist voll!");
	        add(type, "SERVER_NOT_LOBBYPHASE", "§cDieser Server ist aktuell nicht in der Lobbyphase!");
	        add(type, "SERVER_FULL_WITH_PREMIUM", "§cDer Server ist voll! Kaufe dir Premium auf §6shop.EpicPvP.de§c um auf volle Servern zu joinen!");
	        add(type, "KILL_BY", "§a{INPUT0}§7 wurde von §e{INPUT1}§7 getoetet!");
	        add(type, "DEATH", "§a{INPUT0}§7 ist gestorben!");
	        add(type, "GAME_EXCLUSION", "§c{INPUT0} wurde vom Spiel ausgeschlossen!");
	        add(type, "PERK_NOT_BOUGHT", "§cDu hast noch keine Perks gekauft! §6Shop.EpicPvP.de");
	        add(type, "VOTE_TEAM_ADD", "§aDu bist nun in Team §7{INPUT0}§a");
	        add(type, "VOTE_TEAM_MIN_PLAYER", "§cEs muessen min. {INPUT0} Spieler online sein!");
	        add(type, "VOTE_TEAM_REMOVE", "§aDu hast §7{INPUT0}§a verlassen!");
	        add(type, "VOTE_TEAM_FULL", "§7{INPUT0}§c ist voll!");
	        add(type, "VOTE_MIN", "§cEs muessen mindestens §e{INPUT0}§c Spieler online sein damit das Spiel starten kann!");
	        add(type, "KICKED_BY_PREMIUM", "§cDu wurdest vom Server gekickt damit ein Premium User spielen kann! Du moechtest auch? §6shop.EpicPvP.de");
	        add(type, "PREFIX_GAME", "§6{INPUT0}§8» §7");
	        add(type, "PREFIX", "§6EpicPvP §8» §7");
	        add(type, "TEAM_WIN", "§aDas Team {INPUT0}§a hat das Spiel Gewonnen!");
	        add(type, "TEAM_OUT", "§7{INPUT0}§c ist gefallen!");
	        add(type, "RESTART_FROM_ADMIN", "§cDer Server wurde von einem Administrator restartet!");
	        add(type, "SCHUTZZEIT_END_IN", "§7Die Schutzzeit endet in §e{INPUT0}§7 Sekunden!");
	        add(type, "SCHUTZZEIT_END", "§aDie Schutzzeit ist nun zuende, ihr koennt euch bekriegen!");
	        add(type, "GAME_START", "§aDas Spiel ist gestartet!");
	        add(type, "GAME_START_IN", "§7Das Spiel startet in §e{INPUT0}§7 Sekunden!");
	        add(type, "DEATHMATCH_START", "§aDas Deathmatch startet");
	        add(type, "DEATHMATCH_START_IN", "§7Das Deathmatch startet in §e{INPUT0}§7 Sekunden!");
	        add(type, "DEATHMATCH_END", "§cDas Deathmatch ist zu Ende!");
	        add(type, "GAME_WIN", "§aDer Spieler §7{INPUT0}§a hat das Spiel gewonnen!");
	        add(type, "SURVIVAL_GAMES_DISTRICT_WIN", "§aDas District §e{INPUT0}§a mit den Spielern §7{INPUT1}§a und §7{INPUT2}§a hat Gewonnen!");
	        add(type, "DEATHMATCH_END_IN", "§cDas Deathmatch endet in §e{INPUT0}§c Sekunden");
	        add(type, "LOGIN_MESSAGE", "§cBitte logge dich ein: §e/Login <Password>");
	        add(type, "KICK_BY_FALSE_PASSWORD", "§cDu hast §e{INPUT0}§c mal dein Password falsch eingegeben");
	        add(type, "LOGIN_ACCEPT", "§aDu hast dich erfolgreich eingeloggt!");
	        add(type, "LOGIN_DENY", "§cDas Password ist falsch!");
	        add(type, "REGISTER_ACCEPT", "§aRegister erfolgreich!");
	        add(type, "REGISTER_MESSAGE", "§cRegistriere dich bitte: §e/Register <Password>");
	        add(type, "SPECTATOR_CHAT_CANCEL", "§cDu kannst als Spectator nicht im Chat schreiben!");
	        add(type, "TTT_WIN", "§aDie §7§l{INPUT0}§a haben diese Runde gewonnen!");
	        add(type, "TTT_DEATH", "§7Der Spieler §a{INPUT0}§7 ist gestorben und war §e{INPUT1}§7!");
	        add(type, "TTT_TESTER_WAS_USED", "§cDer Tester ist im moment nicht Freigegeben!");
	        add(type, "TTT_TRAITOR_CHAT", "§7Um den §cTraitor-Chat§7 zu benutzten §c/tc [Nachricht]§7!");
	        add(type, "TTT_TESTER_USED", "§cDer Tester ist momentan nicht frei!");
	        add(type, "TTT_TESTER_TIME", "§cDu warst schon im Tester kommer spaeter wieder!");
	        add(type, "TTT_TRAITOR_SHOP", "§cDu hast nicht genug Traitor-Punkte!");
	        add(type, "TTT_PASSE_USE", "§aDu hast einen §6{INPUT0}§a Pass benutzt! Du hast jetzt noch §e{INPUT1}§a!");
	        add(type, "TTT_PASSE_KEINE", "§cDu besitzt keine {INPUT0}§c Paesse!");
	        add(type, "TTT_PASSE_LOBBYPHASE", "§cDie Lobby Phase ist vorbei...");
	        add(type, "TTT_PASSE_MAX_USED", "§cEs wurden schon zu viele §e{INPUT0}§c Paesse benutzt!");
	        add(type, "TTT_SHOP", "§cDu hast nicht genug Punkte!");
	        add(type, "TTT_SHOP_BUYED", "§aDu hast erfolgreich ein Item erworben!");
	        add(type, "TTT_NPC_CLICKED", "§7Das ist §a{INPUT0}§7 er war ein §e{INPUT1}§7.");
	        add(type, "TTT_DNA_TEST", "§7Der DNA-TEST hat ergeben, dass der Spieler §a{INPUT0}§7 von §e{INPUT1}§7 getoetet wurde.");
	        add(type, "TTT_TRAITOR_SHOP_RADAR_CHANGE", "§aDu hast den Spieler §a{INPUT0}§7 ausgewaehlt!");
	        add(type, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH", "§cBei diesem Spieler ist es zu spaet!");
	        add(type, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT", "§aDu hast erfolgreich §7{INPUT0}§a wiederbelebt!");
	        add(type, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER", "§aDu wurdest von §7{INPUT0}§a wiederbelebt!");
	        add(type, "TTT_TESTER_JOIN", "§a{INPUT0}§7 ist den Tester beigetreten!");
	        add(type, "TTT_LEICHE_IDENTIFIZIERT", "§7Die Leiche von §a{INPUT0}§7 wurde gefunden er war ein §e{INPUT1}§7!");
	        add(type, "TTT_IS_NOW", "§7Du bist ein §c{INPUT0}§7.");
	        add(type, "GEMS_ADD", "§aDu hast §e{INPUT0}§7 Gems erhalten!");
	        add(type, "GEMS_DEL", "§cDir wurden §e{INPUT0}§c Gems abgezogen!");
	        add(type, "COINS_ADD", "§aDu hast §e{INPUT0}§7 Coins erhalten!");
	        add(type, "COINS_DEL", "§cDir wurden §e{INPUT0}§c Coins abgezogen!");
	        add(type,"GILDE_PLAYER_IS_NOT_IN_GILDE","§cDu bist in keiner Gilde.");
	        add(type, "GAME_AUSGESCHIEDEN", "§a{INPUT0}§c wurde aus dem Spiel ausgeschlossen!");
	        add(type, "CAVEWARS_SPIDER_DEATH", "§7Das Schaf von Team {INPUT0}§7 wurde von §a{INPUT1}§7 getoetet!");
	        add(type, "SHEEPWARS_SHEEP_DEATH", "§7Team {INPUT0}§7 hat ihr Schaf verloren!");
	        add(type, "BEDWARS_BED_BROKE", "§7Team {INPUT0}§7 hat ihr Bett verloren!");
	        add(type, "GILDE_PLAYER_GO_OUT", "§b{INPUT0} §chat die Gilde verlassen.");
	        add(type, "GILDE_OWNER_NOT", "§cDu bist nicht der Owner der Gilde.");
	        add(type,"GILDE_PLAYER_IS_IN_GILDE","§cDu bist bereits in einer Gilde.");
	        add(type,"GILDE_PLAYER_IS_IN_GILDE1","§c{INPUT0} ist bereits in einer Gilde.");
	        add(type, "GILDE_MONEY_DEPOSIT", "§a{INPUT0}§7 hat §a{INPUT1} Epics§7 eingezahlt!");
	        add(type, "GILDE_MONEY_LIFTED", "§a{INPUT0}§7 hat§c {INPUT1} Epics§7 abgehoben.");
	        add(type, "GILDE_NOT_ENOUGH_MONEY", "§cEs ist nicht genug auf dem Gilden-Konto.");
	        add(type, "GILDE_CREATE", "§aDu hast erfolgreich die Gilde §6{INPUT0}§a gegruendet! Dein Gilden Home wurde auf deine aktuelle Position gesetzt.");
	        add(type, "GILDE_PLAYER_OFFLINE", "§cDer Spieler §e{INPUT0}§c ist offline.");
	        add(type, "GILDE_PREFIX", "§6Gilde §8» §7");
	        add(type, "GILDE_PLAYER_ENTRE", "§a{INPUT0}§7 ist der Gilde begetreten!");
	        add(type, "GILDE_NAME_LENGTH_MIN", "§cDer Gildenname muss laenger als §e{INPUT0}§c Zeichen sein!");
	        add(type, "GILDE_NAME_LENGTH_MAX", "§cDer Gildenname muss kleiner als §e{INPUT0}§c Zeichen sein!");
	        add(type, "GILDE_EXIST", "§cDer Gildenname wurde bereits benutzt!");
	        add(type, "GILDE_EXIST_NOT", "§cDiese Gilde exestiert nicht!");
	        add(type, "GILDE_DELETE", "§cDu hast die Gilde geloescht!");
	        add(type, "GILDE_EINLADEN", "§aDu hast §e{INPUT0}§a in die Gilde eingeladen!");
	        add(type, "GILDE_STATS_PREFIX", "§b[=========== §6§lGilden §b===========]");
	        add(type, "GILDE_STATS_PREFIXBY", "§b[=========== §6§lGilden Info von {INPUT0}§b ===========]");
	        add(type, "GILDE_EILADUNG", "§aDu wurdest in die Gilde §e{INPUT0}§a eingeladen!");
	        add(type, "GILDE_CLOSED", "§cDie Gilde wurde geschlossen!");
	        add(type, "GILDE_KICK_PLAYER", "§cDer Spieler §e{INPUT0}§c wurde aus der Gilde gekickt!");
	        add(type, "GILDE_SETHOME", "§aDu hast das Gildenhome gesetzt!");
	        add(type, "GILDE_SETISLAND", "§aDu hast eine Gildenisland erstellt!");
	        add(type, "GILDEN_NAME", "§cDu hast ein ungueltiges Zeichen in deinem Gildennamen!");
	        add(type, "GILDE_TELEPORT_CANCELLED", "§cDie Teleportation wurde abgebrochen!");
	        add(type, "GILDE_TELEPORTET", "§aDie Teleportation wurde durchgefuehrt!");
	        add(type, "GILDE_HOME", "§7Du wirst in §e{INPUT0}§7 Teleportiert...");
	        add(type, "GILDE_PLAYER_NICHT_EINGELADEN", "§cDu wurdest in keine Gilde eingeladen!");
	        add(type, "GILDE_PLAYER_JOIN", "§a{INPUT0}§7 hat den Server betreten!");
	        add(type,"GILDE_IS_NOT_IN_THE_GILD","§cDer Spieler §a{INPUT0}§c ist nicht in deiner Gilde!");
	        add(type, "FRIEND_HIT", "§cWillst du deinen Kameraden toeten?");
	        add(type, "FRIEND_PREFIX", "§6Freund §8» §7");
	        add(type, "FRIEND_EXIST", "§cDu bist bereits mit dem Spieler §a{INPUT0}§c befreundet!");
	        add(type, "FRIEND_SEND", "§aDu hast dem Spieler §e{INPUT0}§a eine Freundschaftsanfrage gesendet!");
	        add(type, "FRIEND_NOT", "§cDu bist nicht mit §e{INPUT0}§c befreundet!");
	        add(type, "FRIEND_DEL", "§cDeine Freundschaft zu §e{INPUT0}§c wurde beendet!");
	        add(type, "FRIEND_DEL_IN", "§aIn §6{INPUT0}§a Sekunden wird die Freundschaft mit §e{INPUT1}§a aufgeloest!");
	        add(type, "FRIEND_GET", "§aDu hast von §e{INPUT0}§a eine Freundschaftsanfrage erhalten!");
	        add(type, "FRIEND_NOW", "§aDu bist nun mit §e{INPUT0}§a befreundet!");
	        add(type, "FRIEND_ASK_NOT", "§aDu hast momentan keine Freundschaftsanfragen.");
	        add(type, "FRIEND_IS_FRIEND", "§aDu bist bereits mit §e{INPUT0}§a befreundet!");
	        add(type, "FRIEND_YOURE_SELF", "§cFor ever alone?");
	        add(type, "NEULING_SCHUTZ", "§cDer Spieler §e{INPUT0}§c ist noch im Neulingsschutz!");
	        add(type, "NEULING_SCHUTZ_YOU", "§cDu bist momentan noch im Neulingsschutz benutzte §7/neuling§c um den Schutz zu deaktivieren.");
	        add(type, "NEULING_END", "§aDu besitzt nun kein Neulingsschutz mehr!");
	        add(type, "NEULING_CMD", "§cDu bist kein Neuling.");
	        add(type, "ANTI_LOGOUT_FIGHT", "§cDu bist nun im Kampf, logge dich nicht aus!");
	        add(type, "ANTI_LOGOUT_FIGHT_END", "§aDu bist nun nicht mehr im Kampf!");
	        add(type, "TELEPORT_VERZÖGERUNG", "§7Du wirst in §e{INPUT0}§7 teleportiert.");
	        add(type, "TELEPORT", "§aDu wurdest erfolgreich teleportiert!");
	        add(type, "TELEPORT_HERE", "§e{INPUT0}§a wurde erfolgreich teleportiert!");
	        add(type, "ACCEPT", "§aDu hast die Anfrage angenommen!");
	        add(type, "DENY", "§cDu hast die Anfrage abgelehnt!");
	        add(type, "ACCEPT_FROM", "§e{INPUT0}§a hat die Anfrage angenommen!");
	        add(type, "DENY_FROM", "§e{INPUT0}§e hat die Anfrage abgelehnt!");
	        add(type, "NO_ANFRAGE", "§cDu hast keine Anfrage erhalten.");
	        add(type, "ME", "mir");
	        add(type, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", "§aDer Spieler §e{INPUT0}§a fragt, ob du dich zu ihn telepotieren moechtest. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny");
	        add(type, "TELEPORT_ANFRAGE_EMPFÄNGER", "§aDer Spieler §e{INPUT0}§a fragt, ob er sich zu dir telepotieren darf. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny");
	        add(type, "TELEPORT_ANFRAGE_SENDER", "§aDu hast §e{INPUT0}§a eine Teleport Anfrage gesendet!");
	        add(type, "WARP_EXIST", "§cDer Warp existiert nicht.");
	        add(type, "WARP_SET", "§aDer Warp §e{INPUT0}§a wurde gespeichert!");
	        add(type, "KIT_USE", "§aDu hast das Kit §e{INPUT0}§a ausgewaehlt!");
	        add(type, "KIT_SET", "§aDas Kit §e{INPUT0}§a wurde gespeichert!");
	        add(type, "KIT_EXIST", "§cDieses Kit existiert nicht!");
	        add(type, "KIT_DEL", "§aDas Kit §e{INPUT0}§a wurde entfernt!");
	        add(type, "KIT_DELAY", "§cDu kannst das Kit erst in §e{INPUT0}§c wieder benutzen!");
	        add(type, "CLEARINVENTORY", "§7Dein Inventar wurde geleert!");
	        add(type, "CLEARINVENTORY_OTHER", "§7Dein Inventar wurde von §a{INPUT0}§7 geleert!");
	        add(type, "CLEARINVENTORY_FROM_OTHER", "§aDu hast das Inventar von §e{INPUT0}§a geleert");
	        add(type, "CLEARINVENTORY_ALL", "§aDu hast das Inventar von allen Spielern geleert!");
	        add(type, "NO_ITEM_IN_HAND", "§cDu hast kein Item in der Hand!");
	        add(type, "ITEM_RENAME", "§aDas Item wurde umbenannt!");
	        add(type, "NO_ANSWER_PARTNER", "§cDu hast niemanden zum antworten.");
	        add(type, "SOCIALSPY_ON", "§aDu hast Socialspy aktiviert!");
	        add(type, "SOCIALSPY_OFF", "§cDu hast Socialspy deaktiviert!");
	        add(type, "MONEY_NO_DOUBLE", "§cEntweder ist das keine Zahl oder 0!");
	        add(type, "MONEY_ENOUGH_MONEY", "§cDu hast nicht genug Epics!");
	        add(type, "MONEY_SEND_TO", "§aDu hast dem Spieler §e{INPUT0}§7 {INPUT1}§a Epics gesendet!");
	        add(type, "MONEY_RECEIVE_FROM", "§aDu hast von §e{INPUT0}§7 {INPUT1}§a Epics erhalten!");
	        add(type, "FALLDOWN_BREWITEM_BLOCKED", "§cDu kannst erst in §e{INPUT0}§c dein Item benutzten!");
	        add(type, "VANISH_AN", "§aVanish wurde aktiviert!");
	        add(type, "VANISH_AUS", "§cVanish wurde deaktiviert!");
	        add(type, "UNKNOWN_CMD", "§7Much command. Such no. Many confuse. Wow.");
	        add(type, "NO_PERMISSION", "§cMany permissions, Such denied, Much no, Wow.");
	        add(type, "WHEREIS_TEXT", "§7Du befindest dich momentan auf dem §e{INPUT0}§7 Server.");
	        add(type, "FALLDOWN_NICHT_GENUG_POWER", "§cDu hast nicht genug Kraft!");
	        add(type, "SKYBLOCK_HAVE_ISLAND", "§cDu hast bereits eine Insel!");
	        add(type, "SKYBLOCK_NO_ISLAND", "§cDu besitzt keine Insel!");
	        add(type, "SKYBLOCK_PARTY_NO", "§cAktuell hast du keine Party am laufen!");
	        add(type, "SKYBLOCK_REMOVE_ISLAND", "§cDeine Insel wurde geloescht!");
	        add(type, "SKYBLOCK_CHANGE_BIOME", "§aDas Biom deiner Inesel wurde zu §e{INPUT0}§a geaendert!");
	        add(type, "SKYBLOCK_CREATE_ISLAND", "§aDeine Insel wurde erstellt!");
	        add(type, "SKYBLOCK_PLAYER_KICK", "§aDu hast den Spieler §e{INPUT0}§a von deiner Insel gekickt!");
	        add(type, "SKYBLOCK_PLAYER_KICKED", "§cDu wurdest von §e{INPUT0}s§c Insel gekickt!");
	        add(type, "SKYBLOCK_PLAYER_NOT_ON_YOUR_ISLAND", "§aDer Spieler §e{INPUT0}§a ist nicht auf deiner Insel!");
	        add(type, "NO_BEFEHL", "§cDieser Befehl exestiert nicht!");
	        add(type, "WARPS_EMPTY","§cEs exestieren noch keine Warps!");
	        add(type,"NOT_ENOUGH_EXP","§cDu hast nicht genug Exp!");
	        add(type, "SKYBLOCK_PARTY_EINLADEN_IS_IN", "§cDer Spieler ist bereits in einer Party.");
	        add(type, "SKYBLOCK_PARTY_IN", "§cDu bist bereits in einer Party. Tippe §7/party verlassen §cum sie wieder zu verlassen!");
	        add(type, "SKYBLOCK_PARTY_EINLADEN_NO", "§cDu wurdest in keine Party eingeladen.");
	        add(type, "SKYBLOCK_PARTY_ENTER", "§aDu bist der Party beigetreten!");
	        add(type, "SKYBLOCK_PARTY_ENTER_BY", "§aDer Spieler §e{INPUT0}§a ist der Party beigetreten!");
	        add(type, "SKYBLOCK_PARTY_SIZE", "§cDu kannst maximal §e{INPUT0}§c Spieler in deine Party einladen!");
	        add(type, "SKYBLOCK_PARTY_VOLL", "§cDie Party ist voll!");
	        add(type, "SKYBLOCK_PARTY_EINLADEN_INVITE", "§aDu wurdest von §e{INPUT0}§a in seine Skyblock-Part eingeladen. Wenn du annehmen moechtest §7/party annehmen");
	        add(type, "SKYBLOCK_PARTY_EINLADEN_IS", "§cDer Spieler §e{INPUT0}§c ist bereits zur Party eingeladen.");
	        add(type, "SKYBLOCK_PARTY_ERSTELLT", "§aDeine Skyblock-Party wurde erfolgreich erstellt!");
	        add(type, "SKYBLOCK_PARTY_NO_OWNER", "§cDu bist nicht der Owner dieser Party!");
	        add(type, "SKYBLOCK_PARTY_EINLADEN", "§aDu hast §e{INPUT0}§a in die Party eingeladen!");
	        add(type, "SKYBLOCK_PARTY_VERLASSEN", "§aDu hast die Party verlassen!");
	        add(type, "SKYBLOCK_PARTY_KICKEN", "§aDu hast §e{INPUT0}§a aus der Party gekickt!");
	        add(type, "SKYBLOCK_PARTY_SCHLIEßEN", "§cDie Party wurde geschlossen!");
	        add(type, "SKYBLOCK_PARTY_PLAYER_NOT", "§cDieser Spieler ist nicht in der Party!");
	        add(type, "SKYBLOCK_TELEPORT_HOME", "§aDu wurdest zu deiner Insel teleportiert!");
	        add(type, "SKYBLOCK_PREFIX", "§[=========== §6§lSkyBlock §b===========]");
	        add(type, "SKYBLOCK_PARTY_PREFIX", "§b[=========== §6§lSkyBlock Party §b===========]");
	        add(type, "SKYBLOCK_REMOVE_ISLAND_ONE", "§cDu kannst nur einmal am Tag deine Insel loeschen oder als §bUltra §ceine Gilden Insel erstellen!");
	        add(type, "SIGN_SHOP_NO_ITEM_ON_INV", "§cDu hast dieses Item nicht im Inventar oder zu wenig davon!");
	        add(type, "SIGN_SHOP_VERKAUFT_", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}:{INPUT2}§7 Verkauft und hast §a{INPUT3}§7 Epics erhalten!");
	        add(type, "SIGN_SHOP_VERKAUFT", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}§7 verkauft und hast §a{INPUT2} Epics §7erhalten!");
	        add(type, "SIGN_SHOP_DELAY", "§cDu kannst nur alle §e{INPUT0}§c Sekunden was verkaufen!");
	        add(type, "SIGN_SHOP_GET", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}§7 bekommen dir wurden §a{INPUT2} Epics §7abgezogen!");
	        add(type, "SIGN_SHOP_GET_", "§7Du hast §6{INPUT0}§7 mal §e{INPUT1}:{INPUT2}§7 bekommen dir wurden §a{INPUT3} Epics §7abgezogen!");
	        add(type, "kFLY_NOT_ON", "§cDer Flugmodus ist nicht aktiviert!");
	        add(type, "NO_INTEGER", "§cDas ist keine Zahl!");
	        add(type, "kFLY_SPEED", "§aFlug Geschwindigkeit wurde auf §e{INPUT0}§a geaendert!");
	        add(type, "kFLY_ON", "§aDer Flugmodus wurde aktiviert!");
	        add(type, "kFLY_OFF", "§cDer Flugmodus wurde deaktiviert!");
	        add(type, "kFLY_PVP_FLAG", "§cDu darfst im PvP-Bereich nicht fliegen!");
	        add(type, "SPAWN_SET", "§aDer Spawn-Punkt wurde gespeichert!");
	        add(type, "SPAWN_TELEPORT", "§aDu wurdest zum Spawn teleportiert!");
	        add(type, "GIVEALL", "§a{INPUT0}§7 hat jedem Spieler §6{INPUT1}§7 mal §e{INPUT2}§7 gegeben!");
	        add(type, "REPAIR_HAND", "§aDas Item in deiner Hand wurde repariert!");
	        add(type, "REPAIR_ALL", "§aAlle Items in deinem Inventory wurden repariert!");
	        add(type, "FEED", "§aDu hast nun kein Hunger mehr!");
	        add(type, "FEED_ALL", "§7Der Spieler §a{INPUT0}§7 hat deinen Hunger gestillt!");
	        add(type, "FEED_OTHER", "§aDu hast den Hunger von §e{INPUT0}§a gestillt!");
	        add(type, "HEAL", "§aDu hast nun wieder volles Leben!");
	        add(type, "HEAL_ALL", "§aDer Spieler §e{INPUT0}§a hat nun wieder volles Leben!");
	        add(type, "HEAL_OTHER", "§aDu hast §e{INPUT0}s§a geheilt!");
	        add(type, "DAY", "§aEs ist nun 4:55 Uhr.");
	        add(type, "NIGHT", "§aEs ist nun 18:34 Uhr.");
	        add(type, "SUN", "§aJetzt scheint die Sonne.");
	        add(type, "USE_ENDERPEARL_TIME", "§cDu kannst die Enderperle erst in §e{INPUT0}§c wieder benutzten!");
	        add(type, "USE_BEFEHL_TIME", "§cDu kannst den Befehl erst in §e{INPUT0}§c wieder benutzen!");
	        add(type, "NO_RANG", "§cMany permissions, such denied, much no, Wow.");
	        add(type, "HOME_SET", "§7Das Home §e{INPUT0}§7 wurde gespeichert!");
	        add(type, "HOME_EXIST", "§cDieses Home exestiert nicht.");
	        add(type, "HOME_DEL", "§cDas Home §e{INPUT0}§c wurde geloescht");
	        add(type, "HOME_SKYBLOCK_DELETE", "§aDie Homes von §e{INPUT0}§a wurden entfernt!");
	        add(type, "HOME_MAX", "§cDu darfst nur §e{INPUT0}§c Homes setzten!");
	        add(type, "HOME_QUESTION", "§7Der Spieler §a{INPUT0}§7 fragt, ob er ein Home setzen darf. §e/homeaccept §7oder §e/homedeny");
	        add(type, "HOME_ISLAND", "§aEs wurde eine Anfrage zum setzen des Homes abgesetzt!");
	        add(type, "WARP_DEL", "§aDer Warp §e{INPUT0}§a wurde geloescht!");
	        add(type, "WARP_EXIST", "§cDieser Warp exestiert nicht!");
	        add(type, "GILDE_PLAYER_LEAVE", "§a{INPUT0}§7 hat den Server verlassen!");
	        add(type, "VOTE_THX", "§aDanke fürs §a§lVoten§a du hast deine Belohnung erhalten!");
			add(type, "ANIT_LOGOUT_FIGHT_CMD", "§cDu kannst den Befehl §e/gilden home§c nicht im Kampf ausführen!");
			add(type,"XMAS_DOOR","§aDu hast das Türchen geöffnet und §e{INPUT0}§a Coins erhalten!");
			add(type,"XMAS_DAY","§cIst heute der {INPUT0}te?");
			add(type,"LOGIN_FAIL","§cUngültiges Zeichen!");
			add(type,"LOAD_PLAYER_DATA","§7Deine Spieler Informationen werden geladen...");
			add(type,"PET_MUST_BUYED_IN_SHOP","§cDu musst das Pet im Online-Store kaufen! §6Shop.EpicPvP.de");
			add(type,"GAME_TIME_CHANGE","§aDie Zeit wurde zu {INPUT0} geändert!");
			add(type,"EXP_HIS_TO_ME","§7Du hast von §a{INPUT0} §e{INPUT1} Exp§7 erhalten!");
			add(type,"EXP_ME_TO_HIS","§7Du hast §a{INPUT0} §e{INPUT1} Exp§7 gesendet!");
			add(type,"NOT_ENOUGH_COINS","§cDu hast nicht genug Coins.");
			add(type,"NOT_ENOUGH_GEMS","§cDu hast nicht genug Gems.");
			add(type,"GILDE_CMD1","§6/gilde erstellen [Gilde] §8|§7 Erstellt eine neue Gilde.");
			add(type,"GILDE_CMD2","§6/gilde einladen [Player] §8§8|§7 Lädt einen Spieler in die Gilde ein");
			add(type,"GILDE_CMD3","§6/gilde annehmen §8|§7 Nimmt Einladung an.");
			add(type,"GILDE_CMD4","§6/gilde ranking §8|§7 Zeigt die Top Ten an Gilden.");
			add(type,"GILDE_CMD5","§6/gilde verlassen §8|§7 Zum Verlassen/Schließen der Gilde.");
			add(type,"GILDE_CMD6","§6/gilde kicken [Player] §8|§7 Kickt einen Spieler aus der Gilde.");
			add(type,"GILDE_CMD7","§6/gilde info [Gilde] §8|§7 Zeigt Infos über eine Gilde.");
			add(type,"GILDE_CMD8","§6/gilde sethome §8|§7 setzt das Gilden-Home.");
			add(type,"GILDE_CMD9","§6/gilde home §8|§7 Teleportiert dich zum Gilden-Home.");
			add(type,"GILDE_CMD10","§6/gilde island §8|§7 Teleportiert dich zur Gilden-Insel.");
			add(type,"GILDE_CMD11","§6/gilde createisland §8|§7 Erstellt eine Gilden-Insel.");
			add(type,"GILDE_CMD12","§6/gilde money abheben [Money] §8|§7 Vom Gilden-Konto abheben.");
			add(type,"GILDE_CMD13","§6/gilde money einzahlen [Money] §8|§7 Auf dem Gilden-Konto einzahlen.");
			add(type,"HOMES_EMPTY","Du hast keine Homes");
			add(type,"KITS_EMPTY","Du hast keine Kits");
			add(type,"MONEY","Dein Kontostand beträgt:§3 ");
			add(type,"LOOK_ON_SPAWNER","Du musst einen MobSpawner ankucken.");
			add(type,"MOB_TYPE_NOT_FOUND","Mob Type nicht gefunden!");
			add(type,"EXP_MINUS","§cDu kannst keine Minus zahlen verschicken!");
			add(type,"SKYBLOCK_CMD1","§6/skyblock erstellen §8|§7 Erstelle deine Insel.");
			add(type,"SKYBLOCK_CMD2","§6/skyblock entfernen §8|§7 Lösche deine Insel.");
			add(type,"SKYBLOCK_CMD3","§6/skyblock home §8|§7 Teleportiere dich zu deiner Insel.");
			add(type,"SKYBLOCK_CMD4","§6/skyblock fixhome §8|§7 Teleportiere dich zu deiner Insel.");
			add(type,"SKYBLOCK_CMD5","§6/skyblock kick [Player] §8|§7 Kicke Spieler von deiner Insel.");
			add(type,"SKYBLOCK_CMD6","§6/skyblock newisland [Player] §8|§7 Erneurt die Insel.");
			add(type,"SKYBLOCK_CMD7","§6/skyblock check §8|§7 Erneuert die alten Gilden u. Islands.");
			add(type,"SKYBLOCK_CMD8","§6/skyblock entities [Player] §8|§7 Die Entitys werden entfernt.");
			add(type,"SKYBLOCK_CMD9","§6/homedelete [Player] §8|§7 Löschen von Homes auf deiner Insel.");
			add(type,"SKYBLOCK_CMD10","§6/homeaccept §8|§7 Annehmen von Homes.");
			add(type,"SKYBLOCK_CMD11","§6/homedeny §8|§7 Ablehnen von Homes.");
			add(type,"SKYBLOCK_CMD12","§6/party §8|§7 Party Menue.");
			add(type,"SKYBLOCK_PARTY_CMD1","§6/party erstellen §8|§7 Erstellt eine Party.");
			add(type,"SKYBLOCK_PARTY_CMD2","§6/party home §8|§7 Teleportiere dich zur Party.");
			add(type,"SKYBLOCK_PARTY_CMD3","§6/party verlassen §8|§7 Party verlassen.");
			add(type,"SKYBLOCK_PARTY_CMD4","§6/party annehmen §8|§7 Annehmen von Einladungen.");
			add(type,"SKYBLOCK_PARTY_CMD5","§6/party einladen [Player] §8|§7 Einladen zur Party.");
			add(type,"SKYBLOCK_PARTY_CMD6","§6/party schließen §8|§7 Schließt die Party.");
			add(type,"SKYBLOCK_PARTY_CMD7","§6/party kicken [Player] §8|§7 Kickt aus der Party.");
			add(type, "HUB_ITEM_COMPASS", "§aSpiele");
			add(type, "HUB_ITEM_CHEST", "§6Shop");
			add(type, "HUB_ITEM_BUCH", "§bSprache");
			add(type, "HUB_ITEM_NAMETAG_NICK_ON", "§5Nick §8» §aAn");
			add(type, "HUB_ITEM_NAMETAG_NICK_OFF", "§5Nick §8» §cAus");
			add(type, "HUB_ITEM_GREEN.DYE_PLAYERS_ON", "§aSpieler §8» §aAn");
			add(type, "HUB_ITEM_GRAY.DYE_PLAYERS_OFF", "§cSpieler §8» §cAus");
			add(type, "HUB_ITEM_NETHERSTAR", "§eLobby-Teleporter");
			add(type,"GAME_START_MIN_PLAYER","§cEs sind zu wenig Spieler(min. {INPUT0}) online! Wartemodus wird neugestartet!");
			add(type,"GAME_START_MIN_PLAYER2","§cEs sind zu wenig Spieler online! Wartemodus wird neugestartet!");
			add(type,"GAME_HOLOGRAM_SERVER","Server: {INPUT0}");
			add(type,"GAME_HOLOGRAM_MAP","Map: {INPUT0}");
			add(type,"GAME_HOLOGRAM_KILLS","Kills: {INPUT0}");
			add(type,"GAME_HOLOGRAM_DEATHS","Tode: {INPUT0}");
			add(type,"GAME_HOLOGRAM_BEDWARS","Betten Zerstört: {INPUT0}");
			add(type,"GAME_HOLOGRAM_CAVEWARS","Spinnen-Kills: {INPUT0}");
			add(type,"GAME_HOLOGRAM_WINS","Gewonnene Spiele: {INPUT0}");
			add(type,"GAME_HOLOGRAM_LOSE","Verlorene Spiele: {INPUT0}");
			add(type,"GAME_HOLOGRAM_GAMES","Gespielte Spiele: {INPUT0}");
			add(type,"GAME_HOLOGRAM_SHEEP","getötete Schafe: {INPUT0}");
			add(type,"GAME_HOLOGRAM_STATS","§a{INPUT0} §6§l Stats");
			add(type,"GAME_HOLOGRAM_POWER","Power: {INPUT0}");
			add(type,"GAME_HOLOGRAM_KARMA","Karma: {INPUT0}");
			add(type,"GAME_HOLOGRAM_TESTS","Tests: {INPUT0}");
			add(type,"GAME_HOLOGRAM_TRAITOR_POINTS","Traitor-Punkte: {INPUT0}");
			add(type,"GAME_HOLOGRAM_DETECTIVE_POINTS","Detektive-Punkte: {INPUT0}");
			add(type,"SCOREBOARD_COINS","§e§lCoins:");
			add(type,"SCOREBOARD_GEMS","§A§lGems:");
			add(type,"SCOREBOARD_RANK","§6§lRang:");
			add(type,"SCOREBOARD_FORUM","§c§lForum:");
			add(type,"SCOREBOARD_ONLINE_STORE","§a§lOnline-Shop:");
			add(type,"SCOREBOARD_TS","§d§lTeamspeak:");
			add(type,"SCOREBOARD_NO_RANK","kein rang");
			add(type, "HOW_MANY_PLAYER_LEFT", "§e{INPUT0} §7verbleibende Spieler!");
			add(type,"NO_KIT","Kein Kit");
			add(type,"PLAYER_SET_NICK","Dein aktueller Nickname lautet: {INPUT0}");
			add(type,"NO_TEAMS_ALLOWED","§c§lKeine Teams erlaubt");
			add(type,"PLAYER_DISALLOW_TEMPORÄR","Dein Nickname wurde kurzeitig deaktviert! Du kannst nun ungenickt in den Chat schreiben!");
			add(type,"GAME_TEAM_ITEM","§bWähle dein Team");
			add(type,"KIT_SHOP_CHOOSE","§aauwählen");
			add(type,"KIT_SHOP_BUY","§6Kaufen");
			add(type,"KIT_SHOP_ADMIN","§7Dieses Kit ist ein §cAdmin-Kit");
			add(type,"KIT_SHOP_SPEZIAL1","§7Dieses Kit ist ein §aSpezial-Kit");
			add(type,"KIT_SHOP_SPEZIAL2","§7Nur erhältlich zu Besonderen anlässen!");
			add(type,"KIT_SHOP_PREMIUM","§7Dieses Kit ist ein Premium-Kit!");
			add(type,"KIT_BACK","§czurück");
			add(type,"LANGUAGE_CHANGE","§aEpicPvP wird dir nun in §eDeutsch §aangezeigt!");
			add(type,"HUB_VERSUS_1VS1_QUESTION","§7Du hast §a{INPUT0}§7 eine 1vs1 anfrage gesendet!");
			add(type,"HUB_VERSUS_1VS1_FROM_QUESTION","§7Du hast von §a{INPUT0}§7 eine 1vs1 anfrage erhalten!");
			add(type,"HUB_VERSUS_1VS1_NO_FREE_ARENAS","§cAlle Arenen sind momentan besetzt...!");
			add(type,"OPEN_CHEST_WITH_WEAPON","§cDu kannst keine Kisten oeffnen, wenn du eine Waffe in der Hand hast.!");
			add(type,"GO","§a§lLOS");
			add(type,"TELEPORT_TO_DEATHMATCH_ARENA","§7Du wirst in §e{INPUT0} §7in die Deathmatch-Arena geportet!");
			add(type,"invalidFireworkFormat","§cFalsches Feuerwerk format!");
			add(type,"enchantmentNotFound","§cVerzauberung nicht gefunden!");
			add(type,"onlyPlayerSkulls","§cNur Spieler Skulls gehen!");
			add(type,"EXT","Du brennst nun nicht mehr!");
			add(type,"EXT_ALL","Nun brennt kein Spieler mehr!");
			add(type,"HEAD_ITEM_EQUAL_NULL","Du hast kein Item in deiner Hand!");
			add(type,"HEAD_ITEM_NOT_BLOCK","Das ist kein Block!");
			add(type,"HEAD_HELM_NOT_NULL","Du hast noch einen Hut an!");
			add(type,"HEAD","Du hast du diesen Block als Kopf!");
			add(type,"PVP_KILL","§2Du hast §e{INPUT0} §2getoetet und §e{INPUT1} §2FAME von ihm erhalten!");
			add(type,"PVP_DEATH","§cDa du getoetet wurdest, wurde dein FAME auf Startwert zurueckgesetzt!");
			add(type,"DELIVERY_USED","§7Du kannst das Item in §c{INPUT0}§7 benutzten");
			add(type,"DELIVERY_LOTTO_USED","§cIst momentan besetzt!");
			add(type,"DELIVERY_HM_3","§6§lKLICK MICH");
			add(type,"DELIVERY_HM_1","{INPUT0} Belohnung!");
			add(type,"DELIVERY_HM_1_MORE","{INPUT0} Belohnungen!");
			add(type,"DELIVERY_ITEM_1","{INPUT0} Belohnungen");
			add(type,"TWITTER_ACC_NOT","§cDu hast noch keinen Twitter Account hinzugefügt!");
			add(type,"TWITTER_FOLLOW_N","§cDu folgst @EpicPvPMC nicht mehr!");
			add(type,"TWITTER_REMOVE","§cDein Twitter Account wurde entfernt!");
		}
	}
	
	public static void add(LanguageType type,String name,String msg){
		if(!list.get(type).containsKey(name)){
			mysql.Update("INSERT INTO language (type,name,msg) VALUES ('"+type.getDef()+"','"+name+"','"+msg+"');");
			list.get(type).put(name, msg);
		}
	}
	
}

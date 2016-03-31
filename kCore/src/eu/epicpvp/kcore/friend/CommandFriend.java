package eu.epicpvp.kcore.friend;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandFriend implements CommandExecutor{
	
	Player p;
	FriendManager manager;
	
	public CommandFriend(FriendManager manager){
		this.manager=manager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "friend", alias = {"f","freund","friede"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		
		p = (Player)cs;
		if(args.length==0){

			p.sendMessage("�b�����������������������������6�l FRIEND �b����������������������������");
			p.sendMessage("�6/f hinzufügen [Player] �8|�7 Freundschaftanfrage senden.");
			p.sendMessage("�6/f entfernen [Player] �8|�7 Freundschaftanfrage zurück ziehen.");
			p.sendMessage("�6/f annehmen �8|�7 Freundschaftanfrage annehmen.");
			p.sendMessage("�6/f list �8|�7 Freundschaften auflisten.");
			p.sendMessage("�b�����������������������������6�l FRIEND �b����������������������������");
		}else{
			if(!manager.getFriendList().containsKey(UtilPlayer.getRealUUID(p)))manager.getFriendList().put(UtilPlayer.getRealUUID(p), manager.getFriendList(p));
			if(args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("hinzufügen")){
				if(args.length!=2)return false;
				String f = args[1];
				if(!UtilPlayer.isOnline(f)){
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "PLAYER_IS_OFFLINE",f));
					return false;
				}
				Player friend = Bukkit.getPlayer(f);
				
				if(manager.getFriendList().containsKey(UtilPlayer.getRealUUID(p))&&manager.getFriendList().get(UtilPlayer.getRealUUID(p)).contains( UtilPlayer.getRealUUID(friend) )){
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_EXIST",args[1]));
					return false;
				}
				
				if(f.equalsIgnoreCase(p.getName())){
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_YOURE_SELF"));
					return false;
				}
				if(!UtilPlayer.isOnline(f)){
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "PLAYER_IS_OFFLINE",f));
					return false;
				}
				manager.getAnfrage().put(friend, p);
				friend.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_GET",p.getName()));
				p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_SEND",friend.getName()));
			}else if(args[0].equalsIgnoreCase("del")||args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("entfernen")){
				if(args.length!=2)return false;
				String friend = args[1].toLowerCase();
				if(friend.equalsIgnoreCase(p.getName())){
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_YOURE_SELF"));
					return false;
				}
				UUID uuid;
				if(UtilPlayer.isOnline(friend)){
					 uuid = UtilPlayer.getRealUUID(Bukkit.getPlayer(friend));
				}else{
					 uuid = UtilPlayer.getUUID(friend, manager.getMysql());
				}
				
				if(manager.getFriendList().get(UtilPlayer.getRealUUID(p)).contains(uuid)){
					if(UtilPlayer.isOnline(friend)){
						manager.del_friend.put(Bukkit.getPlayer(friend), p);
						manager.del_friend_timer.put(Bukkit.getPlayer(friend), 10);
					}else{
						manager.DelFriend(p, uuid);
						if(manager.getFriendList().containsKey(uuid))manager.getFriendList().remove(uuid);
						manager.getFriendList().get( UtilPlayer.getRealUUID(p) ).remove(uuid);
						p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_DEL",friend));
					}
				}else{
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_NOT",friend));
				}
			}else if(args[0].equalsIgnoreCase("annehmen")||args[0].equalsIgnoreCase("agree")||args[0].equalsIgnoreCase("accept")){
				if(manager.getAnfrage().containsKey(p)){
					Player friend = manager.getAnfrage().get(p);
					manager.getAnfrage().remove(p);
					if(manager.getFriendList().get( UtilPlayer.getRealUUID(p) ).contains( UtilPlayer.getRealUUID(friend) )){
						p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_EXIST",friend.getName()));
						return false;
					}
					manager.addFriend(p, friend);
					
					if(friend.isOnline())friend.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_NOW",p.getName()));
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_NOW",friend.getName()));
				}else{
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+Language.getText(p, "FRIEND_ASK_NOT"));
				}
			}else if(args[0].equalsIgnoreCase("list")){
				if(manager.getFriendList().get(UtilPlayer.getRealUUID(p)).isEmpty()){
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+"Du hast keine Freunde!");
				}else{
					String l = "List: ";
					for(UUID n : manager.getFriendList().get(UtilPlayer.getRealUUID(p))){
						if(UtilPlayer.isOnline(n)){
							l=l+"�a"+Bukkit.getPlayer(n).getName()+"�7,";
						}else{
							if(Bukkit.getOfflinePlayer(n)==null)continue;
							l=l+"�c"+Bukkit.getOfflinePlayer(n).getName()+"�7,";
						}
					}
					l=l.substring(0, l.length()-1);
					p.sendMessage(Language.getText(p, "FRIEND_PREFIX")+l);
				}
			}
		}
		
		return false;
	}
}
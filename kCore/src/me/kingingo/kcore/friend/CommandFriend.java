package me.kingingo.kcore.friend;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFriend implements CommandExecutor{
	
	Player p;
	FriendManager manager;
	
	public CommandFriend(FriendManager manager){
		this.manager=manager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "friend", alias = {"f","freund"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		p = (Player)cs;
		if(args.length==0){
			p.sendMessage("�6/freund hinzuf�gen [Player] �8|�b Freundschaftanfrage senden.");
			p.sendMessage("�6/freund entfernen [Player] �8|�b Freundschaftanfrage zur�ck ziehen.");
			p.sendMessage("�6/freund annehmen �8|�b Freundschaftanfrage annehmen.");
			p.sendMessage("�6/freund list �8|�b Freundschaften auflisten.");
		}else if(args.length > 0){
			if(!manager.getFriendList().containsKey(p.getName().toLowerCase()))manager.getFriendList().put(p.getName().toLowerCase(), manager.getFriendList(p));
			if(args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("hinzuf�gen")){
				if(manager.getFriendList().containsKey(p.getName().toLowerCase())&&manager.getFriendList().get(p.getName().toLowerCase()).contains(args[1].toLowerCase())){
					p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_EXIST.getText(args[1]));
					return false;
				}
				String f = args[1];
				if(!UtilPlayer.isOnline(f)){
					p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(f));
					return false;
				}
				Player friend = Bukkit.getPlayer(f);
				manager.getAnfrage().put(friend, p);
				friend.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_GET.getText(p.getName()));
				p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_SEND.getText(friend.getName()));
			}else if(args[0].equalsIgnoreCase("del")||args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("entfernen")){
				String friend = args[1].toLowerCase();
				if(manager.getFriendList().get(p.getName().toLowerCase()).contains(friend.toLowerCase())){
					if(UtilPlayer.isOnline(friend)){
						manager.del_friend.put(Bukkit.getPlayer(friend), p);
						manager.del_friend_timer.put(Bukkit.getPlayer(friend), 10);
					}else{
						manager.DelFriend(p, friend);
						if(manager.getFriendList().containsKey(friend))manager.getFriendList().remove(friend);
						manager.getFriendList().get(p.getName().toLowerCase()).remove(friend);
						p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_DEL.getText(friend));
					}
				}else{
					p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_NOT.getText(friend));
				}
			}else if(args[0].equalsIgnoreCase("annehmen")||args[0].equalsIgnoreCase("accept")){
				if(manager.getAnfrage().containsKey(p)){
					Player friend = manager.getAnfrage().get(p);
					manager.getAnfrage().remove(p);
					if(manager.getFriendList().get(p.getName().toLowerCase()).contains(friend.getName().toLowerCase())){
						p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_EXIST.getText(friend.getName()));
						return false;
					}
					manager.addFriend(p, friend.getName());
					
					if(friend.isOnline())friend.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_NOW.getText(p.getName()));
					p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_NOW.getText(friend.getName()));
				}else{
					p.sendMessage(Text.FRIEND_PREFIX.getText()+Text.FRIEND_ASK_NOT.getText());
				}
			}else if(args[0].equalsIgnoreCase("list")){
				if(manager.getFriendList().get(p.getName().toLowerCase()).isEmpty()){
					p.sendMessage(Text.FRIEND_PREFIX.getText()+" Du hast keine Freunde!");
				}else{
					String l = "List: ";
					for(String n : manager.getFriendList().get(p.getName().toLowerCase())){
						if(UtilPlayer.isOnline(n)){
							l=l+"�a"+n+"�7,";
						}else{
							l=l+"�c"+n+"�7,";
						}
					}
					l=l.substring(0, l.length()-1);
					p.sendMessage(Text.FRIEND_PREFIX.getText()+l);
				}
			}
		}
		return false;
	}
	
}


package me.kingingo.kcore.JumpPad;

import java.util.HashMap;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilLocation;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class CommandJump extends kListener implements CommandExecutor {

	private JavaPlugin hub;
	private HashMap<Location,JumpPad> list = new HashMap<>();
	private Player player;
	
	//new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+i+".fromX"),hub.getConfig().getInt("Config.Jump."+i+".fromY"),hub.getConfig().getInt("Config.Jump."+i+".fromZ"))
	//new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+i+".toX"),hub.getConfig().getInt("Config.Jump."+i+".toY"),hub.getConfig().getInt("Config.Jump."+i+".toZ"))
	public CommandJump(JavaPlugin hub){
		super(hub,"Jump");
		this.hub=hub;
		for(int i = 0; i<50; i++){
			if(hub.getConfig().contains("Config.Jump."+i+".fromX")){
				JumpPad jump = new JumpPad(new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+i+".fromX"),hub.getConfig().getInt("Config.Jump."+i+".fromY"),hub.getConfig().getInt("Config.Jump."+i+".fromZ")),new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+i+".toX"),hub.getConfig().getInt("Config.Jump."+i+".toY"),hub.getConfig().getInt("Config.Jump."+i+".toZ")));
				list.put(new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+i+".fromX"),hub.getConfig().getInt("Config.Jump."+i+".fromY"),hub.getConfig().getInt("Config.Jump."+i+".fromZ")), jump);
				Log("Load Jump-"+i);
			}else{
				break;
			}
		}
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setjump", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(p.isOp()){
			if(p.getLocation().add(0,-1,0).getBlock().getType()==Material.GOLD_PLATE){
				p.sendMessage(Text.PREFIX.getText()+"§cDu musst auf einer Gold Platte stehen!");
				return false;
			}
			hub.getConfig().set("Config.Jump."+list.size()+".fromX", p.getLocation().getBlockX());
			hub.getConfig().set("Config.Jump."+list.size()+".fromZ", p.getLocation().getBlockZ());
			hub.getConfig().set("Config.Jump."+list.size()+".fromY", p.getLocation().getBlockY()-1);
			hub.saveConfig();
			player=p;
			p.sendMessage(Text.PREFIX.getText()+"§aDie Jump Platte "+list.size()+" wurde gesetzt!");
		}
		return false;
	}
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTER)return;
		for(Player player : UtilServer.getPlayers()){
			for(Location loc : list.keySet()){
				if( (loc.getBlockX()==player.getLocation().getBlockX() || loc.getBlockX()+1==player.getLocation().getBlockX() || loc.getBlockX()-1==player.getLocation().getBlockX())
						&&loc.getBlockY()==player.getLocation().getBlockY()-1
							&& (loc.getBlockZ()==player.getLocation().getBlockZ() || loc.getBlockZ()+1==player.getLocation().getBlockZ() || loc.getBlockZ()-1==player.getLocation().getBlockZ())){
					list.get(loc).jump(player,0);
				}
			}
		}
		
		for(int j = 0; j>list.values().size(); j++){
			if(!((JumpPad)list.values().toArray()[j]).getJump().isEmpty())for(int i = 0; i<((JumpPad)list.values().toArray()[j]).getJump().size(); i++)((JumpPad)list.values().toArray()[j]).jump(player, ((JumpPad)list.values().toArray()[j]).getJump().get(((Player)((JumpPad)list.values().toArray()[j]).getJump().keySet().toArray()[i]))+1);
		}
	}
	
	@EventHandler
	public void Sneak(PlayerToggleSneakEvent ev){
		if(player!=null&&player==ev.getPlayer()){
			hub.getConfig().set("Config.Jump."+list.size()+".toX", ev.getPlayer().getLocation().getBlockX());
			hub.getConfig().set("Config.Jump."+list.size()+".toZ", ev.getPlayer().getLocation().getBlockZ());
			hub.getConfig().set("Config.Jump."+list.size()+".toY", ev.getPlayer().getLocation().getBlockY()-1);
			hub.saveConfig();
			ev.getPlayer().sendMessage(Text.PREFIX.getText()+"§cDie Jump Platte "+list.size()+" wurde gesetzt!");
			JumpPad jump = new JumpPad(new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+list.size()+".fromX"),hub.getConfig().getInt("Config.Jump."+list.size()+".fromY"),hub.getConfig().getInt("Config.Jump."+list.size()+".fromZ")),new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+list.size()+".toX"),hub.getConfig().getInt("Config.Jump."+list.size()+".toY"),hub.getConfig().getInt("Config.Jump."+list.size()+".toZ")));
			list.put(new Location(Bukkit.getWorld("world"),hub.getConfig().getInt("Config.Jump."+list.size()+".fromX"),hub.getConfig().getInt("Config.Jump."+list.size()+".fromY"),hub.getConfig().getInt("Config.Jump."+list.size()+".fromZ")), jump);
			player=null;
		}
	}

}

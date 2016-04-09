package eu.epicpvp.kcore.JumpPad;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandJump extends kListener implements CommandExecutor {

	private HashMap<Location,JumpPad> list = new HashMap<>();
	private Player player;
	private kConfig config;
	
	public CommandJump(JavaPlugin hub){
		super(hub,"Jump");
		this.config=new kConfig(new File("plugins"+File.separator+hub.getPlugin(hub.getClass()).getName()+File.separator+"pad.yml"));
		JumpPad jump;
		for(String i : config.getPathList("Jump").keySet()){
			if(config.isSet("Jump."+i+".To")){
				jump = new TeleportPad(config.getLocation("Jump."+i+".From"), config.getLocation("Jump."+i+".To"));
				list.put(config.getLocation("Jump."+i+".From"), jump);
				logMessage("Load Jump "+i);
			}
		}
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "setjump", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(p.isOp()){
			if(p.getLocation().add(0,-1,0).getBlock().getType()==Material.GOLD_PLATE){
				p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§cDu musst auf einer Gold Platte stehen!");
				return false;
			}
			config.setLocation("Jump."+list.size()+".From", p.getLocation().add(0, 1, 0));
			config.save();
			player=p;
			p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§aDie Jump Platte "+list.size()+" wurde gesetzt!");
		}
		return false;
	}
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTER)return;
		for(Player player : UtilServer.getPlayers()){
			for(Location loc : list.keySet()){
				if(loc.getWorld()==player.getWorld()&&loc.distance(player.getLocation())<= 1.4){
					list.get(loc).doit(player,0);
				}
			}
		}
		
		for(int j = 0; j>list.values().size(); j++){
			if(!((JumpPad)list.values().toArray()[j]).getJump().isEmpty())for(int i = 0; i<((JumpPad)list.values().toArray()[j]).getJump().size(); i++)((JumpPad)list.values().toArray()[j]).doit(player, ((JumpPad)list.values().toArray()[j]).getJump().get(((Player)((JumpPad)list.values().toArray()[j]).getJump().keySet().toArray()[i]))+1);
		}
	}
	
	@EventHandler
	public void Sneak(PlayerToggleSneakEvent ev){
		if(player!=null&&player==ev.getPlayer()){
			config.setLocation("Jump."+list.size()+".To", ev.getPlayer().getLocation().add(0, 1, 0));
			config.save();
			ev.getPlayer().sendMessage(TranslationManager.getText(player, "PREFIX")+"§cDie Jump Platte "+list.size()+" wurde gesetzt!");
			JumpPad jump = new TeleportPad(config.getLocation("Jump."+list.size()+".From"),config.getLocation("Jump."+list.size()+".To"));
			list.put(config.getLocation("Jump."+list.size()+".From"), jump);
			player=null;
		}
	}

}

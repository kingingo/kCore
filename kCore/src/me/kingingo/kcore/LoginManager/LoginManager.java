package me.kingingo.kcore.LoginManager;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.LoginManager.Commands.CommandCaptcha;
import me.kingingo.kcore.LoginManager.Commands.CommandLogin;
import me.kingingo.kcore.LoginManager.Commands.CommandRegister;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PROTECTION_CAPTCHA;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilMap;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginManager extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private MySQL mysql;
	@Getter
	private HashMap<Player,String> Login = new HashMap<>();
	@Getter
	private HashMap<Player,String> Register = new HashMap<>();
	@Getter
	private ArrayList<Player> abfragen = new ArrayList<>();
	@Getter
	private HashMap<String,File> captchas = new HashMap<>();
	private boolean captcha = false;
	@Getter
	private String captcha_string=null;
	private long captcha_time=0;
	
	public LoginManager(JavaPlugin instance,MySQL mysql,CommandHandler cmdHandler){
		super(instance,"LoginManager");
		this.instance=instance;
		this.mysql=mysql;
		File[] maps = UtilMap.getMapPictures();
		
		for(File map : maps)captchas.put(map.getName().replaceAll(".png", ""), map);
		for(String code : captchas.keySet())Log("load Captcha: "+code);
		
		cmdHandler.register(CommandLogin.class, new CommandLogin(this));
		cmdHandler.register(CommandRegister.class, new CommandRegister(this));
		cmdHandler.register(CommandCaptcha.class, new CommandCaptcha(this));
	}
	
	@EventHandler
	public void Reveice(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof PROTECTION_CAPTCHA){
			captcha=((PROTECTION_CAPTCHA)ev.getPacket()).isCaptcha();
		}
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if((UtilServer.getLagMeter().getTicksPerSecond() > 14 ? ev.getType()==UpdateType.FAST : ev.getType()==UpdateType.SEC_2)){
			if(captcha){
				if(captcha_time-System.currentTimeMillis() <= 0){
					captcha_time=System.currentTimeMillis()+TimeSpan.MINUTE*5;
					captcha_string=((String)captchas.keySet().toArray()[UtilMath.r(captchas.keySet().toArray().length)]);
					UtilMap.MapRender(captchas.get(captcha_string));
					for(Player player : Register.keySet())if(Register.get(player)!=null)player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "CAPTCHA_CHANGE"));
				}
			}
			
			if(abfragen.isEmpty())return;
			
			new Thread(new Runnable(){
				@Override
				public void run(){
					try{
						Player player;
						for(int i = 0; i< (abfragen.size() < 10 ? abfragen.size() : 10) ;i++){
							player=abfragen.get(i);
							
							if(isLogin(player)){
								if(!isRegestriert(player)){
									if(captcha){
										Register.put(player, captcha_string);
										player.getInventory().clear();
										player.getInventory().setItem(0, UtilItem.RenameItem(new ItemStack(Material.MAP), "§aCAPTCHA"));
										player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "CAPTCHA_ENTER"));
									}else{
										Register.put(player, null);
										player.getInventory().clear();
										player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "REGISTER_MESSAGE"));
									}
								}else{
									Login.put(player, getPW(player));
									player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "LOGIN_MESSAGE"));
									getMysql().Update("INSERT INTO list_users_1 (name,uuid,password) SELECT '" +player.getName().toLowerCase()+"','"+UtilPlayer.getRealUUID(player)+"','"+Login.get(player)+"' FROM DUAL WHERE NOT EXISTS (SELECT name FROM list_users_1 WHERE name='" +player.getName().toLowerCase()+"');");
								}
							}else{
								//LOGGED IN
							}
							if(!abfragen.isEmpty())abfragen.remove(i);
						}
					}catch(IndexOutOfBoundsException e){
						
					}
					
				}
			}).start();
		}
		
		if(ev.getType()==UpdateType.MIN_32){
			UtilList.CleanList(Login);
			UtilList.CleanList(Register);
			UtilList.CleanList(abfragen);
		}
	}
	
	public void setUser(Player p,String pw, String ip){
		getMysql().Update("INSERT INTO list_users (name, money,password, ip, clanname, kills, deaths,offizier) VALUES ('" + p.getName().toLowerCase() + "','0', '" + pw + "', '" + ip + "', 'default', '0', '0','false');");
	}
	
	public void delLogin(String p){
		getMysql().Update("DELETE FROM list_login WHERE player='" + p.toLowerCase() + "'");
	}
	
	public boolean isRegestriert(Player p){
		boolean user = false;
		
		try{
			
			ResultSet rs = getMysql().Query("SELECT password FROM list_users WHERE name = '" + p.getName().toLowerCase() + "'");
			
			while(rs.next()){
				user = Boolean.valueOf(true);
			}
 			
			rs.close();
		}catch (Exception err){
			System.err.println(err);
		}
		
		return user;
	}
	
	public String getPW(Player p){
		String pw = "";
		
		try{
			
			ResultSet rs = getMysql().Query("SELECT password FROM list_users WHERE name = '" + p.getName().toLowerCase() + "'");
			
			while(rs.next()){
				pw = rs.getString(1);
			}
 			
			rs.close();
		}catch (Exception err){
			System.err.println(err);
		}
		
		return pw;
	}
	
	public boolean isLogin(Player p){
		boolean b = false;
		try {
			ResultSet rs = getMysql().Query("SELECT player FROM list_login WHERE player='" + p.getName().toLowerCase() + "'");

			while (rs.next()) {
				b = Boolean.valueOf(true);
			}

			rs.close();
		} catch (Exception err) {
			System.err.println(err);
		}
		return b;
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		abfragen.add(ev.getPlayer());
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(abfragen.contains(ev.getPlayer()))abfragen.remove(ev.getPlayer());
		if(Login.containsKey(ev.getPlayer()))Login.remove(ev.getPlayer());
		if(Register.containsKey(ev.getPlayer()))Register.remove(ev.getPlayer());
	}
	
	@EventHandler
	public void Command(PlayerCommandPreprocessEvent ev){
		if(!ev.getMessage().contains("/login")&&!ev.getMessage().contains("/register")&&!ev.getMessage().contains("/captcha")){
			if(Login.containsKey(ev.getPlayer()))ev.setCancelled(true);
			if(Register.containsKey(ev.getPlayer()))ev.setCancelled(true);
			if(abfragen.contains(ev.getPlayer()))ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Chat(AsyncPlayerChatEvent ev){
		if(Login.containsKey(ev.getPlayer()))ev.setCancelled(true);
		if(Register.containsKey(ev.getPlayer()))ev.setCancelled(true);
		if(abfragen.contains(ev.getPlayer()))ev.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Interact(PlayerInteractEvent ev){
		if(Login.containsKey(ev.getPlayer()))ev.setCancelled(true);
		if(Register.containsKey(ev.getPlayer()))ev.setCancelled(true);
		if(abfragen.contains(ev.getPlayer()))ev.setCancelled(true);
	}
	
	@EventHandler
	public void Movecancel(PlayerMoveEvent event){
		if(Login.containsKey(event.getPlayer())||Register.containsKey(event.getPlayer())||abfragen.contains(event.getPlayer())){
			if (UtilMath.offset2d(event.getFrom(), event.getTo()) == 0.0D) {
			      return;
			    }
			    event.setTo(event.getFrom());
		}
	  }
}

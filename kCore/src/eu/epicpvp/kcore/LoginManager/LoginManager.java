package eu.epicpvp.kcore.LoginManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.player.Setting;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutPlayerSettings;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.LoginManager.Commands.CommandCaptcha;
import eu.epicpvp.kcore.LoginManager.Commands.CommandLogin;
import eu.epicpvp.kcore.LoginManager.Commands.CommandRegister;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.Title;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class LoginManager extends kListener{

	@Getter
	private ClientWrapper client;
	@Getter
	private JavaPlugin instance;
	private CommandHandler commandHandler;
	@Getter
	private HashMap<String,String> login;
	@Getter
	private ArrayList<String> register;
	@Getter
	private HashMap<String,String> captcha;
	@Getter
	private HashMap<Player,Long> timer;
	private Title loginTitle = new Title("§c/login [Password]","");
	private Title captchaTitle = new Title("","§c/captcha [Captcha]");
	private Title registerTitle = new Title("§cRegister","");
	
	public LoginManager(JavaPlugin instance,CommandHandler commandHandler,ClientWrapper client) {
		super(instance, "LoginManager");
		this.instance=instance;
		this.client=client;
		this.commandHandler=commandHandler;
		this.login=new HashMap<>();
		this.captcha=new HashMap<>();
		this.register=new ArrayList<>();
		this.timer=new HashMap<>();
		this.loginTitle.setFadeInTime(0);
		this.captchaTitle.setFadeInTime(0);
		this.registerTitle.setFadeInTime(0);
		
		this.commandHandler.register(CommandCaptcha.class, new CommandCaptcha(this));
		this.commandHandler.register(CommandLogin.class, new CommandLogin(this));
		this.commandHandler.register(CommandRegister.class, new CommandRegister(this));
		Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "login");
		UtilServer.createUpdaterAsync(instance);
	}

	public void addPlayerToBGList(Player player){
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
		    out.writeUTF(player.getName());
			player.sendPluginMessage(instance, "login", b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(String playername){
		if(this.login.containsKey(playername))this.login.remove(playername);
		if(this.register.contains(playername))this.register.remove(playername);
		
		LoadedPlayer loadedplayer = this.client.getPlayerAndLoad(playername);
		loadedplayer.getSettings(Setting.PASSWORD).getAsync( new Callback<PacketOutPlayerSettings.SettingValue[]>() {

			@Override
			public void call(PacketOutPlayerSettings .SettingValue[] response) {
				if (response.length == 1 && response[0].getSetting() == Setting.PASSWORD){
					if(response[0].getValue()!=null){
						if(UtilPlayer.isOnline(playername)){
							loginTitle.setSubtitle(TranslationHandler.getText(Bukkit.getPlayer(playername), "LOGIN_MESSAGE"));
							loginTitle.send(Bukkit.getPlayer(playername));
						}
						
						login.put(playername, response[0].getValue());
						return;
					}
				}
				addCaptcha(playername);
			}
		} );
	}
	
	String alphabet = new String("0123456789abcdefghijklmnopqrstuvwxyz");
	public String randomString(int length) {
		String result = new String(); 
		for (int i=0; i<length; i++)result = result + alphabet.charAt(UtilMath.r(alphabet.length()));
		return result;
	}
	
	public void addCaptcha(String playername){
		if(!this.captcha.containsKey(playername))this.captcha.put(playername, randomString(5));
		sendCaptcha(playername);
	}
	
	public void sendCaptcha(String playername){
		if(UtilPlayer.isOnline(playername)){
			Bukkit.getPlayer(playername).sendMessage("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ");
			captchaTitle.setTitle("§a"+this.captcha.get(playername));
			captchaTitle.send(Bukkit.getPlayer(playername));
		}
	}
	

	@EventHandler
	public void message(UpdateEvent ev){
		if(ev.getType() == UpdateType.SEC_3){
			for(Player player : UtilServer.getPlayers()){
				if(this.login.containsKey(player.getName())){
					loginTitle.setSubtitle(TranslationHandler.getText(player, "LOGIN_MESSAGE"));
					loginTitle.send(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§c/login [Password]");
				}else if(this.register.contains(player.getName())){
					registerTitle.setSubtitle(TranslationHandler.getText(player, "REGISTER_MESSAGE"));
					registerTitle.send(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "REGISTER_MESSAGE"));
				}else if(this.captcha.containsKey(player.getName())){
					captchaTitle.setTitle("§a"+this.captcha.get(player.getName()));
					captchaTitle.send(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§c/captcha [CAPTCHA]");
				}
			}
		}
	}
	
	ArrayList<Player> list;
	@EventHandler
	public void timer(UpdateEvent ev){
		if(ev.getType() == UpdateType.SEC_2){
			if(list==null)this.list=new ArrayList<>();
			this.list.clear();
			for(Player player : this.timer.keySet()){
				if(player.isOnline()){
					if( (System.currentTimeMillis()-this.timer.get(player)) > TimeSpan.MINUTE * 2){
						player.kickPlayer("§cDie Zeit ist abgelaufen!");
					}else{
						continue;
					}
				}
				list.add(player);
			}
			
			for(Player player : list)this.timer.remove(player);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		if(this.captcha.containsKey(ev.getPlayer().getName())){
			sendCaptcha(ev.getPlayer().getName());
		}
		
		if(this.login.containsKey(ev.getPlayer().getName())){
			loginTitle.setSubtitle(TranslationHandler.getText(ev.getPlayer(), "LOGIN_MESSAGE"));
			loginTitle.send(ev.getPlayer());
		}
		this.timer.put(ev.getPlayer(), System.currentTimeMillis());
	}
	
	@EventHandler
	public void login(AsyncPlayerPreLoginEvent ev){
		load(ev.getName());
	}
}

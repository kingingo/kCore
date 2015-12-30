package me.kingingo.kcore.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Client.Events.ClientConnectEvent;
import me.kingingo.kcore.Client.Events.ClientDisconnectEvent;
import me.kingingo.kcore.Client.Events.ClientErrorConnectEvent;
import me.kingingo.kcore.Client.Events.ClientLostConnectionEvent;
import me.kingingo.kcore.Client.Events.ClientReceiveMessageEvent;
import me.kingingo.kcore.Client.Events.ClientSendMessageEvent;
import me.kingingo.kcore.Update.Updater;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Client {

	@Setter
	@Getter
	private Socket socket;
	@Getter
	private String host;
	@Getter
	private int port;
	@Getter
	private String name;
	@Getter
	Thread thread;
	@Setter
	@Getter
	private Scanner in;
	@Setter
	@Getter
	private PrintWriter out;
	@Getter
	private ClientListener listener;
	@Getter
	@Setter
	private boolean connected=false;
	
	public Client(JavaPlugin instance, String host,int port,String name){
		this.host=host;
		this.port=port;
		this.name=name;
		this.listener=new ClientListener(instance, this);
		connect();
	}
	
	public void connect(){
		try {
			setSocket(new Socket(getHost(),getPort()));
			getSocket().setSoTimeout(2147483647);
			Bukkit.getPluginManager().callEvent(new ClientConnectEvent());
			
			this.thread = new Thread()
		      {
				 
		        public void run() {
		        	
		        	 try {
		                 setIn(new Scanner(getSocket().getInputStream()));;
		                 setOut(new PrintWriter(getSocket().getOutputStream()));;
		                 getOut().println(name);
		                 getOut().flush();
		                 Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent(name));
		                 getOut().println("ping");
		                 getOut().flush();
		                 Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent("ping"));
		                 while(getIn().hasNext()){
		                	Bukkit.getPluginManager().callEvent(new ClientReceiveMessageEvent(getIn().nextLine().replaceAll("&", "§")));
		                 }
		                 disconnect(true);
		        	 }catch (Exception e){
		                 e.printStackTrace();
		             }
		        }
		        
		      };
		      
		      getThread().start();
		} catch (IOException e) {
			Bukkit.getPluginManager().callEvent(new ClientErrorConnectEvent());
		}
	}
	
	public void disconnect(boolean b){
		if(b){
			Bukkit.getPluginManager().callEvent(new ClientLostConnectionEvent());
		}else{
			Bukkit.getPluginManager().callEvent(new ClientDisconnectEvent());
		}
		try {
			getSocket().close();
			getThread().stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToServer(String m){
		if(getName().equalsIgnoreCase("TEST-SERVER"))return;
		m=m.replaceAll("§", "&");
		getOut().println(m);
		getOut().flush();
		Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent(m));
	}
	
}

package me.kingingo.kcore.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
	Socket s;
	@Setter
	@Getter
	String Host;
	@Setter
	@Getter
	int Port;
	@Setter
	@Getter
	String Name;
	@Getter
	Thread send;
	@Getter
	ArrayList<String> messagestosend = new ArrayList<>();
	ArrayList<String> cloned;
	Scanner in;
	PrintWriter out;
	
	public Client(String host,int port,String Name,JavaPlugin plugin,Updater updater){
		setHost(host);
		setPort(port);
		setName(Name);
		Bukkit.getPluginManager().registerEvents(new ClientListener(this), plugin);
		connect();
	}
	
	public void connect(){
		try {
			setS(new Socket(getHost(),getPort()));
			getS().setSoTimeout(2147483647);
			Bukkit.getPluginManager().callEvent(new ClientConnectEvent());
			cloned = new ArrayList<>();
			
			 send = new Thread()
		      {
				 
		        public void run() {
		        	
		        	 try {
		                 in = new Scanner(getS().getInputStream());
		                 out = new PrintWriter(getS().getOutputStream());
		                 out.println(Name);
		                 out.flush();
		                 Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent(Name));
		                 out.println("ping");
		                 out.flush();
		                 Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent("ping"));
		                 while(in.hasNext()){
		                		Bukkit.getPluginManager().callEvent(new ClientReceiveMessageEvent(in.nextLine()));
		                 }
		                 disconnect(true);
		        	 }catch (Exception e){
		                 e.printStackTrace();
		             }
		        }
		        
		      };
		      send.start();
			
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
			send.stop();
			getS().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToServer(String m){
		out.println(m);
		out.flush();
		Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent(m));
	}
	
}

package me.kingingo.kcore.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Client.Events.ClientConnectEvent;
import me.kingingo.kcore.Client.Events.ClientDisconnectEvent;
import me.kingingo.kcore.Client.Events.ClientErrorConnectEvent;
import me.kingingo.kcore.Client.Events.ClientLostConnectionEvent;
import me.kingingo.kcore.Client.Events.ClientReceiveMessageEvent;
import me.kingingo.kcore.Client.Events.ClientSendMessageEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ClientNW {

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
	private Thread writeThread;
	private HashMap<Integer,String> writeList;
	private int write_counter=0;
	@Getter
	private Thread readThread;
	@Getter
	private ClientListener listener;
	@Getter
	@Setter
	private boolean connected=false;
	@Setter
	@Getter
	private DataOutputStream output;
	@Setter
	@Getter
	private DataInputStream input;
	@Getter
	private JavaPlugin instance;
	private boolean on = true;
	
	public ClientNW(JavaPlugin instance, String host,int port,String name){
		this.writeList=new HashMap<>();
		this.host=host;
		this.instance=instance;
		this.port=port;
		this.name=name;
		UtilServer.createUpdater(instance);
//		this.listener=new ClientListener(instance, this);
		connect();
	}
	
	public void connect(){
		try {
			on=true;
			setSocket(new Socket(getHost(),getPort()));
			getSocket().setSoTimeout(2147483647);
			
			setOutput(new DataOutputStream(getSocket().getOutputStream()));
            setInput(new DataInputStream(getSocket().getInputStream()));
            
            setConnected(true);
            
            sendMessageToServer(name);
            sendMessageToServer("ping");
			
			Bukkit.getPluginManager().callEvent(new ClientConnectEvent());
			
			this.readThread = new Thread(){
		        public void run() {
		        	 try {
		                 String userInput;
		                
		                 while(on){
							try{
								if(input==null){
									disconnect(true);
									break;
								}
								if((userInput = input.readLine()) != null) {
									Bukkit.getPluginManager().callEvent(new ClientReceiveMessageEvent(userInput.replaceAll("&", "§")));
								}else{
									disconnect(true);
									break;
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
		                 
		        	 }catch (Exception e){
		                 e.printStackTrace();
		             }
		        }
		      };
		      getReadThread().start();
		      
		      this.writeThread = new Thread(){
			        public void run() {
			        	 try {
			     			HashMap<Integer,String> list=new HashMap<>();
			    			String message;
			                
			                 while(on){
								try{
									if(!writeList.isEmpty()){
										for(int a : writeList.keySet())list.put(a, writeList.get(a));
										for(int i : list.keySet()){
											writeList.remove(i);
											message=list.get(i);
											Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent(message));
											
											try {
												output.writeBytes(message);
												output.writeByte('\n');
												output.flush();
											}catch (IOException e) {
												e.printStackTrace();
											}
										}
										list.clear();
									}
									Thread.sleep(500);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
			                 
			        	 }catch (Exception e){
			                 e.printStackTrace();
			             }
			        }
			      };
			      getWriteThread().start();
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
		
		this.on=false;
		try {
			getReadThread().stop();
			getWriteThread().stop();
			getSocket().close();
			getInput().close();
			getOutput().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToServer(final String message){
		if(!isConnected())return;
		if(getName().equalsIgnoreCase("TEST-SERVER"))return;
		this.writeList.put(this.write_counter, message);
		this.write_counter++;
	}
	
}

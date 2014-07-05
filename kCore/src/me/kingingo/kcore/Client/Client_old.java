
package me.kingingo.kcore.Client;

//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import lombok.Getter;
//import me.kingingo.kcore.Client.Events.ClientConnectEvent;
//import me.kingingo.kcore.Client.Events.ClientDisconnectEvent;
//import me.kingingo.kcore.Client.Events.ClientErrorConnectEvent;
//import me.kingingo.kcore.Client.Events.ClientLostConnectionEvent;
//import me.kingingo.kcore.Client.Events.ClientSendMessageEvent;
//import me.kingingo.kcore.Update.Updater;
//
//import org.bukkit.Bukkit;
//import org.bukkit.plugin.java.JavaPlugin;

public class Client_old
{
//  int port;
//  String host;
//  final ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//  Socket sock;
//  Thread updateThread;
//  Thread send;
//  List cloned;
//  String st;
//  String Name;
//  Iterator localIterator;
//  Client_old instance;
//  List<String> messagestosend = new ArrayList();
//
//  public Client_old(int port, String host,String Name,JavaPlugin plugin,Updater updater) { 
//	this.instance = this;
//    setPort(port);
//    setHost(host);
//    this.Name=Name;
//    if(updater==null)updater=new Updater(plugin);
//    Bukkit.getPluginManager().registerEvents(new ClientListener(this), plugin);
//    connect();
//    }
//
//  public void connect()
//  {
//    try
//    {
//      final Socket s = new Socket(this.host, this.port);
//      this.sock = s;
//      if (Client_old.this.updateThread != null){
//    	  System.out.println("[CLIENT] stopped THREAD");
//    	  Client_old.this.updateThread.stop();
//    	  updateThread=null;
//      }
//      s.setSoTimeout(2147483647);
//	  Bukkit.getPluginManager().callEvent(new ClientConnectEvent());
//      cloned=new ArrayList();
//      send = new Thread()
//      {
//        public void run() {
//          try {
//            Scanner in = new Scanner(s.getInputStream());
//            PrintWriter out = new PrintWriter(s.getOutputStream());
//            sendMessageToServer(Name);
//            sendMessageToServer("ping");
//            
//            while (true) {
//            	
//            	if(messagestosend.isEmpty()){
//            		Thread.sleep(1000);
//            		continue;
//            	}
//            	cloned.clear();
//              for (localIterator = Client_old.this.messagestosend.iterator(); localIterator.hasNext(); cloned.add(st)) st = (String)localIterator.next();
//              for (int i = 0; i < cloned.size(); i++) {
//                out.println((String)Client_old.this.messagestosend.get(0));
//        		Bukkit.getPluginManager().callEvent(new ClientSendMessageEvent((String)Client_old.this.messagestosend.get(0)));
//                Client_old.this.messagestosend.remove(0);
//              }
//              out.flush();
//              if (Client_old.this.updateThread == null) {
//                Client_old.this.updateThread = new UpdateThread(in, Client_old.this.instance);
//                Client_old.this.updateThread.start();
//              }
//
//            }
//
//          }
//          catch (Exception e)
//          {
//            e.printStackTrace();
//          }
//        }
//      };
//     ex.execute(send);
//     
//    } catch (Exception e) {
//		Bukkit.getPluginManager().callEvent(new ClientErrorConnectEvent());
//    }
//  }
//
//  public void disconnect(boolean b)
//  {  
//    try {
//      this.sock.close();
//    }
//    catch (IOException e) {
//      e.printStackTrace();
//    }
//    
//    if(b){
//		Bukkit.getPluginManager().callEvent(new ClientLostConnectionEvent());
//	}else{
//		Bukkit.getPluginManager().callEvent(new ClientDisconnectEvent());
//	}
//    
//  }
//
//  public void ClearMessageList() {
//    this.messagestosend.clear();
//  }
//
//  public void sendMessageToServer(String message) {
//      this.messagestosend.add(message);
//  }
//
//  public int getPort()
//  {
//    return this.port; } 
//  public void setPort(int port) { this.port = port; } 
//  public String getHost() {
//    return this.host; } 
//  public void setHost(String host) { this.host = host; } 
//  public Socket getSock() {
//    return this.sock;
//  }
//  public Thread getUpdateThread() { return this.updateThread; } 
//  public Client_old getInstance() {
//    return this.instance;
//  }
}

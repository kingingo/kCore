package me.kingingo.kcore.Client;

import java.util.Scanner;

import me.kingingo.kcore.Client.Events.ClientReceiveMessageEvent;

import org.bukkit.Bukkit;

public class UpdateThread extends Thread
{
  Scanner scan = null;
  Client ser;
  String line;

  public UpdateThread(Scanner c, Client s)
  {
    this.scan = c;
    this.ser = s;
  }

  public void run() {
    while (this.scan.hasNext()){
      line = this.scan.nextLine();
		Bukkit.getPluginManager().callEvent(new ClientReceiveMessageEvent(line));
    }
    
    ser.disconnect(true);
  }
}

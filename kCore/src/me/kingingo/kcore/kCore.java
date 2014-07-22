package me.kingingo.kcore;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.plugin.java.JavaPlugin;

public class kCore extends JavaPlugin
{
  public void onEnable(){
		System.out.println("=======================================================");
	    System.out.println("|                                                     |");
	    System.out.println("|             kCore - a Bukkit Plugin API             |");
	    System.out.println("|    Copyright (C) 2013 - "+new SimpleDateFormat("yyyy").format(new Date())+"  kingingo              |");
	    System.out.println("|                                                     |");
	    System.out.println("|        This program is not free software!           |");
	    System.out.println("|    You are not allowed to use, modify or spread     |");
	    System.out.println("|  it without the agreement of the copyright holder.  |");
	    System.out.println("|                                                     |");
	    System.out.println("|      Contact: EpicPvP.eu & Skype: Starteaker        |");
	    System.out.println("|                                                     |");
	    System.out.println("=======================================================");
  }

  public void onDisable(){
	  
  }
}
package me.kingingo.kcore.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import me.kingingo.kcore.MySQL.Events.MySQLConnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLDisconnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.MySQL.Events.MySQLQueryEvent;
import me.kingingo.kcore.MySQL.Events.MySQLUpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.CustomTimingsHandler;

public class MySQL
{
  private String user = "";
  private String pass = "";
  private String host = "";
  private String db = "";
  private Connection connection;
  @Getter
  private JavaPlugin instance;
  private CustomTimingsHandler timings;

  public MySQL(String user,String pass,String host,String db,JavaPlugin plugin) {
	  Bukkit.getPluginManager().registerEvents(new MySQLListener(this), plugin);
	  this.timings=new CustomTimingsHandler("** MySQL");
	  this.user=user;
	  this.pass=pass;
	  this.instance=plugin;
	  this.host=host;
	  this.db=db;
	  connect();
	}
  
  public void close(){
	timings.startTiming();
    try
    {
      if (connection != null)
        connection.close();
      	Bukkit.getPluginManager().callEvent(new MySQLDisconnectEvent(this));
    }
    catch (Exception ex) {
    	System.err.println(ex);
    }
    timings.stopTiming();
  }

  public void Delete(String table,String where){
	  Update("DELETE FROM `"+table+"`"+(where!=null?" WHERE "+where:""));
  }
  
  public void Insert(String table,String content,String values){
	  Update("INSERT INTO "+table+" ("+content+") VALUES ("+values+");");
  }
  
  public ResultSet Query(String table,String select,String where){
	 return Query("SELECT "+select+" FROM `"+table+"`"+(where!=null?" WHERE "+where:""));
  }
  
  public void Update(String table,String set,String where){
	  Update("UPDATE "+table+" SET "+set+""+(where!=null?" WHERE "+where:""));
  }
  
  public void createTable(String table,String content){
	  Update("CREATE TABLE IF NOT EXISTS "+table+"("+content+");");
  }
  
  public void connect() {
		timings.startTiming();
	   try {
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db, 
				user, pass);
	} catch (SQLException e) {
		Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.CONNECT,e,this));
	    timings.stopTiming();
		return;
	}
	Bukkit.getPluginManager().callEvent(new MySQLConnectEvent(this));
    timings.stopTiming();
  }
  
  public boolean Update(String qry) {
	  timings.startTiming();
	  
	  try {
		  MySQLUpdateEvent ev=new MySQLUpdateEvent(qry,this);
		  Bukkit.getPluginManager().callEvent(ev);
		  Statement stmt = connection.createStatement();
		  stmt.executeUpdate(ev.getUpdater());
		  stmt.close();
		  timings.stopTiming();
		  return true;
		  } catch (Exception ex) {
			  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.UPDATE,ex,this));
			  timings.stopTiming();
			  return false;
		  }
  }
	    
  
  public Double getDouble(String qry){
	  timings.startTiming();
	  MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
	  Bukkit.getPluginManager().callEvent(ev);
	  ResultSet rs = null;
	  double o = 0.0;
	  try{
		  Statement stmt = connection.createStatement();
		  rs = stmt.executeQuery(ev.getQuery());
		  while(rs.next()){
			  o=rs.getDouble(1);
		  }
		 }catch (Exception ex) {
			 Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
		 }
	  timings.stopTiming();
	return o;
  }
  
  public Integer getInt(String qry){
	  timings.startTiming();
	  	MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
	  	Bukkit.getPluginManager().callEvent(ev);
	    ResultSet rs = null;
	    Integer o = null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(ev.getQuery());
	      while(rs.next()){
	    	  o=rs.getInt(1);
	      }
	    }
	    catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
	    }
		  timings.stopTiming();
	  return o;
  }
  
  public String getString(String qry){
	  timings.startTiming();
		  	MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
		  	Bukkit.getPluginManager().callEvent(ev);
		    ResultSet rs = null;
		    String o = "null";
		    try
		    {
		      Statement stmt = connection.createStatement();
		      rs = stmt.executeQuery(ev.getQuery());
		      while(rs.next()){
		    	  o=rs.getString(1);
		      }
		    }
		    catch (Exception ex) {
		    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
		    }
			  
		    timings.stopTiming();
		    return o;
		  
  }
  
  public Long getLong(String qry){
	  timings.startTiming();
			  MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
			  	Bukkit.getPluginManager().callEvent(ev);
			    ResultSet rs = null;
			    Long o = null;
			    try
			    {
			      Statement stmt = connection.createStatement();
			      rs = stmt.executeQuery(ev.getQuery());
			      while(rs.next()){
			    	  o=rs.getLong(1);
			      }
			    }
			    catch (Exception ex) {
			    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
			    }
				  timings.stopTiming();
			  return o;
  }
  
  public Object getObject(String qry){
	  timings.startTiming(); 
			MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
		  	Bukkit.getPluginManager().callEvent(ev);
		    ResultSet rs = null;
		    Object o= null;
		    try
		    {
		      Statement stmt = connection.createStatement();
		      rs = stmt.executeQuery(ev.getQuery());
		      while(rs.next()){
		    	  o=rs.getObject(1);
		      }
		    }
		    catch (Exception ex) {
		    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
		    }
			  timings.stopTiming();
		  return o; 
  }
  
  public ResultSet Query(String qry) {
	  timings.startTiming();
			  MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
			  	Bukkit.getPluginManager().callEvent(ev);
			    ResultSet rs = null;
			    try
			    {
			      Statement stmt = connection.createStatement();
			      rs = stmt.executeQuery(ev.getQuery());
			    }
			    catch (Exception ex) {
			    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
			    }
				  timings.stopTiming();
			    return rs;
	  }
}

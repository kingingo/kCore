package me.kingingo.kcore.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.kingingo.kcore.MySQL.Events.MySQLConnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.MySQL.Events.MySQLQueryEvent;
import me.kingingo.kcore.MySQL.Events.MySQLUpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQL
{
  private String user = "";
  private String pass = "";
  private String host = "";
  private String db = "";
  private Connection connection;

  public MySQL(String user,String pass,String host,String db,JavaPlugin plugin) {
	  Bukkit.getPluginManager().registerEvents(new MySQLListener(this), plugin);
	  this.user=user;
	  this.pass=pass;
	  this.host=host;
	  this.db=db;
	  connect();
	}
  
  public void close()
  {
    try
    {
      if (connection != null)
        connection.close();
    }
    catch (Exception ex) {
    	System.err.println(ex);
    }
  }

  public void connect() {
	   try {
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db, 
				user, pass);
	} catch (SQLException e) {
		Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.CONNECT,e));
		return;
	}
	Bukkit.getPluginManager().callEvent(new MySQLConnectEvent());
  }
  
  public void Update(String qry) {
	  Bukkit.getPluginManager().callEvent(new MySQLUpdateEvent(qry));
	    try {
	      Statement stmt = connection.createStatement();
	      stmt.executeUpdate(qry);
	      stmt.close();
	    } catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.UPDATE,ex));
	    }
	  }
  
  public Integer getInt(String qry){
	  Bukkit.getPluginManager().callEvent(new MySQLQueryEvent(qry));
	    ResultSet rs = null;
	    Integer o = null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(qry);
	      while(rs.next()){
	    	  o=rs.getInt(1);
	      }
	    }
	    catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex));
	    }
	  return o;
  }
  
  public String getString(String qry){
	  Bukkit.getPluginManager().callEvent(new MySQLQueryEvent(qry));
	    ResultSet rs = null;
	    String o = null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(qry);
	      while(rs.next()){
	    	  o=rs.getString(1);
	      }
	    }
	    catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex));
	    }
	  return o;
  }
  
  public Object getObject(String qry){
	  Bukkit.getPluginManager().callEvent(new MySQLQueryEvent(qry));
	    ResultSet rs = null;
	    Object o= null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(qry);
	      while(rs.next()){
	    	  o=rs.getObject(1);
	      }
	    }
	    catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex));
	    }
	  return o;
  }
  
  public ResultSet Query(String qry) {
	  Bukkit.getPluginManager().callEvent(new MySQLQueryEvent(qry));
	    ResultSet rs = null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(qry);
	    }
	    catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex));
	    }

	    return rs;
	  }
}

package me.kingingo.kcore.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.MySQL.Events.MySQLConnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLDisconnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.MySQL.Events.MySQLQueryEvent;
import me.kingingo.kcore.MySQL.Events.MySQLUpdateEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQL
{
  private String user = "";
  private String pass = "";
  private String host = "";
  private String db = "";
  private Connection connection;
  @Getter
  private JavaPlugin instance;
  @Getter
  @Setter
  private boolean debug=false;

  public MySQL(String user,String pass,String host,String db,JavaPlugin plugin) {
	  Bukkit.getPluginManager().registerEvents(new MySQLListener(this), plugin);
	  this.user=user;
	  this.pass=pass;
	  this.instance=plugin;
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
      	Bukkit.getPluginManager().callEvent(new MySQLDisconnectEvent(this));
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
		Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.CONNECT,e,this));
		return;
	}
	Bukkit.getPluginManager().callEvent(new MySQLConnectEvent(this));
  }
  
  public void Update(String qry) {
	  if(debug){
		  long time = System.currentTimeMillis();
		  
		  try {
		  	  MySQLUpdateEvent ev=new MySQLUpdateEvent(qry,this);
			  Bukkit.getPluginManager().callEvent(ev);
		      Statement stmt = connection.createStatement();
		      stmt.executeUpdate(ev.getUpdater());
		      stmt.close();
		    } catch (Exception ex) {
		    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.UPDATE,ex,this));
		    }
		  
	  	  	UtilServer.DebugLog(time, new String[]{"Update",qry}, "");
		  }else{
			  try {
			  	  MySQLUpdateEvent ev=new MySQLUpdateEvent(qry,this);
				  Bukkit.getPluginManager().callEvent(ev);
			      Statement stmt = connection.createStatement();
			      stmt.executeUpdate(ev.getUpdater());
			      stmt.close();
			    } catch (Exception ex) {
			    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.UPDATE,ex,this));
			    }
		  }
	  
	  }
	    
  
  public Double getDouble(String qry){
	  if(debug){
	  long time = System.currentTimeMillis();
	  
	  MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
	  	Bukkit.getPluginManager().callEvent(ev);
	    ResultSet rs = null;
	    double o = 0.0;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(ev.getQuery());
	      while(rs.next()){
	    	  o=rs.getDouble(1);
	      }
	    }
	    catch (Exception ex) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
	    }
	  
  	  	UtilServer.DebugLog(time, new String[]{"getDouble",qry}, "");
  	  	return o;
	  }else{
		  MySQLQueryEvent ev = new MySQLQueryEvent(qry,this);
		  	Bukkit.getPluginManager().callEvent(ev);
		    ResultSet rs = null;
		    double o = 0.0;
		    try
		    {
		      Statement stmt = connection.createStatement();
		      rs = stmt.executeQuery(ev.getQuery());
		      while(rs.next()){
		    	  o=rs.getDouble(1);
		      }
		    }
		    catch (Exception ex) {
		    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,ex,this));
		    }
			return o;
	  }
  }
  
  public Integer getInt(String qry){
	  if(debug){
	  long time = System.currentTimeMillis();
	  
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
	  
  	  	UtilServer.DebugLog(time, new String[]{"getInt",qry}, "");
  	  	return o;
	  }else{
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
	  return o;
	  }
  }
  
  public String getString(String qry){
	  if(debug){
		  long time = System.currentTimeMillis();
		  
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
		  
	  	  	UtilServer.DebugLog(time, new String[]{"getString",qry}, "");
	  	  	return o;
		  }else{
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
		    return o;
		  }
  }
  
  public Long getLong(String qry){
	  if(debug){
		  long time = System.currentTimeMillis();
		  
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
		  
	  	  	UtilServer.DebugLog(time, new String[]{"getLong",qry}, "");
			  return o;
		  }else{
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
			  return o;
		  }
	  	
  }
  
  public Object getObject(String qry){ 
	  if(debug){
	  long time = System.currentTimeMillis();
	  
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
	    
  	  	UtilServer.DebugLog(time, new String[]{"getObject",qry}, "");
		return o;
	  }else{
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
		  return o; 
	  }
  }
  
  public ResultSet Query(String qry) {
	  if(debug){
		  long time = System.currentTimeMillis();
		  
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
		    
	  	  	UtilServer.DebugLog(time, new String[]{"Query",qry}, "");
		    return rs;
		  }else{
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

			    return rs;
		  }
	  }
}

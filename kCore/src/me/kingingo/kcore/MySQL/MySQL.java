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
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQL {
	@Getter
	private JavaPlugin instance;
	private String user = "";
	private String pass = "";
	private String host = "";
	private String db = "";
	private Connection connection;

	public MySQL(String user, String pass, String host, String db, JavaPlugin plugin) {
		this.user = user;
		this.pass = pass;
		this.instance = plugin;
		this.host = host;
		this.db = db;
		connect();
		new MySQLListener(this);
		UtilServer.setMysql(this);
	}

	public void close() {
		try {
			if (connection != null){
				connection.close();
				Bukkit.getPluginManager().callEvent(new MySQLDisconnectEvent(this));
			}
			connection=null;
		} catch (Exception e) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.CLOSE, e, this));
		}
	}

	public void connect() {
		close();
		
		if(user==null||pass==null||host==null||db==null)return;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host
					+ ":3306/" + db + "?autoReconnect=true", user, pass);
		} catch (SQLException e) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.CONNECT, e, this));
			return;
		}
		Bukkit.getPluginManager().callEvent(new MySQLConnectEvent(this));
	}

	public void asyncUpdate(String qry) {
		if (connection == null)return;
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
				new Runnable() {

					@Override
					public void run() {
						try {
							Statement stmt = connection.createStatement();
							stmt.executeUpdate(qry);
							stmt.close();

						} catch (Exception ex) {
							ex.printStackTrace();

						}
					}
				});
	}

	public void asyncGetObject(String qry, Callback callback) {
		if (connection == null)return;
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
				new Runnable() {

					@Override
					public void run() {
						ResultSet rs = null;
						try {
							Statement stmt = connection.createStatement();
							rs = stmt.executeQuery(qry);
							while (rs.next())
								callback.done(rs.getObject(1));
							rs.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
	}

	public void asyncGetInt(String qry, Callback callback) {
		if (connection == null)return;
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
				new Runnable() {

					@Override
					public void run() {

						ResultSet rs = null;
						try {
							Statement stmt = connection.createStatement();
							rs = stmt.executeQuery(qry);
							while (rs.next())
								callback.done(rs.getInt(1));
							rs.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				});
	}

	public void asyncGetString(String qry, Callback callback) {
		if (connection == null)
			return;
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
				new Runnable() {

					@Override
					public void run() {

						ResultSet rs = null;
						try {
							Statement stmt = connection.createStatement();
							rs = stmt.executeQuery(qry);
							while (rs.next())
								callback.done(rs.getString(1));
							rs.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				});
	}

	public void asyncGetDouble(String qry, Callback callback) {
		if (connection == null)
			return;
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
				new Runnable() {

					@Override
					public void run() {

						ResultSet rs = null;
						try {
							Statement stmt = connection.createStatement();
							rs = stmt.executeQuery(qry);
							while (rs.next())
								callback.done(rs.getDouble(1));
							rs.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				});
	}

	public void asyncQuery(String qry, Callback callback) {
		asyncQuery(qry, callback, null);
	}

	public void asyncQuery(String qry, Callback callback, Callback noAnswer) {
		if (connection == null)
			return;
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
				new Runnable() {

					@Override
					public void run() {

						ResultSet rs = null;
						try {
							Statement stmt = connection.createStatement();
							rs = stmt.executeQuery(qry);
							boolean b = false;
							while (rs.next()) {
								callback.done(rs);
								b = true;
							}

							if (!b && noAnswer != null)
								noAnswer.done(false);
							rs.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				});
	}

	public void asyncDelete(String table, String where) {
		asyncUpdate("DELETE FROM `" + table + "`"
				+ (where != null ? " WHERE " + where : ""));
	}

	public void asyncInsert(String table, String content, String values) {
		asyncUpdate("INSERT INTO " + table + " (" + content + ") VALUES ("
				+ values + ");");
	}

	public void asyncQuery(String table, String select, String where,
			Callback callback) {
		asyncQuery("SELECT " + select + " FROM `" + table + "`"
				+ (where != null ? " WHERE " + where : ""), callback);
	}

	public void asyncUpdate(String table, String set, String where) {
		asyncUpdate("UPDATE " + table + " SET " + set + ""
				+ (where != null ? " WHERE " + where : ""));
	}

	public void asyncCreateTable(String table, String content) {
		asyncUpdate("CREATE TABLE IF NOT EXISTS " + table + "(" + content
				+ ");");
	}

	public void Delete(String table, String where) {
		Update("DELETE FROM `" + table + "`"
				+ (where != null ? " WHERE " + where : ""));
	}

	public void Insert(String table, String content, String values) {
		Update("INSERT INTO " + table + " (" + content + ") VALUES (" + values
				+ ");");
	}

	public ResultSet Query(String table, String select, String where) {
		return Query("SELECT " + select + " FROM `" + table + "`"
				+ (where != null ? " WHERE " + where : ""));
	}

	public void Update(String table, String set, String where) {
		Update("UPDATE " + table + " SET " + set + ""
				+ (where != null ? " WHERE " + where : ""));
	}

	public void createTable(String table, String content) {
		Update("CREATE TABLE IF NOT EXISTS " + table + "(" + content + ");");
	}

	public void Update(boolean async, String qry) {
		if (async) {
			asyncUpdate(qry);
		} else {
			Update(qry);
		}
	}

	public boolean Update(String qry) {
		if (connection == null)
			return false;
		try {
			MySQLUpdateEvent ev = new MySQLUpdateEvent(qry, this);
			Bukkit.getPluginManager().callEvent(ev);
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(ev.getUpdater());
			stmt.close();
			return true;
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.UPDATE, ex, this));
		}
		return false;
	}

	public Double getDouble(String qry) {
		if (connection == null)
			return 0.0;
		MySQLQueryEvent ev = new MySQLQueryEvent(qry, this);
		Bukkit.getPluginManager().callEvent(ev);
		ResultSet rs = null;
		double o = 0.0;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(ev.getQuery());
			while (rs.next()) {
				o = rs.getDouble(1);
			}
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.QUERY, ex, this));
		}
		return o;
	}

	public Integer getInt(String qry) {
		if (connection == null)
			return 0;
		MySQLQueryEvent ev = new MySQLQueryEvent(qry, this);
		Bukkit.getPluginManager().callEvent(ev);
		ResultSet rs = null;
		Integer o = null;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(ev.getQuery());
			while (rs.next()) {
				o = rs.getInt(1);
			}
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.QUERY, ex, this));
		}
		return o;
	}

	public String getString(String qry) {
		if (connection == null)
			return "null";
		MySQLQueryEvent ev = new MySQLQueryEvent(qry, this);
		Bukkit.getPluginManager().callEvent(ev);
		ResultSet rs = null;
		String o = "null";
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(ev.getQuery());
			while (rs.next()) {
				o = rs.getString(1);
			}
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.QUERY, ex, this));
		}
		return o;
	}

	public Long getLong(String qry) {
		if (connection == null)return 0L;
		MySQLQueryEvent ev = new MySQLQueryEvent(qry, this);
		Bukkit.getPluginManager().callEvent(ev);
		ResultSet rs = null;
		Long o = null;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(ev.getQuery());
			while (rs.next()) {
				o = rs.getLong(1);
			}
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.QUERY, ex, this));
		}
		return o;
	}

	public Object getObject(String qry) {
		if (connection == null)return null;
		MySQLQueryEvent ev = new MySQLQueryEvent(qry, this);
		Bukkit.getPluginManager().callEvent(ev);
		ResultSet rs = null;
		Object o = null;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(ev.getQuery());
			while (rs.next()) {
				o = rs.getObject(1);
			}
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.QUERY, ex, this));
		}
		return o;
	}

	public ResultSet Query(String qry) {
		if (connection == null)return null;
		MySQLQueryEvent ev = new MySQLQueryEvent(qry, this);
		Bukkit.getPluginManager().callEvent(ev);
		ResultSet rs = null;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(ev.getQuery());
		} catch (Exception ex) {
			Bukkit.getPluginManager().callEvent(
					new MySQLErrorEvent(MySQLErr.QUERY, ex, this));
		}
		return rs;
	}
}

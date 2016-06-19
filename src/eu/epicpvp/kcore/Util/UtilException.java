package eu.epicpvp.kcore.Util;

import java.util.EmptyStackException;

import eu.epicpvp.kcore.MySQL.MySQL;

public class UtilException {
	
	public static void catchException(String Server,String IP,MySQL sql,String msg){
		System.err.println("[EpicPvP] "+msg);
		
		sql.Update("CREATE TABLE IF NOT EXISTS list_exception(server varchar(30),ip varchar(30),timestamp timestamp,exceptiontype varchar(30),message varchar(200))");
		sql.Update("INSERT INTO list_exception (server,ip,exceptiontype,message) VALUES ('"+Server+"','"+IP+"','"+msg+"','"+msg+"');");
	}
	
	public static void catchException(Exception e,String Server,String IP,MySQL sql){
		String exception = "Exception";
		if(e instanceof NullPointerException){
			exception="NullPointerException";
		}else if(e instanceof NumberFormatException){
			exception="NumberFormatException";
		}else if(e instanceof ClassCastException){
			exception="ClassCastException";
		}else if(e instanceof IllegalMonitorStateException){
			exception="IllegalMonitorStateException";
		}else if(e instanceof EmptyStackException){
			exception="EmptyStackException";
		}else if(e instanceof ArithmeticException){
			exception="ArithmeticException";
		}else if(e instanceof ArrayIndexOutOfBoundsException){
			exception="ArrayIndexOutOfBoundsException";
		}else if(e instanceof UnsupportedOperationException){
			exception="UnsupportedOperationException";
		}
		
		System.err.println("[EpicPvP] "+exception+": "+e.getMessage());
		e.printStackTrace();
		
		sql.Update("CREATE TABLE IF NOT EXISTS list_exception(server varchar(30),ip varchar(30),timestamp timestamp,exceptiontype varchar(30),message varchar(200))");
		sql.Update("INSERT INTO list_exception (server,ip,exceptiontype,message) VALUES ('"+Server+"','"+IP+"','"+exception+"','"+e.getMessage()+"');");
	}
	
}

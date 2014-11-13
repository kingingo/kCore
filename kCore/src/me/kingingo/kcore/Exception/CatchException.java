package me.kingingo.kcore.Exception;

import java.util.EmptyStackException;

import me.kingingo.kcore.MySQL.MySQL;

public class CatchException {
	
	public void catchException(Exception e,String Server,String IP,MySQL sql){
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

		sql.Update("CREATE TABLE IF NOT EXISTS list_exception(server varchar(30),ip varchar(30),exceptiontype varchar(30),message varchar(200))");
		sql.Update("INSERT INTO list_exception (server,ip,exceptiontype,message) VALUES ('"+Server+"','"+IP+"','"+exception+"','"+e.getMessage()+"');");
	}
	
}

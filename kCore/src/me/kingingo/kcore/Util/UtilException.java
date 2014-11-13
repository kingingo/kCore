package me.kingingo.kcore.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import me.kingingo.kcore.MySQL.MySQL;

public class UtilException {
	
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

		Date MyDate = new Date();
		SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		df2.setTimeZone(TimeZone.getDefault());
		df2.format(MyDate);
		Calendar gc2 = new GregorianCalendar();
		Date now = gc2.getTime();
		
		sql.Update("CREATE TABLE IF NOT EXISTS list_exception(server varchar(30),ip varchar(30),time varchar(30),exceptiontype varchar(30),message varchar(200))");
		sql.Update("INSERT INTO list_exception (server,ip,time,exceptiontype,message) VALUES ('"+Server+"','"+IP+"','"+df2.format(now)+"','"+exception+"','"+e.getMessage()+"');");
	}
	
}

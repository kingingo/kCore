package me.kingingo.kcore.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

public class Calendar {

	public static boolean isWeakend(){
		SimpleDateFormat ft = new SimpleDateFormat("E");
		String day = ft.format(new Date());
		switch(day){
		case "Friday":return true;
		case "Saturday":return true;
		case "Sunday":return true;
		}
		return false;
	}
	
	public static boolean isInTime(int days,CalendarType type){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("MM");
	     if(Integer.valueOf(ft.format(dNow))==type.month){
	    	 ft = new SimpleDateFormat ("dd");
		     if( (Integer.valueOf(ft.format(dNow))+days) >= type.day && (Integer.valueOf(ft.format(dNow))-days) <= type.day ){
		    	 return true;
		     }
	     }
		return false;
	}
	
	public static CalendarType getHoliday(int days){
	     for(CalendarType typ : CalendarType.values()){
	    	 if(isInTime(days,typ)){
	    		 return typ;
	    	 }
	     }
	     return null;
	}
	
	public static CalendarType getHoliday(){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
	     for(CalendarType typ : CalendarType.values()){
	    	 if(typ.getDate().equalsIgnoreCase(ft.format(dNow))){
	    		 return typ;
	    	 }
	     }
	     return null;
	}
	
	public static boolean isHoliday(){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
	     for(CalendarType typ : CalendarType.values()){
	    	 if(typ.getDate().equalsIgnoreCase(ft.format(dNow))){
	    		 return true;
	    	 }
	     }
	     return false;
	}
	
	public enum CalendarType {
		GEBURSTAG("06.11",6,11),
		WEIHNACHTEN("24.12",24,12),
		NIKOLAUS("06.12",6,12),
		OSTERN("05.04",5,4),
		SILVESTER("01.01",1,1),
		HELLOWEEN("31.10",31,10);
		@Getter
		String date;
		int day;
		int month;
		private CalendarType(String date,int day,int month){
			this.date=date;
			this.day=day;
			this.month=month;
		}	
	}
	
}

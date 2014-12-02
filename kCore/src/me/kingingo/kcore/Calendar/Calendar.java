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
	
	public static boolean FromToTime(String FromDate,String ToDate){
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("MM");
	    if(FromDate.substring(3).equalsIgnoreCase(ft.format(dNow)) && ToDate.substring(3).equalsIgnoreCase(ft.format(dNow))){
	    	ft=new SimpleDateFormat("dd");
	    	if(Integer.valueOf(ft.format(dNow)) >= Integer.valueOf(FromDate.substring(0,2)) && Integer.valueOf(ft.format(dNow)) <= Integer.valueOf(ToDate.substring(0,2))){
	    		return true;
	    	}
	    }
	    
	    return false;
	}
	
	public static boolean isInTime(int days,CalendarType type){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("MM");
	     
	     if(Integer.valueOf(ft.format(dNow))==type.month/*||(b&&(Integer.valueOf(ft.format(dNow))-1)==(type.month))*/){
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
		for(CalendarType typ : CalendarType.values()){
	    	 if(isInTime(typ.timeSpan,typ)){
	    		 return typ;
	    	 }
	     }
		return null;
	}
	
	public static CalendarType TodayHoliday(){
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
		HITLER("20.04",20,4,1),
		GEBURSTAG("06.11",6,11,3),
		WEIHNACHTEN("24.12",24,12,24),
		NIKOLAUS("06.12",6,12,2),
		OSTERN("05.04",5,4,3),
		SILVESTER("01.01",1,1,2),
		HELLOWEEN("31.10",31,10,4);
		@Getter
		String date;
		int day;
		int month;
		@Getter
		int timeSpan;
		private CalendarType(String date,int day,int month,int timeSpan){
			this.date=date;
			this.day=day;
			this.timeSpan=timeSpan;
			this.month=month;
		}	
	}
	
}

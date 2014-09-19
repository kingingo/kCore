package me.kingingo.kcore.Calender;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

public class Calender {

	public boolean isWeakend(){
		SimpleDateFormat ft = new SimpleDateFormat("E");
		String day = ft.format(new Date());
		switch(day){
		case "Friday":return true;
		case "Saturday":return true;
		case "Sunday":return true;
		}
		return false;
	}
	
	public CalenderTyp getHoliday(){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
	     for(CalenderTyp typ : CalenderTyp.values()){
	    	 if(typ.getDate().equalsIgnoreCase(ft.format(dNow))){
	    		 return typ;
	    	 }
	     }
	     return null;
	}
	
	public boolean isHoliday(){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
	     for(CalenderTyp typ : CalenderTyp.values()){
	    	 if(typ.getDate().equalsIgnoreCase(ft.format(dNow))){
	    		 return true;
	    	 }
	     }
	     return false;
	}
	
	public enum CalenderTyp {
		GEBURSTAG("06.11"),
		WEIHNACHTEN("24.12"),
		NIKOLAUS("06.12"),
		OSTERN("05.04"),
		SILVESTER("01.01");
		@Getter
		String date;
		private CalenderTyp(String date){
			this.date=date;
		}	
	}
	
}

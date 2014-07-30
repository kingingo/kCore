package me.kingingo.kcore.Calender;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calender {

	public CalenderTyp isHoliday(){
		 Date dNow = new Date( );
	     SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
	     for(CalenderTyp typ : CalenderTyp.values()){
	    	 if(typ.getDate().equalsIgnoreCase(ft.format(dNow))){
	    		 return typ;
	    	 }
	     }
	     return null;
	}
	
}

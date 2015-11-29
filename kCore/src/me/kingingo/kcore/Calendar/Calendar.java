package me.kingingo.kcore.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilTime;

public class Calendar {
	
	public static CalendarType holiday;

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
		Date fix = toDate(type.getFixDate());
		Date dNow = new Date();
		return ((dNow.getTime()+(days*TimeSpan.DAY)) >= fix.getTime() && (dNow.getTime()-(days*TimeSpan.DAY)) <= fix.getTime());
	}
	
	public static Date toDate(String d){
		try {
			Date date = new SimpleDateFormat("dd.MM").parse(d);
			date.setYear(new Date().getYear());
			date=UtilTime.getEndOfDay(date);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date fromDate(String d){
		try {
			Date date = new SimpleDateFormat("dd.MM").parse(d);
			date.setYear(new Date().getYear());
			date=UtilTime.getStartOfDay(date);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean FromToTime(String FromDate,String ToDate){
		Date date = new Date();
		if( Integer.valueOf(new SimpleDateFormat("MM").format(toDate(FromDate)))==12 && Integer.valueOf(new SimpleDateFormat("MM").format(toDate(ToDate)))==01 ){
			Date to = toDate(ToDate);
			to.setYear(to.getYear()+1);
			return (date.getTime() >= fromDate(FromDate).getTime() && date.getTime() <= to.getTime());
		}
		return (date.getTime() >= fromDate(FromDate).getTime() && date.getTime() <= toDate(ToDate).getTime());
	}
	
	public static boolean isFixHolidayDate(CalendarType type){
		if(new SimpleDateFormat ("dd.MM").format(new Date( )).equalsIgnoreCase(type.getFixDate())){
			return true;
		}
		return false;
	}
	
	public static CalendarType getFixHoliday(){
		String date = new SimpleDateFormat ("dd.MM").format(new Date( ));
		for(CalendarType type : CalendarType.values()){
			if(date.equalsIgnoreCase(type.getFixDate())){
				return type;
			}
		}
		return null;
	}
	
	public static boolean isHoliday(CalendarType type){
		if(FromToTime(type.getFromDate(), type.getToDate())){
			return true;
		}
		return false;
	}
	
	public static CalendarType getHoliday(){
		for(CalendarType type : CalendarType.values()){
			if(FromToTime(type.getFromDate(), type.getToDate())){
				holiday=type;
				return type;
			}
		}
		return null;
	}
	
	public static CalendarType[] getHolidayAll(){
		CalendarType[] types = new CalendarType[CalendarType.values().length];
		int i =0;
		for(CalendarType type : CalendarType.values()){
			if(FromToTime(type.getFromDate(), type.getToDate())){
				types[i]=type;
				i++;
			}
		}
		return types;
	}
	
	public enum CalendarType {
		GEBURSTAG_MANUEL("24.02","26.02", "25.02"),
		GEBURSTAG("05.11","07.11", "06.11"),
		WEIHNACHTEN("28.11","31.12","24.12"),
		NIKOLAUS("05.12","07.12","06.12"),
		OSTERN("03.04","07.04","05.04"),
		SILVESTER("31.12","02.01","01.01"),
		HALLOWEEN("21.10","31.10","31.10");
		
		@Getter
		private String ToDate;
		@Getter
		private String FromDate;
		@Getter
		private String FixDate;
		
		private CalendarType(String FromDate,String ToDate,String FixDate){
			this.FromDate=FromDate;
			this.ToDate=ToDate;
			this.FixDate=FixDate;
		}
	}
	
}

package me.kingingo.kcore.Calender;

import lombok.Getter;

public enum CalenderTyp {
NONE("00.00"),
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

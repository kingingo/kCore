package eu.epicpvp.kcore.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.TimeManager.TimeManager;

public class UtilTime {
	private static TimeManager manager;
	public static final SimpleDateFormat DATE_FORMAT_NOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd");

	public static void setTimeManager(TimeManager timeManager) {
		manager=timeManager;
	}
	
	public static void setTimeManager(PermissionManager perm) {
		if (manager != null)
			return;
		manager = new TimeManager(perm);
	}

	public static TimeManager getTimeManager() {
		return manager;
	}

	public static Date nowDate() {
		return Calendar.getInstance().getTime();
	}

	public static String now() {
		Calendar calendar = Calendar.getInstance();
		return DATE_FORMAT_NOW.format(calendar.getTime());
	}

	public static String when(long time) {
		return DATE_FORMAT_NOW.format(UtilNumber.toLong(time));
	}

	public static String date() {
		Calendar calendar = Calendar.getInstance();
		return DATE_FORMAT_DAY.format(calendar.getTime());
	}

	public static Date getEndOfDay() {
		return getEndOfDay(nowDate());
	}

	public static Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date getStartOfDay() {
		return getStartOfDay(nowDate());
	}

	public static Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String since(long epoch) {
		return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
	}

	public static String formatMili(long milis) {
		if (milis > TimeSpan.MINUTE) {
			if (milis > TimeSpan.HOUR) {
				if (milis > TimeSpan.DAY) {
					int time = (int) (milis / TimeSpan.DAY);
					if (milis - time * TimeSpan.DAY > 1) {
						return time + " days " + formatMili(milis - time * TimeSpan.DAY);
					}
					return time + " day";
				}

				int time = (int) (milis / TimeSpan.HOUR);
				if (milis - time * TimeSpan.HOUR > 1) {
					return time + "h " + formatMili(milis - time * TimeSpan.HOUR);
				}
				return time + "h";
			}

			int time = (int) (milis / TimeSpan.MINUTE);
			if (milis - time * TimeSpan.MINUTE > 1) {
				return time + "min " + formatMili(milis - time * TimeSpan.MINUTE);
			}
			return time + "min";
		}

		return (int) (milis / TimeSpan.SECOND) + "sec";
	}

	public static double convert(long time, int trim, TimeUnit type) {
		if (type == TimeUnit.FIT) {
			if (time < 60000L)
				type = TimeUnit.SECONDS;
			else if (time < 3600000L)
				type = TimeUnit.MINUTES;
			else if (time < 86400000L)
				type = TimeUnit.HOURS;
			else {
				type = TimeUnit.DAYS;
			}
		}
		if (type == TimeUnit.DAYS)
			return UtilMath.trim(trim, time / 86400000.0D);
		if (type == TimeUnit.HOURS)
			return UtilMath.trim(trim, time / 3600000.0D);
		if (type == TimeUnit.MINUTES)
			return UtilMath.trim(trim, time / 60000.0D);
		if (type == TimeUnit.SECONDS)
			return UtilMath.trim(trim, time / 1000.0D);
		return UtilMath.trim(trim, time);
	}

	public static String MakeStr(long time) {
		return convertString(time, 1, TimeUnit.FIT);
	}

	public static String MakeStr(long time, int trim) {
		return convertString(time, trim, TimeUnit.FIT);
	}

	public static String formatSeconds(int milis) {
		if (milis > 60) {
			if (milis > 3600) {
				int time = (int) (milis / 3600);
				if (milis - time * 3600 > 1) {
					return time + "h " + formatSeconds(milis - time * 3600);
				}
				return time + "h";
			}

			int time = (int) (milis / 60);
			if (milis - time * 60 > 1) {
				return time + "min " + formatSeconds(milis - time * 60);
			}
			return time + "min";
		}

		return milis + "sec";
	}

	public static String convertString(long time, int trim, TimeUnit type) {
		if (time == -1L)
			return "Permanent";

		if (type == TimeUnit.FIT) {
			if (time < 60000L)
				type = TimeUnit.SECONDS;
			else if (time < 3600000L)
				type = TimeUnit.MINUTES;
			else if (time < 86400000L)
				type = TimeUnit.HOURS;
			else {
				type = TimeUnit.DAYS;
			}
		}
		if (type == TimeUnit.DAYS)
			return UtilMath.trim(trim, time / 86400000.0D) + " Days";
		if (type == TimeUnit.HOURS)
			return UtilMath.trim(trim, time / 3600000.0D) + " Hours";
		if (type == TimeUnit.MINUTES)
			return UtilMath.trim(trim, time / 60000.0D) + " Minutes";
		if (type == TimeUnit.SECONDS)
			return UtilMath.trim(trim, time / 1000.0D) + " Seconds";
		return UtilMath.trim(trim, time) + " Milliseconds";
	}

	public static boolean elapsed(long from, long required) {
		return System.currentTimeMillis() - from > required;
	}

	public static enum TimeUnit {
		FIT, DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS;
	}
}

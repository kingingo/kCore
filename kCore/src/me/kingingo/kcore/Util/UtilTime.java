package me.kingingo.kcore.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.plugin.java.JavaPlugin;

import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.TimeManager.TimeManager;

public class UtilTime
{
  public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
  private static TimeManager manager;

  public static void setTimeManager(PermissionManager perm){
	  if(manager!=null)return;
	  manager=new TimeManager(perm);
  }
  
  public static TimeManager getTimeManager(){
	  return manager;
  }
  
  public static String now(){
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(cal.getTime());
  }

  public static String when(long time)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(Long.valueOf(time));
  }

  public static String date()
  {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(cal.getTime());
  }

  public static String since(long epoch)
  {
    return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
  }

  public static String formatMili(long milis) {
		if (milis > TimeSpan.MINUTE) {
			if (milis > TimeSpan.HOUR) {
				int time = (int) (milis / TimeSpan.HOUR);
				if (milis - time * TimeSpan.HOUR > 1) {
					return time + "h "
							+ formatMili(milis - time * TimeSpan.HOUR);
				}
				return time + "h";
			}

			int time = (int) (milis / TimeSpan.MINUTE);
			if (milis - time * TimeSpan.MINUTE > 1) {
				return time + "min "
						+ formatMili(milis - time * TimeSpan.MINUTE);
			}
			return time + "min";
		}

	return (int) (milis / TimeSpan.SECOND) + "sec";
  }
  
  public static double convert(long time, int trim, TimeUnit type)
  {
    if (type == TimeUnit.FIT)
    {
      if (time < 60000L) type = TimeUnit.SECONDS;
      else if (time < 3600000L) type = TimeUnit.MINUTES;
      else if (time < 86400000L) type = TimeUnit.HOURS; else {
        type = TimeUnit.DAYS;
      }
    }
    if (type == TimeUnit.DAYS) return UtilMath.trim(trim, time / 86400000.0D);
    if (type == TimeUnit.HOURS) return UtilMath.trim(trim, time / 3600000.0D);
    if (type == TimeUnit.MINUTES) return UtilMath.trim(trim, time / 60000.0D);
    if (type == TimeUnit.SECONDS) return UtilMath.trim(trim, time / 1000.0D);
    return UtilMath.trim(trim, time);
  }

  public static String MakeStr(long time)
  {
    return convertString(time, 1, TimeUnit.FIT);
  }

  public static String MakeStr(long time, int trim)
  {
    return convertString(time, trim, TimeUnit.FIT);
  }
  
  public static String formatSeconds(int milis) {
			if (milis > 60) {
				if (milis > 3600) {
					int time = (int) (milis / 3600);
					if (milis - time * 3600 > 1) {
						return time + "h "
								+ formatSeconds(milis - time * 3600);
					}
					return time + "h";
				}

				int time = (int) (milis / 60);
				if (milis - time * 60 > 1) {
					return time + "min "
							+ formatSeconds(milis - time * 60);
				}
				return time + "min";
			}

		return milis + "sec";
  }
  
  public static String convertString(long time, int trim, TimeUnit type)
  {
    if (time == -1L) return "Permanent";

    if (type == TimeUnit.FIT)
    {
      if (time < 60000L) type = TimeUnit.SECONDS;
      else if (time < 3600000L) type = TimeUnit.MINUTES;
      else if (time < 86400000L) type = TimeUnit.HOURS; else {
        type = TimeUnit.DAYS;
      }
    }
    if (type == TimeUnit.DAYS) return UtilMath.trim(trim, time / 86400000.0D) + " Days";
    if (type == TimeUnit.HOURS) return UtilMath.trim(trim, time / 3600000.0D) + " Hours";
    if (type == TimeUnit.MINUTES) return UtilMath.trim(trim, time / 60000.0D) + " Minutes";
    if (type == TimeUnit.SECONDS) return UtilMath.trim(trim, time / 1000.0D) + " Seconds";
    return UtilMath.trim(trim, time) + " Milliseconds";
  }

  public static boolean elapsed(long from, long required)
  {
    return System.currentTimeMillis() - from > required;
  }

  public static enum TimeUnit
  {
    FIT, 
    DAYS, 
    HOURS, 
    MINUTES, 
    SECONDS, 
    MILLISECONDS;
  }
}

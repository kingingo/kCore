package me.kingingo.kcore.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class UtilProxy {

	public static boolean checkS(String ip)
	  {
	    try
	    {
	      URL url = new URL("http://www.shroomery.org/ythan/proxycheck.php?ip=" + ip);
	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	      String line = in.readLine();
	      in.close();
	      if (line.equals("Y"))
	      {
	        return true;
	      }
	      return false;
	    }
	    catch (Exception e) {
	      Logger.getLogger("CheckVPN").warning("Error checking Shroomery, assuming clean.");
	    }
	    return false;
	  }

	public static boolean checkGIP(String ip) {
	    try {
	      URL url = new URL("http://check.getipaddr.net/check.php?ip=" + ip);
	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	      String line = in.readLine();
	      in.close();
	      if (line.equals("1"))
	      {
	        return true;
	      }
	    } catch (Exception e) { Logger.getLogger("CheckVPN").warning("Error checking getipaddr, assuming clean."); }

	    return false;
	  }

	public static boolean checkYASB(String ip) {
	    try {
	      URL url = new URL("http://yasb.intuxication.org/api/check.xml?ip=" + ip);
	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	      String line = in.readLine();
	        if (line.toLowerCase().contains("true"))
	          return true;
	      in.close();
	    } catch (Exception e) {
	      Logger.getLogger("CheckVPN").warning("Error checking Yet Another Spam Blacklist, assuming clean.");
	    }
	    return false;
	  }

	public static boolean checkSFS(String ip) {
	    try {
	      URL url = new URL("http://api.stopforumspam.org/api?ip=" + ip);
	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	      String line = in.readLine();
	        if (line.toLowerCase().contains("yes"))
	          return true;
	      in.close();
	    } catch (Exception e) {
	      Logger.getLogger("CheckVPN").warning("Error checking StopForumSpam, assuming clean.");
	    }
	    return false;
	  }

	public static boolean checkWU(String ip)
	  {
	    try {
	      URL url = new URL("http://winmxunlimited.net/api/proxydetection/v1/query/?ip=" + ip);
	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	      String line = in.readLine();
	      if (!line.equals("0"))
	        return true;
	    } catch (Exception e) {
	      Logger.getLogger("CheckVPN").warning("Error checking winmxunlimited.net, assuming clean.");
	    }
	    return false;
	  }
	
}

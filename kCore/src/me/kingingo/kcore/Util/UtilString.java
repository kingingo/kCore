package me.kingingo.kcore.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString
{
  private static List<String> badWords = new ArrayList<>();
  static Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,_ ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,_ ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,_ ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
  static Pattern webPattern = Pattern.compile("(http://)|(https://)?(www)?\\S{2,}((\\.com)|(\\.ru)|(\\.net)|(\\.org)|(\\.minecraft\\.to)|(\\.co\\.uk)|(\\.me)|(\\.tk)|(\\.info)|(\\.es)|(\\.de)|(\\.arpa)|(\\.edu)|(\\.firm)|(\\.int)|(\\.mil)|(\\.mobi)|(\\.nato)|(\\.to)|(\\.fr)|(\\.ms)|(\\.vu)|(\\.eu)|(\\.nl)|(\\.us)|(\\.dk))");

  public static List<String> stringArrayToList(String[] arg1) {
    List<String> toreturn = new ArrayList<>();
    String[] arrayOfString = arg1; int j = arg1.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
      toreturn.add(s);
    }
    return toreturn;
  }

  public static String listToString(List<String> list, String seperator) {
    String toreturn = "";
    for (String s : list) {
      if (toreturn.equalsIgnoreCase("")) toreturn = s; else
        toreturn = toreturn + seperator + s;
    }
    return toreturn;
  }

  public static boolean isBadWord(String s) {
    if (badWords == null) setupBadWords(new String[0]);
    for (String words : badWords) {
      if (containsIgnoreCase(words, s)) {
        return true;
      }
    }
    return false;
  }

  public static void setupBadWords(String[] additions) {
    String[] arrayOfString = additions; int j = additions.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i]; badWords.add(s); }
    badWords.add("Gay"); badWords.add("schwul"); badWords.add("pussy");
    badWords.add("Arschloch"); badWords.add("Wixxer"); badWords.add("fick");
    badWords.add("Arschgesicht"); badWords.add("scheisse"); badWords.add("scheiße");
    badWords.add("wixer"); badWords.add("Homo"); badWords.add("Spasst"); badWords.add("Spassti");
    badWords.add("Mongo"); badWords.add("fuck"); badWords.add("Hurensohn"); badWords.add("hundesohn");
    badWords.add("nutte"); badWords.add("behindert"); badWords.add("hässlig"); badWords.add("schlampe");
    badWords.add("hässlich"); badWords.add("Hure"); badWords.add("hure");
  }

  public static boolean containsIgnoreCase(String string, String in) {
    return in.toLowerCase().contains(string.toLowerCase());
  }

  public static boolean arrayContainsString(String[] array, String string, boolean ignoreCase) {
    String[] arrayOfString = array; int j = array.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
      if (((s.equalsIgnoreCase(string)) && (ignoreCase)) || ((s.equals(string)) && (!ignoreCase))) return true;
    }
    return false;
  }

  public static int countLines(String text, int charsPerLine) {
    int toreturn = 0;
    int lastLength = 0;
    for (int i = 0; i < text.length(); i++) {
      if ((text.charAt(i) == '/') && (text.charAt(i + 1) == 'n')) {
        toreturn++;
        i++;
      }
      else {
        lastLength++;
        if (lastLength >= charsPerLine) {
          lastLength = 0;
          toreturn++;
        }
      }
    }
    return toreturn;
  }

  public static String[] getLines(String text, int charsPerLine) {
    String[] toreturn = new String[countLines(text, charsPerLine)];
    String last = "";
    int lastLength = 0;
    int i2 = 0;
    for (int i = 0; i < text.length(); i++) {
      if ((text.charAt(i) == '\\') && (text.charAt(i + 1) == 'n')) {
        toreturn[i2] = "\n";
        i2++;
        i++;
      }
      else {
        lastLength++;
        last = last + text.charAt(i);
        if (lastLength >= charsPerLine) {
          toreturn[i2] = last;
          i2++;
          lastLength = 0;
          last = "";
        }
      }
    }
    return toreturn;
  }

  public static boolean checkForIP(String text) {
    for (String s : text.split(" ")) {
      Matcher searchforips = ipPattern.matcher(s.toLowerCase());
      Matcher searchforweb = webPattern.matcher(s.toLowerCase());
      if ((searchforips.find()) || (searchforweb.find())) return true;
    }
    return false;
  }

  public static int countNumbers(String s)
  {
    int count = 0;
    for (char c : s.toCharArray()) {
    	try{
    		Integer i = Integer.valueOf(c);
    		count++;
    	}catch(NumberFormatException e){
    		
    	}
    }
    return count;
  }
}
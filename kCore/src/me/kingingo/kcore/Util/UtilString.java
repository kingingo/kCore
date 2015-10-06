package me.kingingo.kcore.Util;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;

public class UtilString
{
  private static List<String> badWords = new ArrayList<>();
  static Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,_ ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,_ ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,_ ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
  static Pattern webPattern = Pattern.compile("(http://)|(https://)?(www)?\\S{2,}((\\.com)|(\\.ru)|(\\.net)|(\\.org)|(\\.minecraft\\.to)|(\\.co\\.uk)|(\\.me)|(\\.tk)|(\\.info)|(\\.es)|(\\.de)|(\\.arpa)|(\\.edu)|(\\.firm)|(\\.int)|(\\.mil)|(\\.mobi)|(\\.nato)|(\\.to)|(\\.fr)|(\\.ms)|(\\.vu)|(\\.eu)|(\\.nl)|(\\.us)|(\\.dk))");
  static Pattern mcPattern = Pattern.compile("(eu)|(me)|(de)|(to)|(ru)|(net)|(tv)|(info)|(com)");
  
  public static boolean isNormalCharakter(String msg){
	  if(msg.matches("[a-zA-Z0-9_]*")){
		  return true;
	  }
	  return false;
  }
  
    public static BufferedImage stringToBufferedImage(Font font, String s){
      BufferedImage img = new BufferedImage(1, 1, 6);
      Graphics g = img.getGraphics();
      g.setFont(font);

      FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
      Rectangle2D rect = font.getStringBounds(s, frc);
      g.dispose();

      img = new BufferedImage((int)Math.ceil(rect.getWidth()), (int)Math.ceil(rect.getHeight()), 6);
      g = img.getGraphics();
      g.setColor(Color.black);
      g.setFont(font);

      FontMetrics fm = g.getFontMetrics();
      int x = 0;
      int y = fm.getAscent();

      g.drawString(s, x, y);
      g.dispose();

      return img;
    }
    
  public static String corect(String t){
	  return (t.endsWith("§") ? t.substring(0, t.length()-1) : t );
  }
    
  public static String cut(String t,int length){
	  if(t.length()>length){
		  return corect(t.substring(0,length-1));
	  }else{
		  return t;
	  }
  }
  
  public static String cut(String t){
	  return cut(t,16);
  }
  
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
	//if(additions==null)additions=new String[]{"scheiss","scheiiss","scheiis","scheis","spa$t","sp4$t","sp4st","spast","ez","e2","3z","eezz","eeezzz","eeeezzzz","eeeeezzzzz","eez","eeez","eeeez","ezz","ezzz","ezzzz","ezzzzz","ezzzzzz","n4p","noob","nob","nooob","n00b","n0b","nuub","nub","get","g3t","rekt","r3kt","wrecked","wr3cked","wr3ckd","wreckd","reckt","r3ckt","wr3ekt","wr3kt","schlecht","schlechter","scheiße","e²","3²","pussy","pusy","pu$$y","pu$y","opfer","cheap","learntoplay","l3arntoplay","learntopl4y","l3arntopl4y","learntoply","lerntoplay","l3rntoplay","lerntopl4y","faggot","faggit","fagot","fagit","fagitt","faggitt","f4ggot","f4git","f4gg0t","fgt","0pfer","verfickt","fickt","schisser","schißer","schiesser","schießer","kack","juden","amk","remoV","Wavefrt","erection","wvvvvv","aasgeier","abspritzer","sdfds","ackerfresse","affenarsch","affenhirn","affenkotze","afterlecker","aktivex.info","almosenarsch","am-sperma-riecher","anal*","g4y","lernspielen","lernespielen","analadmiral","analbesamer","analbohrer","analdrill","analentjungferer","analerotiker","analfetischist","analförster","anal-frosch","analnegerdildo","analratte","analritter","aok-chopper","armleuchter","arsch","arschaufreißer","arschbackenschänder","arschbesamer","ärsche","arschentjungferer","arschficker","arschgeburt","arschgeficktegummifotze","arschgeige","arschgesicht","arschhaarfetischist","arschhaarrasierer","arschhöhlenforscher","arschkrampe","arschkratzer","arschlecker","arschloch","arschlöcher","arschmade","arschratte","arschzapfen","arsebandit","arsehole","arsejockey","arselicker","arsenuts","arsewipe","assel","assfuck","assfucking","assgrabber","asshol","asshole","asshole","assi","assrammer","assreamer","asswipe","astlochficker","auspufflutscher","badmotherfucker","badass","badenutte","bananenstecker","bastard","bastard","b4stard","bast4rd","bauernschlampe","beatingthemeat","beefcurtains","beefflaps","behindis","bekloppter","muttergeficktes","beklopter","bettnässer","******er","******er","bettpisser","bettspaltenficker","biatch","bimbo","bitch","bitches","bitchnutte","bitsch","bizzach","blechfotze","blödmann","blogspoint","blowjob","bohnenfresser","boob","boobes","boobie","boobies","boobs","booby","boylove","breasts","brechfurz","bückfleisch","bückstück","bückvieh","buggery","bullensohn","bullshit","bummsen","bumsen","bumsklumpen","buschnutte","busty","buttpirate","buttfuc","buttfuck","buttfucker","buttfucking","carpetmuncher","carpetmunchers","carpetlicker","carpetlickers","chausohn","clitsuck","clitsucker","clitsucking","cock","cocksucker","cockpouch","cracka","crap","craper","crapers","crapping","craps","cunt","cunt","cunts","dachlattengesicht","dackelficker","dickhead","dicklicker","diplomarschloch","doofi","douglette","drecksack","drecksau","dreckschlitz","dreckschüppengesicht","drecksfotze","drecksmösendagmar","drecksnigger","drecksnutte","dreckspack","dreckstürke","dreckvotze","dumbo","dummschwätzer","dumpfbacke","dünnpfifftrinker","eichellecker","eierkopf","eierlutscher","eiswürfelpisser","ejaculate","entenfisterer","epilepi","epilepis","epileppis","fagette","fagitt","fäkalerotiker","faltenficker","fatass","ferkelficker","ferkel-ficker","fettarsch","fettsack","fettsau","feuchtwichser","fick","fick*","fickarsch","fickdreck","ficken","ficker","fickfehler","fickfetzen","fickfresse","fickfrosch","fickfucker","fickgelegenheit","fickgesicht","fickmatratze","ficknudel","ficksau","fickschlitz","fickschnitte","fickschnitzel","fingerfuck","fingerfucking","fisch-stinkenderhodenfresser","fistfuck","fistfucking","flachtitte","flussfotze","fotze","fotzenforscher","fotzenfresse","fotzenknecht","fotzenkruste","fotzenkuchen","fotzenlecker","fotzenlöckchen","fotzenpisser","fotzenschmuser","fotzhobel","frisösenficker","frisösenfotze","fritzfink","froschfotze","froschfotzenficker","froschfotzenleder","****","fucked","fucker","fucker","fucking","fuckup","fudgepacker","futtgesicht","gaylord","geilriemen","gesichtsfotze","göring","großmaul","gummifotzenficker","gummipuppenbumser","gummisklave","hackfresse","hafensau","hartgeldhure","heilhitler","hihoper","hinterlader","hirni","hitler","hodenbeißer","hodensohn","homo","hosenpisser","hosenscheißer","hühnerficker","huhrensohn","hundeficker","hundesohn","hurenlecker","hurenpeter","hurensohn","hurentocher","idiot","idioten","itakker","ittaker","jackoff","jackass","jackshit","jerkoff","jizz","judensau","kackarsch","kacke","kacken","kackfass","kackfresse","kacknoob","kaktusficker","kanacke","kanake","kanaken","kanaldeckelbefruchter","kartoffelficker","kinderficken","kinderficker","kinderporno","kitzlerfresser","klapposkop","klolecker","klötenlutscher","knoblauchfresser","konzentrationslager","kotgeburt","kotnascher","kümmeltürke","kümmeltürken","lackaffe","lebensunwert","lesbian","lurchi","lustbolzen","lutscher","magerschwanz","manwhore","masturbate","meatpuppet","missgeburt","mißgeburt","mistsau","miststück","mitternachtsficker","mohrenkopf","mokkastübchenveredler","mongo","möse","mösenficker","mösenlecker","mösenputzer","möter","motherfucker","motherfucking","motherfucker","muschilecker","muschischlitz","mutterficker","nazi","nazis","neger","nigga","nigger","niggerlover","niggers","niggerschlampe","nignog","nippelsauger","nutte","nuttensohn","nuttenstecher","nuttentochter","ochsenpimmel","ölauge","oralsex","penislicker","penislicking","penissucker","penissucking","penis","peniskopf","penislecker","penislutscher","penissalat","penner","pferdearsch","phentermine","pimmel","pimmelkopf","pimmellutscher","pimmelpirat","pimmelprinz","pimmelschimmel","pimmelvinni","pindick","pissoff","piss","pissbirne","pissbotte","pisse","pisser","pissetrinker","pissfisch","pissflitsche","pissnelke","polacke","polacken","poop","popellfresser","popostecker","popunterlage","porn","porno","pornografie","pornoprengel","pottsau","prärieficker","prick","quiff","randsteinwichser","rasiertevotzen","rimjob","rindsriemen","ritzenfummler","rollbrooden","rosetenputzer","rosetenschlemmer","rosettenhengst","rosettenkönig","rosettenlecker","rosettentester","sackfalter","sackgesicht","sacklutscher","sackratte","saftarsch","sakfalter","schamhaarlecker","schamhaarschädel","schandmaul","scheisse","scheisser","scheissgesicht","scheisshaufen","scheißhaufen","schlammfotze","schlampe","schleimmöse","schlitzpisser","schmalspurficker","schmeue","schmuckbert","schnuddelfresser","schnurbeltatz","schrumpelfotze","schwanzlurch","schwanzlutscher","schweinepriester","schweineschwanzlutscher","schwuchtel","schwutte","sex","shiter","shiting","shitlist","shitomatic","shits","shitty","shlong","shutthefuckup","siegheil","sitzpisser","skullfuck","skullfucker","skullfucking","slut","smegmafresser","spack","spacko","spaghettifresser","spastard","spasti","spastis","spermafresse","spermarutsche","spritzer","stinkschlitz","stricher","suckmycock","suckmydick","threesome","tittenficker","tittenspritzer","titties","titty","tunte","untermensch","vagina","vergasen","viagra","volldepp","volldeppen","vollhorst","vollidiot","vollpfosten","vollspack","vollspacken","vollspasti","vorhaut","votze","votzenkopf","wanker","wankers","weichei","whoar","whore","wichsbart","wichsbirne","wichser","wichsfrosch","wichsgriffel","wichsvorlage","wickspickel","wixa","wixen","wixer","wixxer","wixxxer","wixxxxer","wurstsemmelfresser","yankee","zappler","zyclonb","zyklonb","xxx","hitler","bastart"};
    if(additions!=null)for (int i = 0; i < additions.length; i++) { badWords.add(additions[i]); }
    badWords.add("Gay"); badWords.add("schwul"); badWords.add("pussy");
    badWords.add("Arschloch"); badWords.add("Wixxer"); badWords.add("fick");
    badWords.add("Arschgesicht"); badWords.add("scheisse"); badWords.add("scheiße");
    badWords.add("wixer"); badWords.add("Homo"); badWords.add("Spasst"); badWords.add("Spassti");
    badWords.add("Mongo"); badWords.add("fuck"); badWords.add("Hurensohn"); badWords.add("hundesohn");
    badWords.add("nutte"); badWords.add("behindert"); badWords.add("hässlig"); badWords.add("schlampe");
    badWords.add("hässlich"); badWords.add("Hure"); badWords.add("hure");
    badWords.add(".eu");badWords.add(".de");badWords.add("me");badWords.add(".to");badWords.add(".minecraft.to");
    badWords.add(".tv");badWords.add(".com");badWords.add(".tk");badWords.add(".net");
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
  
  public static String generateCaptchaString(int length) {
	 return RandomStringUtils.random(length, true, true);
  }
  
  public static String toUpperCase(String g){
	  return g.substring(0, 1).toUpperCase()+g.substring(1,g.length());
  }

  public static boolean checkForIP(String text) {
    for (String s : text.split(" ")) {
      Matcher searchforips = ipPattern.matcher(s.toLowerCase());
      Matcher searchforweb = webPattern.matcher(s.toLowerCase());
     // Matcher searchformc = mcPattern.matcher(s.toLowerCase());
      if ((searchforips.find()) || (searchforweb.find()) /*|| (searchformc.find())*/) return true;
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
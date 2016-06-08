package eu.epicpvp.kcore.Listener.Chat.filter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import eu.epicpvp.kcore.Enum.Zeichen;
import org.apache.commons.lang3.StringUtils;

public class ChatUtils {

	private static final Pattern LAG_PATTERN = Pattern.compile("\\b(?:[1l]+[a4]+g+)\\b");
	private static final Set<Pattern> insultPatterns = new LinkedHashSet<>();

	private static final Pattern DOTS_PATTERN = Pattern.compile("(?:punkt|dot|point|,|;|\\(\\.\\)|\\.)+"); // detects (.)
	private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
	private static final Pattern URL_PATTERN = Pattern.compile("(?:https?://)?(?:[-\\w_\\.]{2,}\\.)?([-\\w_]{2,}\\.[a-z]{2,4})(?:/\\S*)?");

	private static final String BRACKETS = "\\(\\)\\[\\]\\{\\}"; //as possible replacement for o or similar like (), [], {}
	private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

	static {
		insultPatterns.add(Pattern.compile("n[oöuüv0" + BRACKETS + "]+[b8]")); //noob
		insultPatterns.add(Pattern.compile("sp+[aä4]+s+t")); //spast
		insultPatterns.add(Pattern.compile("[h][uüv]+r*[e3]*n*s+[oö0" + BRACKETS + "]+h*n?")); //hurensohn
		insultPatterns.add(Pattern.compile("[vw][i1]+[chsx]+(?:[e3]+r|[aä4])*")); //wichser
		insultPatterns.add(Pattern.compile("[aä4]r*s*c*h*[l1]+[oö0" + BRACKETS + "]+c*h?")); //arschloch
		insultPatterns.add(Pattern.compile("[aä4]r*s+c+h")); //arsch
	}

	public static boolean isSimilar(String msg1, String msg2, double requiredSimilarityPercent) {
		if (msg1.length() > 5 && msg2.length() > 5) {
			msg1 = msg1.toLowerCase();
			msg2 = msg2.toLowerCase();
			if (msg1.equals(msg2) || getSimilarityPercentage(msg1, msg2) >= requiredSimilarityPercent) {
				return true;
			}
		} else {
			if (msg2.equalsIgnoreCase(msg1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isCaps(String msg, double requiredCapsPercent) {
		double maxCapsChars = msg.length() * requiredCapsPercent;
		long capsChars = msg.chars().filter(Character::isUpperCase).count();

		if (capsChars >= requiredCapsPercent) {
			return true;
		}
		return false;
	}

	private static double getSimilarityPercentage(String msg1, String msg2) {
		double levenshteinDistance = (double) StringUtils.getLevenshteinDistance(msg1, msg2);
		double maxLevenshteinDistance = (double) Math.max(msg1.length(), msg2.length());
		return 1.0 - (levenshteinDistance / maxLevenshteinDistance);
	}

	public static boolean isLagMessage(String message) {
		if (LAG_PATTERN.matcher(message.toLowerCase()).find()) {
			return true;
		}
		return false;
	}

	public static String replaceSpecial(String msg) {
		msg = msg.replace("<3", Zeichen.BIG_HERZ.toString());
		return msg;
	}

	public static boolean isZeichenSpam(String msg, int maxSameCharsAllowed) {
		int amount = 0;
		char last = ' ';
		for (char c : msg.toCharArray()) {
			if (c == last) {
				amount++;
				if (amount > maxSameCharsAllowed) {
					return true;
				}
			} else {
				last = c;
			}
		}
		return false;
	}

	public static boolean isInsult(String msg) {
		msg = SPACE_PATTERN.matcher(msg.toLowerCase()).replaceAll("");
		for (Pattern pattern : insultPatterns) {
			if (pattern.matcher(msg).find()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInternetAddress(String msg) {
		msg = DOTS_PATTERN.matcher(msg).replaceAll(".");
		if (IP_PATTERN.matcher(msg).find()) {
			return true;
		}
		if (URL_PATTERN.matcher(msg).find()) {
			return true;
		}
		return false;
	}
}

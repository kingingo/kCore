package me.kingingo.kcore.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utils for casting number types to other number types
 */
public final class UtilNumber {
    private UtilNumber() {}

    static DecimalFormat twoDPlaces = new DecimalFormat("#,###.##");
    static DecimalFormat currencyFormat = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

    public static String formatDouble(double value)
    {
      twoDPlaces.setRoundingMode(RoundingMode.HALF_UP);
      return twoDPlaces.format(value);
    }

    public static String formatAsCurrency(BigDecimal value)
    {
      currencyFormat.setRoundingMode(RoundingMode.FLOOR);
      String str = currencyFormat.format(value);
      if (str.endsWith(".00"))
      {
        str = str.substring(0, str.length() - 3);
      }
      return str;
    }

    public static boolean isInt(String sInt)
    {
      try
      {
        Integer.parseInt(sInt);
      }
      catch (NumberFormatException e)
      {
        return false;
      }
      return true;
    }
    
    public static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(final double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int round(double num) {
        return floor(num + 0.5d);
    }

    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.valueOf(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    public static float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.valueOf(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.valueOf(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.valueOf(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    public static short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.valueOf(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }

    public static byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.valueOf(object.toString());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return 0;
    }
}

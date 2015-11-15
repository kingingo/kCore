package me.kingingo.kcore.Encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryption {

    private static SecureRandom rnd = new SecureRandom();

    private static String getMD5(String message) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        md5.reset();
        md5.update(message.getBytes());
        byte[] digest = md5.digest();

        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }

    private static String getSHA1(String message) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        sha1.update(message.getBytes());
        byte[] digest = sha1.digest();

        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }

    private static String getSHA256(String message) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        sha256.reset();
        sha256.update(message.getBytes());
        byte[] digest = sha256.digest();

        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }

    public static String getWhirlpool(String message) {
        byte[] digest = new byte[Whirlpool.DIGESTBYTES];
        Whirlpool.NESSIEinit();
        Whirlpool.NESSIEadd(message);
        Whirlpool.NESSIEfinalize(digest);
        return Whirlpool.display(digest);
    }

    private static String getSaltedHash(String message, String salt) throws NoSuchAlgorithmException {
        return "$SHA$" + salt + "$" + getSHA256(getSHA256(message) + salt);
    }

    private static String getXAuth(String message, String salt) {
        String hash = getWhirlpool(salt + message).toLowerCase();
        int saltPos = (message.length() >= hash.length() ? hash.length() - 1 : message.length());
        return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
    }

    private static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] msg = new byte[40];
        rnd.nextBytes(msg);

        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        byte[] digest = sha1.digest(msg);
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1,digest)).substring(0, length);
    }

    public static String getHash(HashAlgorithm alg, String password) throws NoSuchAlgorithmException {
        switch (alg) {
            case MD5:
                return getMD5(password);
            case SHA1:
                return getSHA1(password);
//            case SHA256:
//                String salt = createSalt(16);
//                return getSaltedHash(password, salt);
            case WHIRLPOOL:
                return getWhirlpool(password);
//            case XAUTH:
//                String xsalt = createSalt(12);
//                return getXAuth(password, xsalt);
            default:
                throw new NoSuchAlgorithmException("Unbekannter Hash Algorithmus");
        }
    }

    public static boolean comparePasswordWithHash(String password, String hash) throws NoSuchAlgorithmException {
        if (hash.length() == 32) {
            return hash.equals(getMD5(password));
        }

        if (hash.length() == 40) {
            return hash.equals(getSHA1(password));
        }

        if (hash.length() == 140) {
            int saltPos = (password.length() >= hash.length() ? hash.length() - 1 : password.length());
            String salt = hash.substring(saltPos, saltPos + 12);
            return hash.equals(getXAuth(password, salt));
        }

        if (hash.contains("$")) {
            String[] line = hash.split("\\$");
            if (line.length > 3 && line[1].equals("SHA")) {
                return hash.equals(getSaltedHash(password, line[2]));
            } else {
                return false;
            }
        }
        return false;
    }

    public enum HashAlgorithm {
        MD5, SHA1, WHIRLPOOL
//        SHA256, XAUTH
    }
}

package me.kingingo.kcore.Util;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Skin
{
  private String uuid;
  private UUID asUUID;
  private String name;
  private String value;
  private String signatur;
  private String playername;

  Skin(UUID uuid)
  {
    this.uuid = uuid.toString().replaceAll("-", "");
    this.asUUID = uuid;
    load();
  }

  Skin(String name, String value, String signature) {
    this.name = name;
    this.value = value;
    this.signatur = signature;
  }

  private void load()
  {
    try
    {
      OfflinePlayer offP = Bukkit.getServer().getOfflinePlayer(this.asUUID);
      this.playername = offP.getName();

      URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + this.uuid + "?unsigned=false");
      URLConnection uc = url.openConnection();
      uc.setUseCaches(false);
      uc.setDefaultUseCaches(false);
      uc.addRequestProperty("User-Agent", "Mozilla/5.0");
      uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
      uc.addRequestProperty("Pragma", "no-cache");

      String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(json);
      JSONArray properties = (JSONArray)((JSONObject)obj).get("properties");
      for (int i = 0; i < properties.size(); i++) {
        try {
          JSONObject property = (JSONObject)properties.get(i);
          String name = (String)property.get("name");
          String value = (String)property.get("value");
          String signature = property.containsKey("signature") ? (String)property.get("signature") : null;

          this.name = name;
          this.value = value;
          this.signatur = signature;
        }
        catch (Exception e)
        {
          Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
        }
      }
      uc.setConnectTimeout(0);
      uc.getInputStream().close();
    }
    catch (Exception localException1) {
    }
  }

  public String getSkinValue() {
    return this.value;
  }

  public String getSkinName() {
    return this.name;
  }

  public String getSkinSignatur() {
    return this.signatur;
  }

  public String getUUID() {
    return this.uuid;
  }

  public UUID getAsUUID() {
    return this.asUUID;
  }

  public String getPlayerName() {
    return this.playername;
  }
}
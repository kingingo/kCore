package eu.epicpvp.kcore.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.ChatColor;
public enum StatusChecker {
    ACCOUNTS("Accounts Service", "account.mojang.com"),
    AUTHENTICATION("Authentication Service", "auth.mojang.com"),
    AUTHENTICATION_SERVER("Authentication Server", "authserver.mojang.com"),
    LOGIN("Login Service", "login.minecraft.net"),
    SESSION_MINECRAFT("Minecraft Session Server", "session.minecraft.net"),
    SESSION_MOJANG("Mojang Session Server", "sessionserver.mojang.com"),
    SKINS("Skin Server", "skins.minecraft.net"),
    MAIN_WEBSITE("Main Site", "minecraft.net");
    
    private String name, serviceURL;
    private JsonParser jsonParser = new JsonParser();
    StatusChecker(String name, String serviceURL) {
        this.name = name;
        this.serviceURL = serviceURL;
    }
    public String getName() {
        return name;
    }
    /**
    * Check the current Mojang service for it's status, errors are ignored.
    *
    * @return Status of the service.
    */
    public Status getStatus() {
        return getStatus(true);
    }
    /**
    * Check the current Mojang service for it's status.
    *
    * @param suppressErrors - Don't print errors in console.
    * @return Status of the service.
    */
    public Status getStatus(boolean suppressErrors) {
            URL url;
            try {
                url = new URL("http://status.mojang.com/check?service=" + serviceURL);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                
                Object object = jsonParser.parse(bufferedReader);
                JsonObject jsonObject = (JsonObject) object;
    
                String status = jsonObject.get(serviceURL).getAsString();
                return Status.get(status);
            } catch (IOException e) {
                if(!suppressErrors)
                    e.printStackTrace();
               
                return Status.UNKNOWN;
               
            }
    }
    public enum Status {
        ONLINE("Online", ChatColor.GREEN.toString(), "No problems detected!"),
        UNSTABLE("Unstable", ChatColor.YELLOW.toString(), "May be experiencing issues..."),
        OFFLINE("Offline", ChatColor.DARK_RED.toString(), "Experiencing problems!"),
        UNKNOWN("Unknown", ChatColor.WHITE.toString(), "Couldn't connect to Mojang!");
        private String status, color, description;
        Status(String status, String color, String description) {
            this.status = status;
            this.color = color;
            this.description = description;
        }
        public String getStatus() {
            return status;
        }
        public String getColor() {
            return color;
        }
        public String getDescription() {
            return description;
        }
        public static Status get(String status) {
            status = status.toLowerCase();
            switch (status) {
            case "green":
                return Status.ONLINE;
            case "yellow":
                return Status.UNSTABLE;
            case "red":
                return Status.OFFLINE;
            default:
                return Status.UNKNOWN;
            }
        }
    }
}
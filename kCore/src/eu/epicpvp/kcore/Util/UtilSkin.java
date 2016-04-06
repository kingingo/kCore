package eu.epicpvp.kcore.Util;

import java.util.UUID;

import org.bukkit.entity.Player;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.ProgressFuture;
import dev.wolveringer.skin.Skin;
import dev.wolveringer.skin.SteveSkin;
import eu.epicpvp.kcore.PacketAPI.Packets.kGameProfile;

public class UtilSkin {
	
	public static void setSkinPlayer(Player player, Skin data){
		kGameProfile profile = (kGameProfile) UtilPlayer.getCraftPlayer(player).getProfile();
		profile.loadSkin(data);
	}
	
	public static void loadSkin(Callback<Skin> callback, String playerName){
		ProgressFuture<Skin> response = UtilServer.getClient().getSkin(playerName);
		response.getAsync(callback);
	}
	
	public static void loadSkin(kGameProfile profile, String playerName){
		loadSkin(new Callback<Skin>() {
			@Override
			public void call(Skin skin) {
				 profile.loadSkin(skin);
			}
		}, playerName);
	}
	
	public static void loadSteveSkin(kGameProfile profile){
		profile.loadSkin( new SteveSkin() );
	}
	
	public static void loadSkin(Callback<Skin> callback, UUID uuid){
		ProgressFuture<Skin> response = UtilServer.getClient().getSkin(uuid);
		
		response.getAsync(callback);
	}
	
	public static void loadSkin(kGameProfile profile, UUID uuid){
		loadSkin(new Callback<Skin>() {
			@Override
			public void call(Skin skin) {
				 profile.loadSkin(skin);
			}
		}, uuid);
	}
}

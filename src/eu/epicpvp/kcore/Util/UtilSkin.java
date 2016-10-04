package eu.epicpvp.kcore.Util;

import java.util.UUID;

import org.bukkit.entity.Player;

import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.datenclient.client.ProgressFuture;
import dev.wolveringer.skin.Skin;
import dev.wolveringer.skin.SteveSkin;
import eu.epicpvp.kcore.Listener.SkinCatcherListener.SkinCatcherListener;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperGameProfile;
import lombok.Getter;
import lombok.Setter;

public class UtilSkin {
	@Setter
	@Getter
	public static SkinCatcherListener catcher;

	public static void setSkinPlayer(Player player, Skin data){
		WrapperGameProfile profile = (WrapperGameProfile) UtilPlayer.getCraftPlayer(player).getProfile();
		profile.loadSkin(data);
	}

	public static void loadSkin(Callback<Skin> callback, String playerName){
		if(catcher!=null){
			if(catcher.getSkins().containsKey(playerName)){
				callback.call(catcher.getSkins().get(playerName),null);
				return;
			}
		}

		if(UtilServer.getClient().getHandle().isConnected()){
			ProgressFuture<Skin> response = UtilServer.getClient().getSkin(playerName);
			response.getAsync(callback);
		}else{
			callback.call(new SteveSkin(),null);
		}
	}

	public static void loadSkin(WrapperGameProfile profile, String playerName){
		loadSkin(profile, playerName, null);
	}

	public static void loadSkin(WrapperGameProfile profile, String playerName,Callback<Skin> fine){
		if(catcher!=null){
			if(catcher.getSkins().containsKey(playerName)){
				 profile.loadSkin(catcher.getSkins().get(playerName));
				return;
			}
		}

		loadSkin(new Callback<Skin>() {
			@Override
			public void call(Skin skin, Throwable ex) {
				 profile.loadSkin(skin);
				 if(fine!=null)fine.call(skin, null);
			}
		}, playerName);
	}

	public static void loadSteveSkin(WrapperGameProfile profile){
		profile.loadSkin( new SteveSkin() );
	}

	public static void loadSkin(Callback<Skin> callback, UUID uuid){
		if(UtilServer.getClient().getHandle().isConnected()){
			ProgressFuture<Skin> response = UtilServer.getClient().getSkin(uuid);
			response.getAsync(callback);
		}else{
			callback.call(new SteveSkin(),null);
		}
	}

	public static void loadSkin(WrapperGameProfile profile, UUID uuid){
		loadSkin(new Callback<Skin>() {
			@Override
			public void call(Skin skin, Throwable ex) {
				 profile.loadSkin(skin);
			}
		}, uuid);
	}
}

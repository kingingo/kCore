package eu.epicpvp.kcore.Util;

import java.util.UUID;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.ProgressFuture;
import dev.wolveringer.skin.Skin;
import dev.wolveringer.skin.SteveSkin;
import eu.epicpvp.kcore.PacketAPI.Packets.kGameProfile;

public class UtilSkin {
	
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

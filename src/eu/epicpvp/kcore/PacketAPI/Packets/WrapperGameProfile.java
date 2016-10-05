package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import eu.epicpvp.datenserver.definitions.skin.Skin;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;

public class WrapperGameProfile extends GameProfile{
    @Getter
    @Setter
    private String skinValue;
    @Getter
    @Setter
    private String skinSignature;

    public WrapperGameProfile(UUID uuid,String name){
    	this(uuid,name,null);
    }

	public WrapperGameProfile(UUID uuid,String name,Skin data){
		super(uuid,name);
		if(data!=null)loadSkin(data);
	}

	public WrapperGameProfile(GameProfile handle){
		super(handle.getId(),handle.getName());
		if(handle.getProperties().containsKey("textures")){
			getProperties().removeAll("textures");
			for(Property prop : handle.getProperties().get("textures"))
				getProperties().put("textures", prop);
		}
	}

	public void setUUID(UUID uuid){
		UtilReflection.setValue(GameProfile.class, "id", this, uuid);
	}

	public void setName(String name){
		UtilReflection.setValue(GameProfile.class, "name", this, name);
	}

	public void setLegacy(boolean flag){
		UtilReflection.setValue(GameProfile.class, "legacy", this, flag);
	}

	public void loadSkin(Skin data){
		setSkinValue(data.getRawData());
		setSkinSignature(data.getSignature());
		updateTextures();
	}

	public void updateTextures(){
	    getProperties().removeAll("textures");
	    getProperties().put("textures", new Property("textures", getSkinValue(), getSkinSignature()));
	}
}

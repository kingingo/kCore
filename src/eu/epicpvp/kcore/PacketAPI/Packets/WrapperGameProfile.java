package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import dev.wolveringer.skin.Skin;
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
	
	public void setUUID(UUID uuid){
		UtilReflection.setValue("id", this, uuid);
	}
	
	public void setName(String name){
		UtilReflection.setValue("name", this, name);
	}
	
	public void setLegacy(boolean flag){
		UtilReflection.setValue("legacy", this, flag);
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

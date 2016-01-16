package me.kingingo.kcore.Util;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class SkinData{
		
		@Setter
		@Getter
		private String skinName;
		@Setter
		@Getter
		private String skinValue;
		@Setter
		@Getter
		private String skinSignature;
		@Setter
		@Getter
		private UUID uuid;
		
		public SkinData(){}
		
		public SkinData(UUID uuid){
			this(uuid,null,null,null);
		}
		
		public SkinData(UUID uuid, String skinName,String skinValue, String skinSignature){
			this.uuid=uuid;
			this.skinName=skinName;
			this.skinValue=skinValue;
			this.skinSignature=skinSignature;
		}
		
		
		public boolean isReady(){
			return (skinName!=null&&skinValue!=null&&skinSignature!=null);
		}
	}
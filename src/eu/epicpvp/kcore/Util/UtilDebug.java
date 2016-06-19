package eu.epicpvp.kcore.Util;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import lombok.Getter;
import lombok.Setter;

public class UtilDebug {
	@Getter
	@Setter
	public static boolean debug=false;
	
	public static void debug(String methode,String msg){
		debug(methode,new String[]{msg});
	}
	
	public static void debug(String... msg){
		debug(null,msg);
	}
	
	public static void debug(InventoryPageBase page,String methode,String... msg){
		if(isDebug()){
			System.err.println("          ");
			if(page.getInventoryType()!=null)System.err.println("[DebugInv] InventoryType: "+page.getInventoryType());
			if(!page.getTitle().equalsIgnoreCase("Inventory"))System.err.println("[DebugInv] Title: "+page.getTitle());
			if(methode!=null)System.err.println("[DebugInv] Methode: "+methode);
			if(msg!=null)for(String m : msg)System.err.println("[DebugInv] "+m);
			System.err.println("          ");
		}
	}
	
	public static void debug(String methode,String... msg){
		if(isDebug()){
			System.err.println("          ");
			if(methode!=null)System.err.println("[Debug] Methode: "+methode);
			if(msg!=null)for(String m : msg)System.err.println("[Debug] "+m);
			System.err.println("          ");
		}
	}
	
}

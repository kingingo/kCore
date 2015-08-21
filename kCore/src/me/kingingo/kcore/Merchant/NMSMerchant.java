package me.kingingo.kcore.Merchant;

import java.lang.reflect.Method;
import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Merchant.ReflectionUtils.NMSMerchantRecipe;
import me.kingingo.kcore.Merchant.ReflectionUtils.NMSMerchantRecipeList;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;

import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class NMSMerchant implements java.lang.reflect.InvocationHandler {
	private NMSMerchantRecipeList o = new NMSMerchantRecipeList(); //MerchantRecipeList
	@Getter
	ArrayList<Object> offerslist = new ArrayList<>();
	private transient Object c; //EntityHuman
	public Object proxy;
	public String title;
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args) {
		try {
			if (m == null || m.getName() == null) return null;
			Class entityHuman = ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".EntityHuman");
			if (m.getName().equals("a_") && args.length == 1 && args[0] != null && args[0].getClass().isInstance(entityHuman))
				this.a_(args[0]);
			else if (m.getName().equals("b") || m.getName().equals("m_") || m.getName().equals("u_") || m.getName().equals("v_")) //m_ = 1.6.4, b = 1.7.4, u_ = Spigot 1.8, v_ = Spigot 1.8.3
				return this.getEntityHuman();
			else if (m.getName().equals("getOffers") && args.length == 1)
				return this.getOffers(args[0]);
			else if (m.getName().equals("a") && args.length == 1)
				this.a(args[0]);
			else if (m.getName().equals("getScoreboardDisplayName"))
				return this.getScoreboardDisplayName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getEntityHuman() { // Return Class = EntityHuman
		return this.c;
	}
	
	public EntityHuman v_(){
		return ((EntityHuman)c);
	}

	public void a_(Object player) {  //Class = EntityHuman
		this.c = player;
	}

	public Object b() { // Return Class = EntityHuman
		return this.c;
	}

	public Object getOffers(Object player) {  //Return Class = MerchantRecipeList, player Class = EntityHuman
		return this.o.getHandle();
	}

	public void a(Object recipe) {  //recipe Class = MerchantRecipe
		offerslist.add(recipe);
		this.o.add(new NMSMerchantRecipe(recipe));
	}

	/*public void a_(ItemStack is) {
		
	}*/

	/* Other Methods */
	public Player getBukkitEntity() {
		try {
			Class c = ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".EntityHuman");
			Method m = c.getDeclaredMethod("getBukkitEntity");
			m.setAccessible(true);
			return (Player) m.invoke(this.c);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object getScoreboardDisplayName() {
		return UtilReflection.createNMSTextComponent(this.title);
	}

	public void clearRecipes() {
		this.o.clear();
	}

	public void setRecipes(NMSMerchantRecipeList recipes) {
		this.o = recipes;
	}

	public void openTrading(Object player,String t) { //player Class = EntityPlayer
		this.c = player;
		this.title=t;
		
		try {
			Class classs = ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".EntityPlayer");
			Method m;
			if (this.getMethodArgs(classs, "openTrade") == 2) {
				// Older than Spigot 1.8 (maybe Bukkit 1.7.10)
				m = classs.getDeclaredMethod("openTrade", ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".IMerchant"), String.class);
				m.setAccessible(true);
				m.invoke(player, this.proxy, this.title);
			} else {
				// Spigot 1.8 and newer
				m = classs.getDeclaredMethod("openTrade", ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".IMerchant"));
				m.setAccessible(true);
				m.invoke(player, this.proxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getMethodArgs(Class classs, String methodName) {
		for (Method method : classs.getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				return method.getParameterTypes().length;
			}
		}
		return -1;
	}
	
}
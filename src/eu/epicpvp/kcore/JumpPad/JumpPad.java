package eu.epicpvp.kcore.JumpPad;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Util.UtilLocation;
import lombok.Getter;

public class JumpPad {

	protected Location from;
	protected Location to;
	@Getter
	private Vector[] vectors;
	private double m;
	private double b;
	private double a;
	@Getter
	private HashMap<Player,Integer> jump = new HashMap<>();
	
	public JumpPad(Location from,Location to){
		this.from=from;
		this.to=to;
		this.m=(from.getZ()-(to.getZ()))/(from.getX()-(to.getX()));// m = y^1 - y^2 / x^1 - x^2
		this.b=from.getZ()-(this.m*from.getX()); //b = z - m * x
		this.a=-(to.getY()-to.getY()+5)/(to.getX()*to.getX());
		
		double dis = from.distance(to);
		this.vectors= new Vector[4];
		
		System.out.println("[EpicPvP] M: "+this.m);
		System.out.println("[EpicPvP] A: "+this.a);
		System.out.println("[EpicPvP] B: "+this.b);
		System.out.println("[EpicPvP] FROM: Z:"+from.getZ()+" X:"+from.getX()+" Y:"+from.getY());
		System.out.println("[EpicPvP] TO: Z:"+to.getZ()+" X:"+to.getX()+" Y:"+to.getY());
		System.out.println("[EpicPvP] DIS; "+dis);
		
		this.vectors[0]=UtilLocation.FromToVector(from, new Location(from.getWorld(), from.getX()+dis/4, QuadratischeFunktionY(from.getX()+dis/4) ,LineareFunktionZ(from.getX()+dis/4)) );
		this.vectors[1]=UtilLocation.FromToVector(from, new Location(from.getWorld(), from.getX()+(dis/4)*2,QuadratischeFunktionY(from.getX()+(dis/4)*2),LineareFunktionZ(from.getX()+(dis/4)*2)) );
		this.vectors[2]=UtilLocation.FromToVector(from, new Location(from.getWorld(), from.getX()+(dis/4)*3,QuadratischeFunktionY(from.getX()+(dis/4)*3),LineareFunktionZ(from.getX()+(dis/4)*3)) );
		this.vectors[3]=UtilLocation.FromToVector(from, new Location(from.getWorld(), from.getX()+(dis/4)*4,QuadratischeFunktionY(from.getX()+(dis/4)*4),LineareFunktionZ(from.getX()+(dis/4)*4)) );
	}
	
	public void doit(Player player,int i){
		player.setVelocity(this.vectors[i]);
		if(i==3)jump.remove(player);
		if(i==0)jump.put(player, i);
	}
	
	public double max(double a,double b){
		if(a>b)return a;
		return b;
	}
	
	public double min(double a,double b){
		if(a>b)return b;
		return a;
	}
	
	public double highY(){
		if(to.getY()>from.getY()){
			return to.getY();
		}else{
			return from.getY();
		}
	}
	
	public double QuadratischeFunktionY(double x){
		return this.a*(x*x)+(highY()+5);
	}
	
	public double LineareFunktionX(double z){
		return (z-this.b)/this.m;
	}
	
	public double LineareFunktionZ(double x){
		return (this.m*x)+this.b;
	}
	
}

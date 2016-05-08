package eu.epicpvp.kcore.Particle;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilNumber;
import lombok.Getter;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class ParticleShape<E extends Enum<E>, V> {

	protected static final long MOVEMENT_MILLIS = 300;
	private static final double PARTICLES_PER_BLOCK = 6;
	private Map<Vector, E> positions = new HashMap<>();
	@Getter
	private ItemStack item;
	@Getter
	private PermissionType permission;
	
	public ParticleShape(ItemStack item,PermissionType permission){
		this.item=item;
		this.permission=permission;
		initShape();
	}

	public abstract void initShape();

	protected Map<Vector, E> createSymmetricLines(double x1, double y1, double x2, double y2, E part) {
		Map<Vector, E> line1 = createLine(-x1, y1, -x2, y2, part);
		Map<Vector, E> line2 = createLine(x1, y1, x2, y2, part);
		HashMap<Vector, E> result = Maps.newHashMapWithExpectedSize(line1.size() + line2.size());
		result.putAll(line1);
		result.putAll(line2);
		return result;
	}
	
	protected Map<Vector, E> fill(E searchPart, E part){
		Map<Vector, E> result = Maps.newHashMapWithExpectedSize(0);
		
		for(Vector v1 : getPositions().keySet()){
			if(getPositions().get(v1) != searchPart)continue;
			for(Vector v2 : getPositions().keySet()){
				if(getPositions().get(v2) != searchPart)continue;
				
				if( (v2.getX()+0.075)>=v1.getX() && (v2.getX()-0.075)<=v1.getX() && v1.getY() != v2.getY()){
					double y1=0;
					double y2=0;
					
					if(v1.getY() > v2.getY()){
						y1=v1.getY()-0.15;
						y2=v2.getY()+0.15;
					}else{
						y2=v2.getY()-0.15;
						y1=v1.getY()+0.15;
					}
					
					result.putAll(createLine(v1.getX(),y1, v2.getX(), y2, part));
				}
			}
		}
		
		return result;
	}
	
	protected Map<Vector, E> createCircle(double x1, double y1, double z1, double radius, E part) {
		double amount = radius * 64;
		double inc = (2*Math.PI)/amount;
		Map<Vector, E> result = Maps.newHashMapWithExpectedSize(UtilNumber.toInt(amount));
		for(int i = 0; i < amount; i++){
			double angle = i * inc;
			double x = (radius * Math.cos(angle))+x1;
			double z = (radius * Math.sin(angle))+z1;
			Vector v = new Vector(x, y1, z);
			result.put(v, part);
		}
		return result;
	}

	protected Map<Vector, E> createLine(double x1, double y1, double x2, double y2, E part) {
		Vector v1 = new Vector(x1, y1, 0);
		Vector v2 = new Vector(x2, y2, 0);
		Vector diff = v1.clone().subtract(v2);
		double len = diff.length();
		int amount = (int) (Math.round(PARTICLES_PER_BLOCK * len));
		diff.multiply(1.0 / amount);
		amount++;
		Map<Vector, E> result = Maps.newHashMapWithExpectedSize(amount);
		for (int i = 0; i < amount; i++) {
			Vector toPut = v1.clone().add(diff.clone().multiply(-i));
			result.put(toPut, part);
		}
		return result;
	}

	public Map<Vector, E> getPositions() {
		return this.positions;
	}

	public static class ValueHolder<V> {
		public V val;

		public ValueHolder(V val) {
			this.val = val;
		}
	}

	/**
	 * @param player the player to check
	 * @param locVector the base draw location
	 * @return whether to go on drawing
	 */
	public abstract boolean transformPerTick(Player player, Location playerLoc, Vector locVector, ValueHolder<V> valueHolder, Location previous);

	/**
	 * @param player the player to check
	 * @param loc the base draw location, (= player location)
	 * @param particlePos the particle position (change this object!)
	 */
	public abstract Color transformPerParticle(Player player, Location loc, Vector particlePos, E particleType, ValueHolder<V> valueHolder);

	public abstract ValueHolder<V> createValueHolder();
}

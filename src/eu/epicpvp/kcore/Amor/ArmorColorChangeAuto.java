package eu.epicpvp.kcore.Amor;

import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilServer;

public class ArmorColorChangeAuto extends kListener{

	public HashMap<Creature , ArmorColorChange> creatures = new HashMap<>();
	
	public ArmorColorChangeAuto(JavaPlugin instance){
		super(instance,"ArmorColorChangeAuto");
	}
	
	public void addCreature(Creature c,ArmorColorChange change){
		this.creatures.put(c, change);
	}
	
	private ItemStack hand;
	private ArmorColorChange change;
	@EventHandler
	public void CreatureEffect(UpdateEvent ev){
		if(ev.getType()==UpdateType.FASTEST&&!UtilServer.getPlayers().isEmpty()){
			if(!this.creatures.isEmpty()){
				
				for(Creature creature : creatures.keySet()){
					change=creatures.get(creature);
					if(change.getTime()!=-1&&change.getTime() < System.currentTimeMillis()){
						change.setType(ArmorColorChangeType.values()[UtilMath.randomInteger(ArmorColorChangeType.values().length)]);
						change.setTime(System.currentTimeMillis()+TimeSpan.SECOND*20);
						if(change.getType()==ArmorColorChangeType.LOAD){
							creature.getEquipment().setArmorContents(null);
						}
						hand = creature.getEquipment().getItemInHand();
						hand.setType((UtilMath.randomInteger(2)==0 ? UtilItem.rdmAxt() : UtilItem.rdmSchwert()));
						creature.getEquipment().setItemInHand( hand );
					}
					
					switch(change.getType()){
					case LOAD:
						//SETZT 2 VERSCHIEDENE FARBEN
						if(change.getColor()==null){
							change.setColor(new Color[2]);
							change.getColor()[0]=eu.epicpvp.kcore.Util.Color.rdmColor();
							change.getColor()[1]=change.getColor()[0];
						}
						creature.getEquipment().setArmorContents(UtilItem.colorRunArmor(creature.getEquipment().getArmorContents(), change.getColor()));
						break;
					case RAINBOW:
						creature.getEquipment().setArmorContents( UtilItem.rainbowArmor(creature.getEquipment().getArmorContents()) );
						break;
					case RANDOM_ALL:
						creature.getEquipment().setArmorContents( UtilItem.setArmorColor(creature.getEquipment().getArmorContents(), eu.epicpvp.kcore.Util.Color.rdmColor()) );
						break;
					case RANDOM_ONLY:
						if(creature.getEquipment().getArmorContents()[0]==null|| creature.getEquipment().getArmorContents()[0].getType()!=Material.LEATHER_HELMET){
							creature.getEquipment().setArmorContents( UtilItem.setArmorColor(creature.getEquipment().getArmorContents(), eu.epicpvp.kcore.Util.Color.rdmColor()) );
						}else{
							creature.getEquipment().setHelmet( UtilItem.LSetColor(creature.getEquipment().getHelmet(), eu.epicpvp.kcore.Util.Color.rdmColor()) );
							creature.getEquipment().setChestplate( UtilItem.LSetColor(creature.getEquipment().getChestplate(), eu.epicpvp.kcore.Util.Color.rdmColor()) );
							creature.getEquipment().setLeggings( UtilItem.LSetColor(creature.getEquipment().getLeggings(), eu.epicpvp.kcore.Util.Color.rdmColor()) );
							creature.getEquipment().setBoots( UtilItem.LSetColor(creature.getEquipment().getBoots(), eu.epicpvp.kcore.Util.Color.rdmColor()) );
						}
						break;
					}
				}
				
			}
		}
	}
	
}

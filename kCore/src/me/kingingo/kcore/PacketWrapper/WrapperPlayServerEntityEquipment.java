package me.kingingo.kcore.PacketWrapper;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayServerEntityEquipment extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_EQUIPMENT;
    
    public WrapperPlayServerEntityEquipment() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityEquipment(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve named Entity ID.
     * @return The current Entity ID
    */
    public int getEntityId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Retrieve the entity.
     * @param world - the current world of the entity.
     * @return The entity.
     */
    public Entity getEntity(World world) {
    	return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity.
     * @param event - the packet event.
     * @return The entity.
     */
    public Entity getEntity(PacketEvent event) {
    	return getEntity(event.getPlayer().getWorld());
    }
    
    /**
     * Set named Entity ID.
     * @param value - new value.
    */
    public void setEntityId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve equipment slot.
     * <p>
     * Here zero indicates a held weapon or item, while 1 is boots, 2 is leggings, 
     * 3 is chestplate and 4 is helmet.
     * @return The current slot
    */
    public short getSlot() {
        return handle.getIntegers().read(1).shortValue();
    }
    
    /**
     * Set equipment slot.
     * <p>
     * Here zero indicates a held weapon or item, while 1 is boots, 2 is leggings, 
     * 3 is chestplate and 4 is helmet.
     * @param value - new value.
     */
    public void setSlot(short value) {
        handle.getIntegers().write(1, (int) value);
    }
    
    /**
     * Retrieve the equipped item.
     * @return The current item
    */
    public ItemStack getItem() {
        return handle.getItemModifier().read(0);
    }
    
    /**
     * Set the equipped item.
     * @param value - new value.
    */
    public void setItem(ItemStack value) {
        handle.getItemModifier().write(0, value);
    }
}
package me.kingingo.kcore.Hologram.wrapper;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerEntityMoveLook extends WrapperPlayServerEntity {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_MOVE_LOOK;
    
    public WrapperPlayServerEntityMoveLook() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityMoveLook(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the relative movement in the x axis.
     * <p>
     * Note that this cannot exceed 4 blocks in either direction.
     * @return The current dX
    */
    public double getDx() {
        return handle.getBytes().read(0) / 32.0D;
    }
    
    /**
     * Set the relative movement in the x axis.
     * <p>
     * Note that this cannot exceed 4 blocks in either direction.
     * @param value - new value.
    */
    public void setDx(double value) {
    	if (Math.abs(value) > 4)
    		throw new IllegalArgumentException("Displacement cannot exceed 4 meters.");
        handle.getBytes().write(0, (byte) Math.min(Math.floor(value * 32.0D), 127));
    }
    
    /**
     * Retrieve the relative movement in the y axis.
     * <p>
     * Note that this cannot exceed 4 blocks in either direction.
     * @return The current dY
    */
    public double getDy() {
        return handle.getBytes().read(1) / 32.0D;
    }
    
    /**
     * Set the relative movement in the y axis.
     * <p>
     * Note that this cannot exceed 4 blocks in either direction.
     * @param value - new value.
    */
    public void setDy(double value) {
    	if (Math.abs(value) > 4)
    		throw new IllegalArgumentException("Displacement cannot exceed 4 meters.");
        handle.getBytes().write(1, (byte) Math.min(Math.floor(value * 32.0D), 127));
    }
    
    /**
     * Retrieve the relative movement in the z axis.
     * <p>
     * Note that this cannot exceed 4 blocks in either direction.
     * @return The current dZ
    */
    public double getDz() {
        return handle.getBytes().read(2) / 32.0D;
    }
    
    /**
     * Set the relative movement in the z axis.
     * <p>
     * Note that this cannot exceed 4 blocks in either direction.
     * @param value - new value.
    */
    public void setDz(double value) {
    	if (Math.abs(value) > 4)
    		throw new IllegalArgumentException("Displacement cannot exceed 4 meters.");
        handle.getBytes().write(2, (byte) Math.min(Math.floor(value * 32.0D), 127));
    }
    
    /**
     * Retrieve the yaw of the current entity.
     * @return The current Yaw
    */
    public float getYaw() {
        return (handle.getBytes().read(3) * 360.F) / 256.0F;
    }
    
    /**
     * Set the yaw of the current entity.
     * @param value - new yaw.
    */
    public void setYaw(float value) {
        handle.getBytes().write(3, (byte) (value * 256.0F / 360.0F));
    }
    
    /**
     * Retrieve the pitch of the current entity.
     * @return The current pitch
    */
    public float getPitch() {
        return (handle.getBytes().read(4) * 360.F) / 256.0F;
    }
    
    /**
     * Set the pitch of the current entity.
     * @param value - new pitch.
    */
    public void setPitch(float value) {
        handle.getBytes().write(4, (byte) (value * 256.0F / 360.0F));
    }
}
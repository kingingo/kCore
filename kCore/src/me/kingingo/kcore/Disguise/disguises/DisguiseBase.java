package me.kingingo.kcore.Disguise.disguises;
import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.livings.DisguiseEnderman;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public abstract class DisguiseBase
{
  protected net.minecraft.server.v1_8_R3.Entity Entity;
  @Getter
  protected kDataWatcher DataWatcher;
  private DisguiseBase _soundDisguise;

  public DisguiseBase(org.bukkit.entity.Entity entity)
  {
    this.Entity = ((CraftEntity)entity).getHandle();
    this.DataWatcher = new kDataWatcher(new DummyEntity(((CraftWorld)entity.getWorld()).getHandle()));

    this.DataWatcher.a(0, Byte.valueOf((byte)0));
    this.DataWatcher.a(1, Short.valueOf((short)300));

    this._soundDisguise = this;
  }

  public void UpdateDataWatcher()
  {
    this.DataWatcher.watch(0, Byte.valueOf(this.Entity.getDataWatcher().getByte(0)));
    this.DataWatcher.watch(1, Short.valueOf(this.Entity.getDataWatcher().getShort(1)));

    if ((this instanceof DisguiseEnderman))
    {
      this.DataWatcher.watch(0, Byte.valueOf((byte)(this.DataWatcher.getByte(0) & 0xFFFFFFFE)));
    }
  }

  public abstract kPacket GetSpawnPacket();

  public abstract EntityType GetEntityTypeId();

  public kPacket GetMetaDataPacket()
  {
    UpdateDataWatcher();
    return new kPacketPlayOutEntityMetadata(this.Entity.getId(), this.DataWatcher);
  }

  public void setSoundDisguise(DisguiseBase soundDisguise)
  {
    this._soundDisguise = soundDisguise;

    if (this._soundDisguise == null)
      this._soundDisguise = this;
  }

  public void playHurtSound()
  {
    this.Entity.world.makeSound(this.Entity, this._soundDisguise.getHurtSound(), this._soundDisguise.getVolume(), this._soundDisguise.getPitch());
  }

  public void playHurtSound(Location location)
  {
    this.Entity.world.makeSound(location.getX(), location.getY(), location.getZ(), this._soundDisguise.getHurtSound(), this._soundDisguise.getVolume(), this._soundDisguise.getPitch());
  }

  public void UpdateEntity(net.minecraft.server.v1_8_R3.Entity entity)
  {
    this.Entity = entity;
  }

  public net.minecraft.server.v1_8_R3.Entity GetEntity()
  {
    return this.Entity;
  }

  public int GetEntityId()
  {
    return this.Entity.getId();
  }

  protected abstract String getHurtSound();

  protected abstract float getVolume();

  protected abstract float getPitch();
}
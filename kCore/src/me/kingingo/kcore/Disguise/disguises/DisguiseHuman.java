package me.kingingo.kcore.Disguise.disguises;
import org.bukkit.entity.Entity;

public abstract class DisguiseHuman extends DisguiseLiving
{
  public DisguiseHuman(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, Byte.valueOf((byte)0));
    this.DataWatcher.a(17, Float.valueOf(0.0F));
    this.DataWatcher.a(18, Integer.valueOf(0));
  }
}
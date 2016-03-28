package eu.epicpvp.kcore.Disguise.disguises;

import org.bukkit.entity.Entity;

import net.minecraft.server.v1_8_R3.MathHelper;

public abstract class DisguiseAgeable extends DisguiseCreature
{
  public DisguiseAgeable(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(12, new Byte((byte) 0));
  }

  public boolean isBaby()
  {
    return getAge() < 0;
  }

  public void setBaby()
  {
    this.DataWatcher.watch(12, Integer.valueOf(-24000));
  }
  
  public int getAge(){
	  return this.DataWatcher.getByte(12); 
  }

  public void setAge(int i) {
	  this.DataWatcher.watch(12, Integer.valueOf((byte)MathHelper.clamp(i, -1, 1)));  
  }
  
}